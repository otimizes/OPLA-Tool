import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {OptimizationDto} from "../dto/optimization-dto";
import {OptimizationService} from "../services/optimization.service";
import {OptimizationInfo} from "../dto/optimization-info";

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
  public static lastLog = "";
  infos: any;

  constructor(fb: FormBuilder) {
    this.options = fb.group({
      hideRequired: this.hideRequiredControl,
      floatLabel: this.floatLabelControl,
    });
    OptimizationService.onOptimizationInfo.asObservable().subscribe(value => {
      if (value && value.status === "RUNNING" && value.threadId) {
        if (value.logs && value.logs !== "" && LogsComponent.lastLog !== value.logs) {
          LogsComponent.lastLog = value.logs;
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
        if (!OptimizationService.getOptimizationInfo()) {
          this.infos.forEach(info => {
            let hash = Object.keys(info)[0];
            if (info[hash].length <= 0 || info[hash][0].status == 'INTERACT')
              console.log("chamou no logs, info", info)
              this.optimizationService.getInteraction(Object.keys(info)[0]).subscribe(interaction => {
                if (interaction.solutionSet) {
                  let optimizationInfo = new OptimizationInfo();
                  optimizationInfo.hash = hash;
                  optimizationInfo.status = 'INTERACT';
                  this.optimizationService.startEventListener(optimizationInfo);
                  console.log(OptimizationService.getOptimizationInfo())
                }
              })
          })
        }
      });
  }

  getInfos(infos: any) {
    let infosR = [];
    if (infos) {
      for (let info of infos) {
        for (let key of Object.keys(info)) {
          infosR.push({
            id: key,
            infos: info[key]
          })
        }
      }
    }
    return infosR.filter(info => info.infos[0] && info.infos[0].status !== 'COMPLETE');
  }

  killOptimizationProcess(info, index) {
    this.optimizationService.killOptimizationProcess(info.id).subscribe(() => {
      this.infos.splice(index, 1);
      OptimizationService.clearOptimizationInfo();
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
