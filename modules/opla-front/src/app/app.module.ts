import {BrowserModule} from '@angular/platform-browser';
import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ExecutionComponent} from './execution/execution.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatButtonModule} from "@angular/material/button";
import {MatStepperModule} from "@angular/material/stepper";
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
import {DragDropDirectiveDirective} from './directives/drag-drop-directive.directive';
import {MatTableModule} from "@angular/material/table";
import {CommonModule} from "@angular/common";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatListModule} from "@angular/material/list";
import { ReplaceallPipe } from './pipes/replaceall.pipe';
import { NgVarDirective } from './directives/ng-var.directive';
import {MatBadgeModule} from "@angular/material/badge";
import {LoginComponent} from "./login/login.component";
import {OplaComponent} from "./opla/opla.component";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";

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
    ReplaceallPipe,
    NgVarDirective,
    LoginComponent,
    OplaComponent
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
    MatTableModule,
    CommonModule,
    MatExpansionModule,
    MatListModule,
    MatBadgeModule,
    AppRoutingModule,
    MatProgressSpinnerModule
  ],
  providers: [],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule {
}
