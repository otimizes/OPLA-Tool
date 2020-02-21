import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {OptimizationDto} from "./optimization-dto";
import {MatStepper} from "@angular/material/stepper";
import {AppService} from "./app.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {OptimizationInfo} from "./optimization-info";

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
  optimizationInfo: OptimizationInfo = AppService.getOptimizationInfo();

  constructor(private _formBuilder: FormBuilder, private service: AppService, private snackBar: MatSnackBar, fb: FormBuilder) {
    this.executionFormGroup = fb.group({
      mutation: ['', Validators.compose([Validators.required])],
      mutationProbability: ['', Validators.compose([Validators.required])],
      crossover: new FormControl(),
      crossoverProbability: new FormControl(),
      numberRuns: ['', Validators.compose([Validators.required])],
      maxEvaluations: ['', Validators.compose([Validators.required])],
      populationSize: ['', Validators.compose([Validators.required])],
      archiveSize: new FormControl(),
      maxInteractions: new FormControl(),
      firstInteraction: new FormControl(),
      intervalInteraction: new FormControl(),
      inputArchitecture: ['', Validators.compose([Validators.required])],
      outputDirectory: ['', Validators.compose([Validators.required])]
    });
    this.generalFormGroup = fb.group({
      pathToTemplateModelsDirectory: new FormControl(),
      directoryToSaveModels: new FormControl(),
      pathToProfileConcern: new FormControl(),
      pathToProfile: new FormControl(),
      pathToProfileRelationships: new FormControl(),
      pathToProfilePatterns: new FormControl(),
      smarty: new FormControl(),
      feature: new FormControl(),
      patterns: new FormControl(),
      relationships: new FormControl()
    });
    this.patternFormGroup = fb.group({
    });
    service.getDto().subscribe(dto => {
      this.optimizationDto = dto
    });

    AppService.onOptimizationInfo.asObservable().subscribe(optimizationInfo => {
      this.optimizationInfo = optimizationInfo;
    })
  }

  ngAfterViewInit(): void {
    this.stepper.selectedIndex = 1;
  }

  ngOnInit() {
  }

  isRunning() {
    return AppService.isRunning();
  }

  run(optimizationDto: OptimizationDto) {
    this.service.optimize(optimizationDto).subscribe(info => {
      this.snackBar.open("Optimization started", "Go to logs", {
        duration: 10000
      }).onAction().subscribe(() => {
        this.stepper.selectedIndex = 5
      })
    })
  }

  download(optimizationInfo: OptimizationInfo) {
    this.service.download(optimizationInfo.hash).subscribe(result => {
      this.snackBar.open("Your download started", null, {
        duration: 2000
      });
      const blob = new Blob([result], {
        type: 'application/zip'
      });
      const url = window.URL.createObjectURL(blob);
      window.open(url);
      AppService.clearOptimizationInfo();
    });
  }
}
