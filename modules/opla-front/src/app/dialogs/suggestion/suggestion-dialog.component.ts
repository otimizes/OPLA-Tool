import {Component, Inject, OnInit} from "@angular/core";
import {FormBuilder} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Location} from "@angular/common";
import {OptimizationService} from "../../services/optimization.service";
import {OptimizationInfo} from "../../dto/optimization-info";
import {getSuggestion} from "codelyzer/util/getSuggestion";
import {LLMService} from "../../services/llm.service";

@Component({
  selector: 'suggestion-dialog',
  templateUrl: 'suggestion-dialog.component.html',
})
export class SuggestionDialogComponent implements OnInit {

  selff: any;
  // suggestion = JSON.parse(`{"fns": ["EC", "EXT", "WOCSCLASS", "DC"],  "suggestion": "LCC (Lack of Feature-based Cohesion) evaluates how closely related the functionalities or features are within a component, aiming to improve modularity by ensuring features are well-grouped. FM (Feature Modularization) focuses on optimizing the distribution of features across the architecture, enhancing the system's scalability and maintainability. ELEG (Elegance) assesses the overall design quality, promoting simplicity, clarity, and minimalism in the architecture to facilitate understanding and modifications."}`);
  suggestion;
  question: any;
  analyzing;

  constructor(
    public dialogRef: MatDialogRef<SuggestionDialogComponent>, public router: Router, public route: ActivatedRoute,
    private fb: FormBuilder, public snackBar: MatSnackBar, public location: Location,
    public llmService: LLMService,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    this.selff = this;
  }


  ngOnInit(): void {
  }

  close(element?) {
    this.dialogRef.close(element)
  }


  getSuggestion(question) {
    this.analyzing = true;
    this.llmService.obj(question).subscribe(result => {
      this.suggestion = JSON.parse(result.content.replace('jsonCopy code', ''));
      this.analyzing = false;
      console.log("result---", result)
    })
  }
}
