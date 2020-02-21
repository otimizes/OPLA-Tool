import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {OptimizationDto} from "../optimization-dto";

@Component({
  selector: 'app-logs',
  templateUrl: './logs.component.html',
  styleUrls: ['./logs.component.css']
})
export class LogsComponent implements OnInit {

  options: FormGroup;
  hideRequiredControl = new FormControl(false);
  floatLabelControl = new FormControl('auto');
  @Input() optimizationDto: OptimizationDto;
  logs: string = "";

  constructor(fb: FormBuilder) {
    this.options = fb.group({
      hideRequired: this.hideRequiredControl,
      floatLabel: this.floatLabelControl,
    });
  }

  ngOnInit() {
    console.log("----", window['EventSource'])
    if (!!window['EventSource']) {
      let source = new EventSource('http://localhost:8080/optimization-info/38');
      source.addEventListener('message', (e) => {
        if (e.data) {
          let json = JSON.parse(e.data);
          if (json.logs != "") {
            this.logs += "\n" + json.logs;
          }
        }
      }, false);

      source.addEventListener('open', function (e) {
        // Connection was opened.
      }, false);

      source.addEventListener('error', function (e) {
        if (e['readyState'] == EventSource.CLOSED) {
          // Connection was closed.
        }
      }, false);
    } else {
      // Result to xhr polling :(
    }
  }
}
