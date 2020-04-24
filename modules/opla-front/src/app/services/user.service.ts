import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {catchError} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  public static baseUrl = location.origin.replace("4200", "8080").concat("/api");
  public static user = null;
  protected collection = 'user';
  constructor(public http: HttpClient) {
    UserService.user = this.getCurrentlyUser();
  }

  errorHandler(error) {
    return Promise.reject(error && error.json ? error.json() : error);
  }

  createAuthorizationHeader(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json'
    });
  }

  post(obj): Observable<any> {
    return this.http.post(`${UserService.baseUrl}/${this.collection}/async`, obj, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }

  put(obj): Observable<any> {
    return this.http.post(`${UserService.baseUrl}/${this.collection}/async`, obj, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }

  delete(id): Observable<any> {
    return this.http.delete(`${UserService.baseUrl}/${this.collection}/${id}`, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }

  get(id): Observable<any> {
    return this.http.get(`${UserService.baseUrl}/${this.collection}/${id}`, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }

  getAll(): Observable<any> {
    return this.http.get(`${UserService.baseUrl}/${this.collection}`, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }

  count(): Observable<any> {
    return this.http.get(`${UserService.baseUrl}/${this.collection}/count`, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }


  findByExperiment(experimentId): Observable<any> {
    return this.http.get(`${UserService.baseUrl}/${this.collection}/by-experiment/${experimentId}`, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }

  login(loginInput: any, passwordInput: any): Observable<any> {
    return this.http.post(`${UserService.baseUrl}/${this.collection}/login`,
      {login: loginInput, password: passwordInput}, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }

  setCurrentlyUser(user: any) {
    UserService.user = user;
    window.localStorage.setItem("user", JSON.stringify(user));
  }

  getCurrentlyUser() {
    let user = window.localStorage.getItem("user");
    return user != null ? JSON.parse(user) : null;
  }

  logout() {
    sessionStorage.clear();
    localStorage.clear();
  }
}
