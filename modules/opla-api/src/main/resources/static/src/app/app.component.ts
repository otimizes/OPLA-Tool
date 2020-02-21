import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {OptimizationDto} from "./optimization-dto";
import {MatStepper} from "@angular/material/stepper";
import {AppService} from "./app.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterViewInit {
  title = 'static';
  isLinear = false;
  generalFormGroup: FormGroup;
  executionFormGroup: FormGroup;
  patternFormGroup: FormGroup;
  resultsFormGroup: FormGroup;
  experimentsFormGroup: FormGroup;
  logsFormGroup: FormGroup;
  optimizationDto: OptimizationDto = new OptimizationDto();
  @ViewChild('stepper', {static: true}) stepper: MatStepper;

  constructor(private _formBuilder: FormBuilder, private service: AppService, private snackBar: MatSnackBar) {
    service.getDto().subscribe(dto => {
      console.log("-->", dto)
      this.optimizationDto = dto
    })
  }

  ngAfterViewInit(): void {
    this.stepper.selectedIndex = 1;
  }

  ngOnInit() {
    this.generalFormGroup = this._formBuilder.group({
      generalCtrl: ['', Validators.required]
    });
    this.executionFormGroup = this._formBuilder.group({
      executionCtrl: ['', Validators.required]
    });
    this.patternFormGroup = this._formBuilder.group({
      patternCtrl: ['', Validators.required]
    });
    this.resultsFormGroup = this._formBuilder.group({
      resultsCtrl: ['', Validators.required]
    });
    this.experimentsFormGroup = this._formBuilder.group({
      experimentsCtrl: ['', Validators.required]
    });
    this.logsFormGroup = this._formBuilder.group({
      logsCtrl: ['', Validators.required]
    });
  }

  isRunning() {
    return AppService.isRunning();
  }

  run(optimizationDto: OptimizationDto) {
    console.log(optimizationDto);

    this.service.optimize(optimizationDto).subscribe(info => {
      this.snackBar.open("Optimization started", "Go to logs", {
        duration: 10000
      }).onAction().subscribe(() => {
        this.stepper.selectedIndex = 5
      })
    })
  }
}
