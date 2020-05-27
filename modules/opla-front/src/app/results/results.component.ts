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
    var colors = d3.scale.category20b();
    var pc = d3.parcoords()("#opla-d3-chart")
      .data(data)
      .hideAxis(["experimentId", "executionId"])
      .color(function (d, i) {
        return colors(d.experimentId + d.executionId);
      })
      .composite("darken")
      .render()
      .alpha(0.35)
      .brushMode("1D-axes")  // enable brushing
      .interactive();
  }

  createGraphs(selectedExperiments: any[]) {
    setTimeout(() => {
      if (selectedExperiments.length > 0) {
        let chart = selectedExperiments[0].experimentConfiguration.objectives.replace('[', '').replace(']', '') + ",experimentId,executionId\n";
        for (let experiment of selectedExperiments) {
          for (let obj of experiment.objective) {
            chart += obj.objectives.split("|").join(",") + "," + obj.experiment.id + "," + (obj.execution ? obj.execution.id : 0) + "\n";
          }
        }
        this.createParallelChart(d3.csv.parse(chart));
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
