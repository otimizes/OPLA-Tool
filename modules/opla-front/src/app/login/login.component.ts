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

  //logar(form) {
  logar() {

    if (!this.loginForm.valid) {
      this.message.createErrorAlert("Dados inválidos", "Complete as informações e tente novamente");
      return;
    }

    this.userService.login(this.loginForm.value.loginInput, this.loginForm.value.passwordInput)
      .subscribe(result => {
        if (result.status != 'WRONG_PASSWORD') {
          this.message.createSuccessAlert("Welcome " + result.user.login, result.status);
          this.userService.setCurrentlyUser(result.user);
          this.router.navigate(['/opla']);
        } else {
          this.message.createErrorAlert("Wops... I'm sorry " + result.user.login, result.status)
        }
      })
  }
}

