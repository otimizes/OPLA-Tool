import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {OptimizationDto} from "./dto/optimization-dto";
import {MatStepper} from "@angular/material/stepper";
import {OptimizationService} from "./services/optimization.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {OptimizationInfo} from "./dto/optimization-info";
import {ExperimentService} from "./services/experiment.service";
import {ExperimentConfigurationService} from "./services/experiment-configuration.service";
import {ObjectiveService} from "./services/objective.service";
import {InfoService} from "./services/info.service";
import {STEPPER_GLOBAL_OPTIONS} from "@angular/cdk/stepper";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [{
    provide: STEPPER_GLOBAL_OPTIONS, useValue: { displayDefaultIndicatorType: false }
  }]
})
export class AppComponent implements OnInit, AfterViewInit {
  title = 'static';


  constructor() {

  }

  ngAfterViewInit(): void {
  }

  ngOnInit(): void {
  }

}
