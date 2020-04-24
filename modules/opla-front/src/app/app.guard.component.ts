import {Injectable} from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router} from '@angular/router';
import {Observable} from 'rxjs/Observable';
import {UserService} from "./services/user.service";
import {OptimizationService} from "./services/optimization.service";

@Injectable({
  providedIn: 'root'
})
export class AppGuardComponent implements CanActivate {

  constructor(private router: Router) {
    console.log("guard component")
  }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    if (!OptimizationService.user) {
      this.router.navigate(['/login']);
    }

    return !!OptimizationService.user;
  }
}

