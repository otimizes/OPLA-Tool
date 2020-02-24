import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {animate, state, style, transition, trigger} from "@angular/animations";
import {OptimizationService} from "../services/optimization.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ExperimentService} from "../services/experiment.service";
import {ExperimentConfigurationService} from "../services/experiment-configuration.service";
import {Observable} from "rxjs";
import {ObjectiveService} from "../services/objective.service";
import {InfoService} from "../services/info.service";

@Component({
  selector: 'app-experiments',
  templateUrl: './experiments.component.html',
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
  styleUrls: ['./experiments.component.css']
})
export class ExperimentsComponent implements OnInit {

  experiments = [];
  @Output() download: EventEmitter<any> = new EventEmitter<any>();
  @Input() experimentService: ExperimentService;
  @Input() objectiveService: ObjectiveService;
  @Input() infoService: InfoService;
  @Input() experimentConfigurationService: ExperimentConfigurationService;
  columnsToDisplay = ['id', 'name', 'algorithm', 'createdAt', 'description'];
  expandedElement: any | null;

  constructor(private snackBar: MatSnackBar) {

  }

  ngOnInit() {
    this.experimentService.getAll().subscribe(experiments => {
      this.experiments = experiments.values;
    });

    OptimizationService.onOptimizationInfo.asObservable().subscribe(value => {
      if (value && value.status === "COMPLETE") {
        this.experimentService.getAll().subscribe(experiments => {
          this.experiments = experiments.values;
        });
      }
    });
  }

  showInfo(experiment) {
    experiment.experimentConfiguration = this.experimentConfigurationService.findByExperiment(experiment.id)
    experiment.objective = this.objectiveService.findByExperiment(experiment.id)
    experiment.info = this.infoService.findByExperiment(experiment.id)
  }

  downloadInfo(experiment) {
    this.infoService.findByExperiment(experiment.id).subscribe(info => {
      var dataStr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(info));
      var downloadAnchorNode = document.createElement('a');
      downloadAnchorNode.setAttribute("href",     dataStr);
      downloadAnchorNode.setAttribute("download", experiment.id + ".info.json");
      document.body.appendChild(downloadAnchorNode); // required for firefox
      downloadAnchorNode.click();
      downloadAnchorNode.remove();
    })
  }

  downloadFile(data, filename = 'data') {
    let csvData = this.convertToCSV(data, ['name', 'age', 'average', 'approved', 'description']);
    let blob = new Blob(['\ufeff' + csvData], {type: 'text/csv;charset=utf-8;'});
    let dwldLink = document.createElement("a");
    let url = URL.createObjectURL(blob);
    let isSafariBrowser = navigator.userAgent.indexOf('Safari') != -1 && navigator.userAgent.indexOf('Chrome') == -1;
    if (isSafariBrowser) {  //if Safari open in new window to save file with random filename.
      dwldLink.setAttribute("target", "_blank");
    }
    dwldLink.setAttribute("href", url);
    dwldLink.setAttribute("download", filename + ".csv");
    dwldLink.style.visibility = "hidden";
    document.body.appendChild(dwldLink);
    dwldLink.click();
    document.body.removeChild(dwldLink);
  }

  convertToCSV(objArray, headerList) {
    let array = typeof objArray != 'object' ? JSON.parse(objArray) : objArray;
    let str = '';
    let row = 'S.No,';
    for (let index in headerList) {
      row += headerList[index] + ',';
    }
    row = row.slice(0, -1);
    str += row + '\r\n';
    for (let i = 0; i < array.length; i++) {
      let line = (i + 1) + '';
      for (let index in headerList) {
        let head = headerList[index];
        line += ',' + array[i][head];
      }
      str += line + '\r\n';
    }
    return str;
  }

}
