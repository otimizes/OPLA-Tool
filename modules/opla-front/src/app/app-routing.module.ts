import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {AppGuardComponent} from "./app.guard.component";
import {AppComponent} from "./app.component";
import {OplaComponent} from "./opla/opla.component";


const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'opla', component: OplaComponent, canActivate: [AppGuardComponent]},
  { path: '', redirectTo: '/opla', pathMatch: 'full' },
  { path: '**', component: OplaComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
  constructor() {
    console.log("router module")
  }
}
