import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl} from "@angular/forms";
import {ExperimentService} from "../services/experiment.service";
import {MatAutocomplete} from "@angular/material/autocomplete";
import {ExperimentConfigurationService} from "../services/experiment-configuration.service";
import {ObjectiveService} from "../services/objective.service";
import {InfoService} from "../services/info.service";
import {Observable} from "rxjs";
import {map, startWith} from "rxjs/operators";
import {COMMA, ENTER} from "@angular/cdk/keycodes";

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent implements OnInit {


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
  multi: any[];
  view: any[] = [700, 400];
  showXAxis: boolean = true;
  showYAxis: boolean = true;
  gradient: boolean = false;
  showLegend: boolean = true;
  showXAxisLabel: boolean = true;
  showYAxisLabel: boolean = true;
  roundDomains: boolean = true;
  showDataLabel: boolean = true;
  disabled = [];

  onSelect(data): void {
    for (let i = 0; i < this.multi.length; i++) {
      for (let j = 0; j < this.multi[i].series.length; j++) {
        if (this.multi[i].series[j].name === data) {
          this.disabled.push({
            i: i,
            j: j,
            serie: Object.assign({}, this.multi[i].series[j])
          });
        }
      }
    }
    let newMulti = Object.assign([], this.multi);
    for (let disabled of this.disabled) {
      console.log(disabled, newMulti[disabled.i].series[disabled.j]);
      newMulti[disabled.i].series.splice(disabled.j, 1)
    }
    this.multi = newMulti;
  }

  onActivate(data): void {
    // console.log('Activate', JSON.parse(JSON.stringify(data)));
  }

  onDeactivate(data): void {
    // console.log('Deactivate', JSON.parse(JSON.stringify(data)));
  }

  constructor(fb: FormBuilder) {
    Object.assign(this, {multi: this.multi})
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

  createGraphs(selectedExperiments: any[]) {
    setTimeout(() => {
      if (selectedExperiments.length > 0) {
        let chart = [];
        for (let i = 0; i < selectedExperiments.length; i++) {
          let experiment = selectedExperiments[i];
          let chartI = {name: experiment.id + ' - ' + experiment.name, series: []};
          let objectiveNames = this.getObjectivesNamesByStr(experiment.experimentConfiguration.objectives)
          for (let obj of experiment.objective) {
            if (obj.execution) {
              let objectives = this.getObjectivesByStr(obj.objectives);
              for (let j = 0; j < objectives.length; j++) {
                chartI.series.push({
                  name: objectiveNames[j],
                  value: objectives[j]
                })
              }
            }
          }
          chart.push(chartI);
        }
        console.log(selectedExperiments, chart);
        this.multi = chart;
      }
    }, 500)

  }

  getObjectivesNamesByStr(str) {
    return str.replace('[', '').replace(']', '').split(',');
  }

  getObjectivesByStr(str) {
    return str.split("|");
  }
}
