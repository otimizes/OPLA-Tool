import {AfterViewInit, Component, ElementRef, HostListener, Input, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl} from "@angular/forms";
import {ExperimentService} from "../services/experiment.service";
import {MatAutocomplete} from "@angular/material/autocomplete";
import {ExperimentConfigurationService} from "../services/experiment-configuration.service";
import {ObjectiveService} from "../services/objective.service";
import {InfoService} from "../services/info.service";
import {Observable} from "rxjs";
import {map, startWith} from "rxjs/operators";
import {COMMA, ENTER} from "@angular/cdk/keycodes";

declare var d3: any;

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent implements OnInit, AfterViewInit {
  selectedExperiments = [];
  experiments = [];
  @Input() experimentConfigurationService: ExperimentConfigurationService;
  @Input() experimentService: ExperimentService;
  @Input() objectiveService: ObjectiveService;
  @Input() infoService: InfoService;
  @ViewChild('experimentInput', {static: false}) experimentInput: ElementRef<HTMLInputElement>;
  @ViewChild('auto', {static: false}) matAutocomplete: MatAutocomplete;
  experimentCtrl = new FormControl();
  separatorKeysCodes: number[] = [ENTER, COMMA];
  filteredExperiments: Observable<string[]>;


  constructor(fb: FormBuilder) {
  }

  private _filter(value: any): string[] {
    if (!(value instanceof String)) {
      return this.experiments;
    }
    const filterValue = value.toLowerCase();

    return this.experiments.filter(exp => (exp.id + exp.name).toLowerCase().includes(filterValue));
  }

  showInfo(experiment) {
    this.experimentConfigurationService.findByExperiment(experiment.id).subscribe(res => {
      experiment.experimentConfiguration = res.values[0];
    });
    this.objectiveService.findByExperiment(experiment.id).subscribe(res => {
      experiment.objective = res.values;
    });
    this.infoService.findByExperiment(experiment.id).subscribe(res => {
      experiment.info = res.values;
    });
  }

  ngOnInit() {
    this.searchExperiments();
  }

  ngAfterViewInit(): void {
  }

  searchExperiments() {
    this.experimentService.getAll().subscribe(results => {
      this.experiments = results.values;
      this.filteredExperiments = this.experimentCtrl.valueChanges.pipe(
        startWith(null),
        map((exp: string | null) => exp ? this._filter(exp) : this.experiments.slice()));
    })
  }

  remove($event: any) {
    this.selectedExperiments.splice(this.selectedExperiments.indexOf($event), 1)
  }

  add(event: any) {
    const input = event.input;
    const value = event.value;

    if ((value || '').trim()) {
      this.selectedExperiments.push(value.trim());
    }

    if (input) {
      input.value = '';
    }

    this.experimentCtrl.setValue(null);
  }

  selected($event: any) {
    let value = $event.option.value;
    this.showInfo(value);
    this.selectedExperiments.push(value);
    this.experimentInput.nativeElement.value = '';
    this.experimentCtrl.setValue(null);
    this.createGraphs(this.selectedExperiments);
  }

  createParallelChart(data) {
    d3.select("#opla-d3-parallel-chart-svg").remove();
    let dimensions = Object.keys(data[0]).filter(d => d !== "executionId" && d !== "experimentId");
    let domain = [];
    let max = 0;
    for (let d of data) {
      if (!domain.includes(d.experimentId)) {
        domain.push(d.experimentId);
      }
      for (let key of dimensions) {
        let vv = Number(d[key].trim());
        if (vv > max) {
          max = vv;
        }
      }
    }
    var svga = d3.select("#opla-d3-parallel-chart");
    var margin = {top: 30, right: 50, bottom: 10, left: 50},
      width = svga.node().getBoundingClientRect().width - 100,
      height = 400 - margin.top - margin.bottom;

    let svg = svga
      .append("svg")
      .attr("width", "100%")
      .attr("id", "opla-d3-parallel-chart-svg")
      .attr("height", height + margin.top + margin.bottom)
      .append("g")
      .attr("transform",
        "translate(" + margin.left + "," + margin.top + ")");

    var color = d3.scaleOrdinal()
      .domain(domain)
      .range(domain.map(d => d3.schemeCategory20b[Number(d)]));

    // Here I set the list of dimension manually to control the order of axis:
    var y = {}
    for (let i in dimensions) {
      let name = dimensions[i]
      y[name] = d3.scaleLinear()
        .domain([0, max]) // --> Same axis range for each group
        // --> different axis range for each group --> .domain( [d3.extent(data, function(d) { return +d[name]; })] )
        .range([height, 0])
    }

    // Build the X scale -> it find the best position for each Y axis
    let x = d3.scalePoint()
      .range([0, width])
      .domain(dimensions);

    // Highlight the specie that is hovered
    var highlight = function (d) {

      let experimentId = d.experimentId

      // first every group turns grey
      d3.selectAll(".line")
        .transition().duration(200)
        .style("stroke", "lightgrey")
        .style("opacity", "0.2")
      // Second the hovered specie takes its color
      d3.selectAll("." + experimentId)
        .transition().duration(200)
        .style("stroke", color(experimentId))
        .style("opacity", "1")
    }

    // Unhighlight
    var doNotHighlight = function (d) {
      d3.selectAll(".line")
        .transition().duration(200).delay(1000)
        .style("stroke", function (d) {
          return (color(d.experimentId))
        })
        .style("opacity", "1")
    }

    // The path function take a row of the csv as input, and return x and y coordinates of the line to draw for this raw.
    function path(d) {
      return d3.line()(dimensions.map(function (p) {
        return [x(p), y[p](d[p])];
      }));
    }

    // Draw the lines
    svg
      .selectAll("myPath")
      .data(data)
      .enter()
      .append("path")
      .attr("class", function (d) {
        return "line " + d.experimentId
      }) // 2 class for each line: 'line' and the group name
      .attr("d", path)
      .style("fill", "none")
      .style("stroke", function (d) {
        return (color(d.experimentId))
      })
      .style("opacity", 0.5)
      .on("mouseover", highlight)
      .on("mouseleave", doNotHighlight)

    // Draw the axis:
    svg.selectAll("myAxis")
    // For each dimension of the dataset I add a 'g' element:
      .data(dimensions).enter()
      .append("g")
      .attr("class", "axis")
      // I translate this element to its right position on the x axis
      .attr("transform", function (d) {
        return "translate(" + x(d) + ")";
      })
      // And I build the axis with the call function
      .each(function (d) {
        d3.select(this).call(d3.axisLeft().ticks(5).scale(y[d]));
      })
      // Add axis title
      .append("text")
      .style("text-anchor", "middle")
      .attr("y", -9)
      .text(function (d) {
        return d;
      })
      .style("fill", "black")

  }

  createBoxplotChart(data) {
    d3.select("#opla-d3-boxplot-chart-svg").remove();
    let dimensions = Object.keys(data[0]).filter(d => d !== "executionId" && d !== "experimentId");
    let domain = [];
    let max = 0;
    for (let d of data) {
      if (!domain.includes(d.experimentId)) {
        domain.push(d.experimentId);
      }
      for (let key of dimensions) {
        let vv = Number(d[key].trim());
        if (vv > max) {
          max = vv;
        }
      }
    }
    var svga = d3.select("#opla-d3-boxplot-chart");
    var margin = {top: 30, right: 50, bottom: 10, left: 50},
      width = svga.node().getBoundingClientRect().width - 100,
      height = 400 - margin.top - margin.bottom;

    let svg = svga
      .append("svg")
      .attr("width", "100%")
      .attr("id", "opla-d3-boxplot-chart-svg")
      .attr("height", height + margin.top + margin.bottom)
      .append("g")
      .attr("transform",
        "translate(" + margin.left + "," + margin.top + ")");


    var sumstat = d3.nest() // nest function allows to group the calculation per level of a factor
      .key(function (d) {
        return d.experimentId;
      })
      .rollup(function (d) {
        console.log(d)
        let q1 = d3.quantile(d.map(function (g) {
          return Number(g.executionId);
        }).sort(d3.ascending), .25)
          console.log("-------", q1)
        let median = d3.quantile(d.map(function (g) {
          return Number(g.executionId);
        }).sort(d3.ascending), .5)
        let q3 = d3.quantile(d.map(function (g) {
          return Number(g.executionId);
        }).sort(d3.ascending), .75)
        let interQuantileRange = q3 - q1
        let min = q1 - 1.5 * interQuantileRange
        let max = q3 + 1.5 * interQuantileRange
        let toR = {q1: q1, median: median, q3: q3, interQuantileRange: interQuantileRange, min: min, max: max};
        console.log(toR)
        return (toR)
      })
      .entries(data)

    // Show the X scale
    var x = d3.scaleBand()
      .range([0, width])
      .domain(domain)
      .paddingInner(1)
      .paddingOuter(.5)
    svg.append("g")
      .attr("transform", "translate(0," + height + ")")
      .call(d3.axisBottom(x))

    // Show the Y scale
    var y = d3.scaleLinear()
      .domain([3, 9])
      .range([height, 0])
    svg.append("g").call(d3.axisLeft(y))

    // Show the main vertical line
    svg
      .selectAll("vertLines")
      .data(sumstat)
      .enter()
      .append("line")
      .attr("x1", function (d) {
        return (x(d.key))
      })
      .attr("x2", function (d) {
        return (x(d.key))
      })
      .attr("y1", function (d) {
        return (y(d.value.min))
      })
      .attr("y2", function (d) {
        return (y(d.value.max))
      })
      .attr("stroke", "black")
      .style("width", 40)

    // rectangle for the main box
    var boxWidth = 100
    svg
      .selectAll("boxes")
      .data(sumstat)
      .enter()
      .append("rect")
      .attr("x", function (d) {
        return (x(d.key) - boxWidth / 2)
      })
      .attr("y", function (d) {
        return (y(d.value.q3))
      })
      .attr("height", function (d) {
        return (y(d.value.q1) - y(d.value.q3))
      })
      .attr("width", boxWidth)
      .attr("stroke", "black")
      .style("fill", "#69b3a2")

    // Show the median
    svg
      .selectAll("medianLines")
      .data(sumstat)
      .enter()
      .append("line")
      .attr("x1", function (d) {
        return (x(d.key) - boxWidth / 2)
      })
      .attr("x2", function (d) {
        return (x(d.key) + boxWidth / 2)
      })
      .attr("y1", function (d) {
        return (y(d.value.median))
      })
      .attr("y2", function (d) {
        return (y(d.value.median))
      })
      .attr("stroke", "black")
      .style("width", 80)

// Add individual points with jitter
    var jitterWidth = 50
    svg
      .selectAll("indPoints")
      .data(data)
      .enter()
      .append("circle")
      .attr("cx", function (d) {
        return (x(d.experimentId) - jitterWidth / 2 + Math.random() * jitterWidth)
      })
      .attr("cy", function (d) {
        return (y(d.executionId))
      })
      .attr("r", 4)
      .style("fill", "white")
      .attr("stroke", "black")

  }

  createGraphs(selectedExperiments: any[]) {
    setTimeout(() => {
      if (selectedExperiments.length > 0) {
        let chart = selectedExperiments[0].experimentConfiguration.objectives.replace('[', '').replace(']', '') + ",experimentId,executionId\n";
        for (let experiment of selectedExperiments) {
          for (let obj of experiment.objective) {
            chart += obj.objectives.split("|").map(v => v.trim()).join(",") + "," + obj.experiment.id + "," + (obj.execution ? obj.execution.id : 0) + "\n";
          }
        }
        this.createParallelChart(d3.csvParse(chart));
        this.createBoxplotChart(d3.csvParse(chart));
      }
    }, 500)

  }

  doit: any;

  resizeChart() {
    let selectExp = [];
    for (let exp of this.selectedExperiments) {
      selectExp.push(Object.assign({}, exp));
    }
    this.selectedExperiments = [];
    setTimeout(() => {
      this.selectedExperiments = selectExp;
      this.createGraphs(selectExp);
    }, 250);
  }

  @HostListener('window:resize', ['$event'])
  onResize(event) {
    clearTimeout(this.doit);
    this.doit = setTimeout(() => this.resizeChart(), 250);
  }

}
