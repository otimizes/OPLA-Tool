import {Injectable} from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router} from '@angular/router';
import {Observable} from 'rxjs';
import {UserService} from "./services/user.service";

@Injectable({
  providedIn: 'root'
})
export class AppGuardComponent implements CanActivate {

  constructor(public userService: UserService, private router: Router) {
    console.log("guard component")
  }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    if (!UserService.user) {
      this.router.navigate(['/login']);
    }

    return !!UserService.user;
  }
}

