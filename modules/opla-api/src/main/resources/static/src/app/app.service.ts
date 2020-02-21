import {EventEmitter, Injectable} from '@angular/core';
import {catchError, tap} from "rxjs/operators";
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Config} from "./config";
import {OptimizationDto} from "./optimization-dto";
import {OptimizationInfo} from "./optimization-info";

@Injectable({
  providedIn: 'root'
})
export class AppService {

  public static baseUrl = "http://localhost:8080";
  public static optimizationInfo: OptimizationInfo;
  public static onOptimizationInfo: EventEmitter<OptimizationInfo> = new EventEmitter<OptimizationInfo>();

  constructor(private http: HttpClient) {
    if (AppService.isRunning()) {
      AppService.optimizationInfo = JSON.parse(localStorage.getItem("optimizationInfo"));
      this.startEventListener(AppService.optimizationInfo);
    }
  }

  public static isRunning() {
    return localStorage.getItem("optimizationInfo") != null && JSON.parse(localStorage.getItem("optimizationInfo")).status === "RUNNING";
  }

  startEventListener(optimizationInfo: OptimizationInfo) {
    localStorage.setItem("optimizationInfo", JSON.stringify(optimizationInfo));
    if (!!window['EventSource'] && optimizationInfo.status != "COMPLETE") {
      let source = new EventSource(`${AppService.baseUrl}/optimization-info/${optimizationInfo.threadId}`);
      source.addEventListener('message', (e) => {
        if (e.data) {
          localStorage.setItem("optimizationInfo", e.data);
          let json = JSON.parse(e.data);
          AppService.optimizationInfo = Object.assign(new OptimizationInfo(), json);
          AppService.onOptimizationInfo.emit(AppService.optimizationInfo);
          if (json.status === "COMPLETE") {
            source.close();
            e.stopImmediatePropagation();
            e.stopPropagation();
          }
        }
      }, false);

      source.addEventListener('open', function (e) {
        // Connection was opened.
      }, false);

      source.addEventListener('error', function (e) {
        if (e['readyState'] == EventSource.CLOSED) {
          source.close();
          e.stopImmediatePropagation();
          e.stopImmediatePropagation();
          localStorage.clear();
          // Connection was closed.
        }
      }, false);
    } else {
      // Result to xhr polling :(
    }
  }

  createAuthorizationHeader(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json'
    });
  }

  errorHandler(error) {
    return Promise.reject(error.json());
  }

  getConfig(): Observable<Config> {
    return this.http.get<Config>(`${AppService.baseUrl}/config`, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }

  getDto(): Observable<OptimizationDto> {
    return this.http.get<OptimizationDto>(`${AppService.baseUrl}/dto`, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }

  optimize(dto: OptimizationDto): Observable<OptimizationInfo> {
    return this.http.post<OptimizationInfo>(`${AppService.baseUrl}/optimize`, dto, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler), tap(data => {
        this.startEventListener(data);
      }));
  }
}
