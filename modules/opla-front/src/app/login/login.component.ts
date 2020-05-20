import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {UserService} from "../services/user.service";
import {AlertService} from "../services/alert.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  showSpinner: any;

  constructor(public userService: UserService,
              private router: Router,
              private fb: FormBuilder,
              public message: AlertService) {
    this.loginForm = fb.group({
      'loginInput': ['admin@opla.com.br', Validators.compose([Validators.required, Validators.email])],
      'passwordInput': ['opla', Validators.compose([Validators.required])]
    });
    console.log("login comp")

  }

  ngOnInit() {
  }

  login() {
    if (!this.loginForm.valid) {
      this.message.createErrorAlert("Invalid Data", "Fix and try again");
      return;
    }
    this.userService.login(this.loginForm.value.loginInput, this.loginForm.value.passwordInput)
      .subscribe(result => {
        if (result.status != 'WRONG_PASSWORD') {
          this.message.createSuccessAlert("Welcome " + result.user.login, result.status);
          this.userService.setCurrentlyUser(result.user);
          this.router.navigate(['/opla']);
        } else {
          this.message.createErrorAlert("We could not login in" + result.user.login, result.status)
        }
      })
  }
  forgot() {
    if (!this.loginForm.valid) {
      this.message.createErrorAlert("Dados inválidos", "Fix and try again");
      return;
    }
    this.userService.forgot(this.loginForm.value.loginInput, this.loginForm.value.passwordInput)
      .subscribe(result => {
        if (result.status == 'CHANGED_PASSWORD') {
          this.message.createSuccessAlert("Your password was changed and sent to your e-mail", result.status);
        } else {
          this.message.createErrorAlert("Your password is right, try the login", result.status)
        }
      })
  }
}

