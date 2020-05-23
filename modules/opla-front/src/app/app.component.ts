import {AfterViewInit, Component, OnInit} from '@angular/core';
import {STEPPER_GLOBAL_OPTIONS} from "@angular/cdk/stepper";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [{
    provide: STEPPER_GLOBAL_OPTIONS, useValue: {displayDefaultIndicatorType: false}
  }]
})
export class AppComponent implements OnInit, AfterViewInit {
  title = 'static';


  constructor() {

  }

  ngAfterViewInit(): void {
  }

  ngOnInit(): void {
  }

}
