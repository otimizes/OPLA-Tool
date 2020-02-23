import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {OptimizationDto} from "../dto/optimization-dto";
import {PersistenceService} from "../services/persistence.service";

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent implements OnInit {


  @Input() experiments = [];
  columnsToDisplay = ['id', 'name', 'algorithm', 'createdAt', 'description'];
  expandedElement: any | null;

  constructor(fb: FormBuilder, private service: PersistenceService) {

  }

  ngOnInit() {
  }

}
