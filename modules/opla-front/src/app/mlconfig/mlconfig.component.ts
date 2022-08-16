import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {OptimizationDto} from "../dto/optimization-dto";
import {FormControl, FormGroup} from "@angular/forms";
import {OptimizationService} from "../services/optimization.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MlpconfigComponent} from "./dialogs/mlpconfig/mlpconfig.component";
import {MatDialog} from "@angular/material/dialog";
import {SvmconfigComponent} from "./dialogs/svmconfig/svmconfig.component";
import {MachineLearningModel} from "./mlmodels/MachineLearningModel";
import {MachineLearningAlgorithm} from "./mlmodels/MachineLearningAlgorithm";
import {LmsconfigComponent} from "./dialogs/lmsconfig/lmsconfig.component";
import {KstarconfigComponent} from "./dialogs/kstarconfig/kstarconfig.component";
import {RandomforestconfigComponent} from "./dialogs/randomforestconfig/randomforestconfig.component";
import {RandomtreeconfigComponent} from "./dialogs/randomtreeconfig/randomtreeconfig.component";


@Component({
  selector: 'app-mlconfig',
  templateUrl: './mlconfig.component.html',
  styleUrls: ['./mlconfig.component.css']
})
export class MlconfigComponent implements OnInit {

  @Input() optimizationOptions: any;
  @Input() optimizationDto: OptimizationDto;
  @Input() formGroup: FormGroup;
  @ViewChild('fileInput', {static: false}) fileInput;
  mlAlgorithm: any;
  disabledEdit: boolean = true;

  floatLabelControl = new FormControl('auto');
  // private mlconfig: MachineLearningModelConfig;

  constructor(protected service: OptimizationService, private snackBar: MatSnackBar, public configDialog: MatDialog) { }

  ngOnInit() {

  }

  onFileSelected() {
    this.service.uploadPLA(this.fileInput.nativeElement.files)
      .subscribe(res => {
        OptimizationService.setPLA(res);
        this.snackBar.open("The input PLA and the profiles were updated", null, {
          duration: 2000
        })
      })
  }

  selectMlConfig() {
    this.fileInput.nativeElement.click();
  }

  formatMLName(alg: string) {
    switch (alg){
      case "MLP":
        return "Multilayer Perceptron"
      case "LMS":
        return "Least Median Square"
      case "SVM":
        return "Support-Vector Machine"
      case "KSTAR":
        return "K*"
      case "RANDOM_FOREST":
        return  "Random Forest"
      case "RANDOM_TREE":
        return "Random Tree"
      case "ENSEMBLE":
        return "Ensemble"
      default:
        return alg;
    }
  }

  formatMLNameNum(alg: number) {
    switch (alg){
      case 0:
        return "Multilayer Perceptron"
      case 1:
        return "Least Median Square"
      case 2:
        return "Support-Vector Machine"
      case 3:
        return "K*"
      case 4:
        return  "Random Forest"
      case 5:
        return "Random Tree"
      case 6:
        return "Ensemble"
      default:
        return alg;
    }
  }


  formatCombinationRules(combinationRule: string) {
    switch (combinationRule){
      case "PRODUCT_OF_PROB":
        return "Product of Probabilities"
      case "MAJORITY_VOTE":
        return "Majority Vote"
      case "MIN_PROB":
        return "Minimum Probability"
      case "MAX_PROB":
        return "Maximum Probability"
      case "MEDIAN":
        return "Median"
      default:
        return combinationRule;
    }
  }

  editAlgConfigs(alg: any) {
    let dialogRef;
    const dialogWidth: number = 720;

    switch(alg){
      case "MLP":
        dialogRef = this.configDialog.open(MlpconfigComponent, {minWidth: dialogWidth, data: {optimizationDto: this.optimizationDto}});
        break;
      case "KSTAR":
        dialogRef = this.configDialog.open(KstarconfigComponent, {minWidth: dialogWidth, data: {optimizationDto: this.optimizationDto}});
        break;
      case "SVM":
        dialogRef = this.configDialog.open(SvmconfigComponent, {minWidth: dialogWidth, data: {optimizationDto: this.optimizationDto}});
        break;
      case "LMS":
        dialogRef = this.configDialog.open(LmsconfigComponent, {minWidth: dialogWidth, data: {optimizationDto: this.optimizationDto}});
        break;
      case "RANDOM_FOREST":
        dialogRef = this.configDialog.open(RandomforestconfigComponent, {minWidth: dialogWidth, data: {optimizationDto: this.optimizationDto}});
        break;
      case "RANDOM_TREE":
        dialogRef = this.configDialog.open(RandomtreeconfigComponent, {minWidth: dialogWidth, data: {optimizationDto: this.optimizationDto}});
        break;
      case "ENSEMBLE":
        break;
    }
    // this.testModels();
  }

  testModels() {
    this.optimizationDto.machineLearningModels.push(new MachineLearningModel(MachineLearningAlgorithm.MLP, [30, true, "0.1", "100", "50", "0.1"]))
    this.optimizationDto.machineLearningModels.push(new MachineLearningModel(MachineLearningAlgorithm.RANDOM_TREE, [35, true, "10", "30", "40", "50"]))
    console.log(this.optimizationDto.machineLearningModels);
  }
}
