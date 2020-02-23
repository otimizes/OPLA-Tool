import {Component, Input, OnInit} from '@angular/core';
import {PersistenceService} from "../services/persistence.service";
import {animate, state, style, transition, trigger} from "@angular/animations";

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

  constructor(private service: PersistenceService) {

  }

  ngOnInit() {

  }

}
