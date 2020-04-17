import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {STEPPER_GLOBAL_OPTIONS} from "@angular/cdk/stepper";
import {OptimizationDto} from "../dto/optimization-dto";
import {OptimizationInfo} from "../dto/optimization-info";
import {OptimizationService} from "../services/optimization.service";
import {MatStepper} from "@angular/material/stepper";
import {ExperimentService} from "../services/experiment.service";
import {InfoService} from "../services/info.service";
import {ObjectiveService} from "../services/objective.service";
import {ExperimentConfigurationService} from "../services/experiment-configuration.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {UserService} from "../services/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'opla',
  templateUrl: './opla.component.html',
  styleUrls: ['./opla.component.css'],
  providers: [{
    provide: STEPPER_GLOBAL_OPTIONS, useValue: {displayDefaultIndicatorType: false}
  }]
})
export class OplaComponent implements OnInit, AfterViewInit {
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
  optimizationInfo: OptimizationInfo = OptimizationService.getOptimizationInfo();
  optimizationOptions: any;
  experiments: any;

  constructor(private _formBuilder: FormBuilder,
              public optimizationService: OptimizationService,
              public userService: UserService,
              private router: Router,
              public experimentService: ExperimentService,
              public infoService: InfoService,
              public objectiveService: ObjectiveService,
              public experimentConfigurationService: ExperimentConfigurationService,
              private snackBar: MatSnackBar, fb: FormBuilder) {
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
    this.patternFormGroup = fb.group({});
    optimizationService.getDto().subscribe(dto => {
      this.optimizationDto = dto
    });

    this.optimizationService.getOptimizationOptions().subscribe(options => {
      this.optimizationOptions = options;
    });

    OptimizationService.onOptimizationInfo.asObservable().subscribe(optimizationInfo => {
      this.optimizationInfo = optimizationInfo;
    });

    this.experimentService.getAll().subscribe(experiments => {
      this.experiments = experiments.values;
      // for (let experiment of this.experiments) {
      // this.persistenceService.getExecutionsByExperiment(experiment.id).subscribe(executions => {
      //   experiment.executions = executions;
      // })
      // }
    })
  }

  ngAfterViewInit(): void {
    this.stepper.selectedIndex = 0;
  }

  ngOnInit() {
  }

  isRunning() {
    return OptimizationService.isRunning();
  }

  run(optimizationDto: OptimizationDto) {
    this.optimizationService.optimize(optimizationDto).subscribe(info => {
      this.snackBar.open("Optimization started", "Go to logs", {
        duration: 10000
      }).onAction().subscribe(() => {
        this.stepper.selectedIndex = 5
      })
    })
  }

  download(hash: string) {
    this.optimizationService.download(hash).subscribe(result => {
      this.snackBar.open("Your download is available", null, {
        duration: 2000
      });
      const blob = new Blob([result], {
        type: 'application/zip'
      });
      const url = window.URL.createObjectURL(blob);
      window.open(url);
      OptimizationService.clearOptimizationInfo();
    });
  }

  logout() {
    this.userService.logout();
    this.router.navigate(['/login']);
  }
}
