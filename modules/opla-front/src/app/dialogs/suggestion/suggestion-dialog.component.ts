import {Component, Inject, OnInit} from "@angular/core";
import {FormBuilder} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Location} from "@angular/common";
import {LLMService} from "../../services/llm.service";

@Component({
  selector: 'suggestion-dialog',
  templateUrl: 'suggestion-dialog.component.html',
})
export class SuggestionDialogComponent implements OnInit {

  selff: any;
  //suggestion = JSON.parse(`{"fns": ["EC", "EXT", "WOCSCLASS", "DC"],  "suggestion": "LCC (Lack of Feature-based Cohesion) evaluates how closely related the functionalities or features are within a component, aiming to improve modularity by ensuring features are well-grouped. FM (Feature Modularization) focuses on optimizing the distribution of features across the architecture, enhancing the system's scalability and maintainability. ELEG (Elegance) assesses the overall design quality, promoting simplicity, clarity, and minimalism in the architecture to facilitate understanding and modifications."}`);
  suggestion;
  question: any;
  analyzing;
  pla;

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
    if (this.suggestion)
      this.suggestion = null
    else {
      this.analyzing = true;
      this.llmService.suggestion(question).subscribe(result => {
        this.suggestion = result;
        this.analyzing = false;
        console.log("result---", this.suggestion)
      })
    }
  }

  splitCrossover(values: any) {
    if (!values)
      return ''
    return values.map(v => '<div class="crossover-operator mat-chip mat-focus-indicator mat-warn mat-standard-chip mat-chip-selected">' + v.replace(/_/g, ' ') + '</div>').join('\n');
  }

  splitMutation(values: any) {
    if (!values)
      return ''
    return values.map(v => '<div class="mutation-operator mat-chip mat-focus-indicator mat-warn mat-standard-chip mat-chip-selected">' + v.replace(/_/g, ' ') + '</div>').join('\n');
  }

  selectPLA() {
    this.pla = {attributes: 10}
  }

  getFns(values: any) {
    return values.map(v => `<span style="margin: 0 10px; font-size: 12px" class="fns mat-chip mat-focus-indicator mat-primary mat-standard-chip mat-chip-selected"> ${v} </span>`).join('\n');
  }
}
