import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {OptimizationDto} from "../dto/optimization-dto";
import {OptimizationService} from "../services/optimization.service";

@Component({
  selector: 'app-logs',
  templateUrl: './logs.component.html',
  styleUrls: ['./logs.component.css']
})
export class LogsComponent implements OnInit {

  options: FormGroup;
  hideRequiredControl = new FormControl(false);
  floatLabelControl = new FormControl('auto');
  @Input() optimizationDto: OptimizationDto;
  @Input() optimizationService: OptimizationService;
  logs: string = "";
  infos: any;

  constructor(fb: FormBuilder) {
    this.options = fb.group({
      hideRequired: this.hideRequiredControl,
      floatLabel: this.floatLabelControl,
    });
    OptimizationService.onOptimizationInfo.asObservable().subscribe(value => {
      if (value && value.status === "RUNNING") {
        if (value.logs && value.logs !== "") {
          this.logs += "\n" + value.logs;
        } else {
          if (!this.logs.includes(value.status + " Thread " + value.hash)) {
            this.logs += value.status + " Thread " + value.hash;
          }
        }
      }
    });
  }

  ngOnInit() {
    this.getOptimizationInfos();
    OptimizationService.onOptimizationStart.asObservable().subscribe(value => this.getOptimizationInfos());
    OptimizationService.onOptimizationFinish.asObservable().subscribe(value => this.getOptimizationInfos());
  }

  getOptimizationInfos() {
    this.optimizationService.getOptimizationInfos()
      .subscribe(infos => {
        this.infos = infos.infos;
      });
  }

  getInfos(infos: any) {
    let infosR = [];
    for (let info of infos) {
      for (let key of Object.keys(info)) {
        infosR.push({
          id: key,
          infos: info[key]
        })
      }
    }
    return infosR;
  }

  killOptimizationProcess(info, index) {
    this.optimizationService.killOptimizationProcess(info.id).subscribe(() => {
      this.infos.splice(index, 1)
    })
  }

  selectOptimizationProcess(info: any) {
    OptimizationService.clearOptimizationInfo();
    this.optimizationService.startEventListener(info.infos[0])
  }

  unselectOptimizationProcess(info: any) {
    OptimizationService.clearOptimizationInfo();
  }
}
