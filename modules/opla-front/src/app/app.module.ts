import {BrowserModule} from '@angular/platform-browser';
import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ExecutionComponent, PapyrusSettingsDialog} from './execution/execution.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatButtonModule} from "@angular/material/button";
import {MatStepperModule} from "@angular/material/stepper";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
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
import {ReplaceallPipe} from './pipes/replaceall.pipe';
import {NgVarDirective} from './directives/ng-var.directive';
import {MatBadgeModule} from "@angular/material/badge";
import {LoginComponent} from "./login/login.component";
import {OplaComponent} from "./opla/opla.component";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {InteractionDialogComponent} from "./dialogs/interaction/interaction-dialog.component";
import {MatDialogModule} from "@angular/material/dialog";
import {MatChipsModule} from "@angular/material/chips";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {DialogTooltipInfo, OplaI18nDirective} from "./directives/opla-i18n.directive";
import {MlconfigComponent} from './mlconfig/mlconfig.component';
import { MlpconfigComponent } from './mlconfig/dialogs/mlpconfig/mlpconfig.component';
import {LmsconfigComponent} from "./mlconfig/dialogs/lmsconfig/lmsconfig.component";
import { SvmconfigComponent } from './mlconfig/dialogs/svmconfig/svmconfig.component';
import { RandomtreeconfigComponent } from './mlconfig/dialogs/randomtreeconfig/randomtreeconfig.component';
import { RandomforestconfigComponent } from './mlconfig/dialogs/randomforestconfig/randomforestconfig.component';
import { KstarconfigComponent } from './mlconfig/dialogs/kstarconfig/kstarconfig.component';
import { ModeloptionsComponent } from './mlconfig/modeloptions/modeloptions.component';

@NgModule({
  declarations: [
    AppComponent,
    ExecutionComponent,
    MlconfigComponent,
    PatternComponent,
    ResultsComponent,
    ExperimentsComponent,
    LogsComponent,
    DragDropDirectiveDirective,
    ReplaceallPipe,
    NgVarDirective,
    OplaI18nDirective,
    LoginComponent,
    OplaComponent,
    DialogTooltipInfo,
    InteractionDialogComponent,
    PapyrusSettingsDialog,
    MlpconfigComponent,
    SvmconfigComponent,
    RandomtreeconfigComponent,
    RandomforestconfigComponent,
    KstarconfigComponent,
    LmsconfigComponent,
    ModeloptionsComponent
  ],
  exports: [OplaI18nDirective],
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
    MatProgressSpinnerModule,
    MatDialogModule,
    MatChipsModule,
    MatAutocompleteModule
  ],
  entryComponents: [
    InteractionDialogComponent,
    DialogTooltipInfo,
    PapyrusSettingsDialog,
    MlpconfigComponent,
    SvmconfigComponent,
    LmsconfigComponent,
    KstarconfigComponent,
    RandomtreeconfigComponent,
    RandomforestconfigComponent
  ],
  providers: [],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule {
}
