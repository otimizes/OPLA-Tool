import {Component, Input, OnInit} from '@angular/core';
import {animate, state, style, transition, trigger} from "@angular/animations";
import {OptimizationService} from "../services/optimization.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-experiments',
  templateUrl: './experiments.component.html',
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
  styleUrls: ['./experiments.component.css']
})
export class ExperimentsComponent implements OnInit {

  @Input() experiments = [];
  columnsToDisplay = ['id', 'name', 'algorithm', 'createdAt', 'description'];
  expandedElement: any | null;

  constructor(private optimizationService: OptimizationService, private snackBar: MatSnackBar) {

  }

  ngOnInit() {

  }

  download(hash) {
    this.optimizationService.download(hash).subscribe(result => {
      this.snackBar.open("Your download is available", null, {
        duration: 2000
      });
      const blob = new Blob([result], {
        type: 'application/zip'
      });
      const url = window.URL.createObjectURL(blob);
      window.open(url);
    });
  }

}
