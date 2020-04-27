import {Component, Inject, OnInit} from "@angular/core";
import {FormBuilder, FormGroup} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Location} from "@angular/common";
import {OptimizationService} from "../../services/optimization.service";
import {OptimizationInfo} from "../../dto/optimization-info";

@Component({
  selector: 'interaction-dialog',
  templateUrl: 'interaction-dialog.component.html',
})
export class InteractionDialogComponent implements OnInit {
  solutionSet: any = {};
  solutions = [];
  info:OptimizationInfo;
  clusters: number[] = [];
  selff: any;

  constructor(
    public dialogRef: MatDialogRef<InteractionDialogComponent>, public router: Router, public route: ActivatedRoute,
    private fb: FormBuilder, public snackBar: MatSnackBar, public location: Location,
    public optimizationService: OptimizationService,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    this.selff = this;
  }


  ngOnInit(): void {
    console.log("----", this.data);
    this.solutionSet = this.data.interaction.solutionSet;
    this.solutions = this.solutionSet.solutionSet;
    this.info = this.data.info;
    for (let obj of Object.keys(this.solutionSet.clusterIds)) {
      this.clusters.push(Math.round(Number(obj)));
    }
  }

  filter(clusterId) {
    return this.solutions.filter(solution => solution.clusterId === clusterId);
  }

  close(element: any) {
    this.dialogRef.close(element)
  }

  download(id?) {
    this.optimizationService.downloadAlternative(this.info.threadId, id).subscribe(result => {
      console.log("Download", result)
    })
  }

  open(id?) {
    this.optimizationService.openAlternative(this.info.threadId, id).subscribe(result => {
      console.log("Open", result)
    })
  }

  getNumber(value: any) {
    return Number(value);
  }
}
