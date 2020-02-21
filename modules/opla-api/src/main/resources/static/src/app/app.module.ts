import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ExecutionComponent} from './execution/execution.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatButtonModule} from "@angular/material/button";
import {MatHorizontalStepper, MatStepperModule} from "@angular/material/stepper";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {GeneralComponent} from './general/general.component';
import {PatternComponent} from './pattern/pattern.component';
import {ResultsComponent} from './results/results.component';
import {ExperimentsComponent} from './experiments/experiments.component';
import {LogsComponent} from './logs/logs.component';
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatRadioModule} from "@angular/material/radio";
import {MatSelectModule} from "@angular/material/select";
import {MatIconModule} from "@angular/material/icon";
import {MatGridListModule} from "@angular/material/grid-list";
import {MatCardModule} from "@angular/material/card";
import {MatSliderModule} from "@angular/material/slider";
import {MatDividerModule} from "@angular/material/divider";
import {MatTooltipModule} from "@angular/material/tooltip";
import {HttpClientModule} from "@angular/common/http";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MatProgressBarModule} from "@angular/material/progress-bar";
import { DragDropDirectiveDirective } from './drag-drop-directive.directive';
import {FileUploadInputFor, MatFileUploadModule} from "angular-material-fileupload";

@NgModule({
  declarations: [
    AppComponent,
    ExecutionComponent,
    GeneralComponent,
    PatternComponent,
    ResultsComponent,
    ExperimentsComponent,
    LogsComponent,
    DragDropDirectiveDirective,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatStepperModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    MatRadioModule,
    MatSelectModule,
    MatIconModule,
    MatGridListModule,
    MatCardModule,
    MatSliderModule,
    FormsModule,
    MatDividerModule,
    MatTooltipModule,
    HttpClientModule,
    MatSnackBarModule,
    MatProgressBarModule,
    MatFileUploadModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
