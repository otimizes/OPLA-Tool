import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder} from "@angular/forms";

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent implements OnInit {


  @Input() experiments = [];
  columnsToDisplay = ['id', 'name', 'algorithm', 'createdAt', 'description'];
  expandedElement: any | null;

  constructor(fb: FormBuilder) {

  }

  ngOnInit() {
  }

}
