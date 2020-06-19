import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {catchError} from "rxjs/operators";
import {UserService} from "./user.service";

export class PersistenceService {

  protected collection = '';
  protected http: HttpClient;

  constructor(collection: string, http: HttpClient) {
    this.collection = collection;
    this.http = http;
  }

  errorHandler(error) {
    return Promise.reject(error && error.json ? error.json() : error);
  }

  createAuthorizationHeader(): HttpHeaders {
    console.log("Auth ---", UserService.user);
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'authorization': UserService.user.token
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


}
