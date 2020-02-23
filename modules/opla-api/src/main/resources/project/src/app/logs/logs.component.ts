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
  logs: string = "";

  constructor(fb: FormBuilder) {
    this.options = fb.group({
      hideRequired: this.hideRequiredControl,
      floatLabel: this.floatLabelControl,
    });
    OptimizationService.onOptimizationInfo.asObservable().subscribe(value => {
      if (value.status === "RUNNING") {
        if (value.logs && value.logs !== "") {
          this.logs += "\n" + value.logs;
        } else {
          if (!this.logs.includes(value.status + " Thread " + value.threadId)) {
            this.logs += value.status + " Thread " + value.threadId;
          }
        }
      }

    })
  }

  ngOnInit() {
  }
}
