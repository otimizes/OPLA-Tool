import {EventEmitter, Injectable} from '@angular/core';
import {catchError, tap} from "rxjs/operators";
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Config} from "../dto/config";
import {OptimizationDto} from "../dto/optimization-dto";
import {OptimizationInfo} from "../dto/optimization-info";
import {UserService} from "./user.service";

@Injectable({
  providedIn: 'root'
})
export class OptimizationService {

  public static baseUrl = location.origin.replace("4200", "8080").concat("/api");
  public static optimizationInfo: OptimizationInfo;
  public static onOptimizationInfo: EventEmitter<OptimizationInfo> = new EventEmitter<OptimizationInfo>();
  public static onSelectPLA: EventEmitter<string[]> = new EventEmitter<string[]>();

  constructor(private http: HttpClient) {
    if (OptimizationService.isRunning()) {
      OptimizationService.optimizationInfo = OptimizationService.getOptimizationInfo()
      this.startEventListener(OptimizationService.optimizationInfo);
    }
    if (OptimizationService.getPLA()) {
      OptimizationService.onSelectPLA.emit(OptimizationService.getPLA());
    }
  }

  public static getOptimizationInfo(): OptimizationInfo {
    return JSON.parse(localStorage.getItem("optimizationInfo"));
  }

  public static clearOptimizationInfo() {
    localStorage.removeItem("optimizationInfo");
    OptimizationService.onOptimizationInfo.emit(null);
  }

  public static isRunning() {
    return localStorage.getItem("optimizationInfo") != null && this.getOptimizationInfo().status === "RUNNING";
  }

  public static setPLA(listOfFiles) {
    this.onSelectPLA.emit(listOfFiles);
    localStorage.setItem("pla", JSON.stringify(listOfFiles));
  }

  public static getPLA() {
    return JSON.parse(localStorage.getItem("pla"));
  }

  startEventListener(optimizationInfo: OptimizationInfo) {
    localStorage.setItem("optimizationInfo", JSON.stringify(optimizationInfo));
    if (!!window['EventSource'] && optimizationInfo.status != "COMPLETE") {
      let source = new EventSource(`${OptimizationService.baseUrl}/optimization/optimization-info/${optimizationInfo.threadId}`);
      source.addEventListener('message', (e) => {
        if (e.data) {
          localStorage.setItem("optimizationInfo", e.data);
          let json = JSON.parse(e.data);
          OptimizationService.optimizationInfo = Object.assign(new OptimizationInfo(), json);
          OptimizationService.onOptimizationInfo.emit(OptimizationService.optimizationInfo);
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
          localStorage.removeItem("optimizationInfo");
          // Connection was closed.
        }
      }, false);
    } else {
      // Result to xhr polling :(
    }
  }

  createAuthorizationHeader(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'authorization': UserService.user.token
    });
  }

  errorHandler(error) {
    return Promise.reject(error.json());
  }

  getConfig(): Observable<Config> {
    return this.http.get<Config>(`${OptimizationService.baseUrl}/optimization/config`, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }

  getOptimizationOptions(): Observable<any> {
    return this.http.get<any>(`${OptimizationService.baseUrl}/optimization/optimization-options`, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }

  getDto(): Observable<OptimizationDto> {
    return this.http.get<OptimizationDto>(`${OptimizationService.baseUrl}/optimization/dto`, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }

  download(id): Observable<any> {
    return this.http.get(`${OptimizationService.baseUrl}/optimization/download/${id}`, {responseType: 'arraybuffer'});
  }

  optimize(dto: OptimizationDto): Observable<OptimizationInfo> {
    return this.http.post<OptimizationInfo>(`${OptimizationService.baseUrl}/optimization/optimize`, dto, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler), tap(data => {
        this.startEventListener(data);
      }));
  }

  getFormUrlEncoded(toConvert) {
    const formBody = [];
    for (const property in toConvert) {
      const encodedKey = encodeURIComponent(property);
      const encodedValue = encodeURIComponent(toConvert[property]);
      formBody.push(encodedKey + '=' + encodedValue);
    }
    return formBody.join('&');
  }

  uploadPLA(files: FileList): Observable<any> {

    let formData = new FormData();
    for (let filesKey in files) {
      if (files[filesKey] instanceof Blob) {
        formData.append("file", files[filesKey], files[filesKey]['webkitRelativePath']);
      }
    }

    return this.http.post(`${OptimizationService.baseUrl}/optimization/upload-pla`, formData, {
      headers: new HttpHeaders({'enctype': 'multipart/form-data'})
    });
  }
}
