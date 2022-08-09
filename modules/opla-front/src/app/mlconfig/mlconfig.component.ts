import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {OptimizationDto} from "../dto/optimization-dto";
import {FormControl, FormGroup} from "@angular/forms";
import {OptimizationService} from "../services/optimization.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MachineLearningModelConfig} from "../dto/MachineLearningModelConfig";
import {MlpconfigComponent} from "./dialogs/mlpconfig/mlpconfig.component";
import {MatDialog} from "@angular/material/dialog";
import {SvmconfigComponent} from "./dialogs/svmconfig/svmconfig.component";


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
  models: string[] = ["mlp500.model", "lms.model", "svm.model", "kstar.model", "mlp2500.model", "myensemble.model", "reallybignameforafile_model.model"];
  mlAlgorithm: any;
  mlConfigs: MachineLearningModelConfig[] = [];
  disabledEdit: boolean = true;

  floatLabelControl = new FormControl('auto');
  // private mlconfig: MachineLearningModelConfig;

  constructor(protected service: OptimizationService,private snackBar: MatSnackBar, public configDialog: MatDialog) { }

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
        return "Least Mean Square"
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
        dialogRef = this.configDialog.open(MlpconfigComponent, {minWidth: dialogWidth, data: {}});
        dialogRef.afterClosed().subscribe(result => {
          console.log(result);
          this.mlConfigs.push(result);
          }
        )
        break;
      case "SVM":
        dialogRef = this.configDialog.open(SvmconfigComponent, {data: {}});
        break;
    }
    this.send(this.mlConfigs);
  }

  send(mlConfigs: MachineLearningModelConfig[]) {
    console.log(mlConfigs);
  }
}
