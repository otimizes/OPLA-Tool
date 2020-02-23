import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {OptimizationDto} from "../dto/optimization-dto";
import {catchError} from "rxjs/operators";
import {OptimizationService} from "./optimization.service";

@Injectable({
  providedIn: 'root'
})
export class PersistenceService {

  constructor(private http: HttpClient) {
  }

  errorHandler(error) {
    return Promise.reject(error.json());
  }

  createAuthorizationHeader(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json'
    });
  }

  getExperiments(): Observable<any> {
    return this.http.get<any>(`${OptimizationService.baseUrl}/persistence/experiments`, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }

  getExecutionsByExperiment(id: number): Observable<any> {
    return this.http.get<any>(`${OptimizationService.baseUrl}/persistence/executions-by-experiment/${id}`, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }
  getNamesByExperiment(id: number): Observable<any> {
    return this.http.get<any>(`${OptimizationService.baseUrl}/persistence/names-by-experiment/${id}`, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }
  getNonDominatedSolutionNumberByExperiment(id: number): Observable<any> {
    return this.http.get<any>(`${OptimizationService.baseUrl}/persistence/non-dominated-Solution-number-by-experiment/${id}`, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }
  getNameSolutionsByExpExec(expId: number, execId: number,solutionName:string): Observable<any> {
    return this.http.get<any>(`${OptimizationService.baseUrl}/persistence/objective-alues/${expId}/${execId}/${solutionName}`, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }
}
