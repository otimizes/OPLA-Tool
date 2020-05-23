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
  public static optimizationInfo: OptimizationInfo;
  public static onOptimizationInfo: EventEmitter<OptimizationInfo> = new EventEmitter<OptimizationInfo>();
  public static onSelectPLA: EventEmitter<string[]> = new EventEmitter<string[]>();
  public static onOptimizationStart: EventEmitter<OptimizationInfo> = new EventEmitter<OptimizationInfo>();
  public static onOptimizationFinish: EventEmitter<OptimizationInfo> = new EventEmitter<OptimizationInfo>();
  public static source: EventSource;
  public static i18nURLs = {
    "en-us": "https://raw.githubusercontent.com/otimizes/OPLA-Tool/master/i18n.en-us.json"
  };
  public static language = 'en-us';
  public static i18n: any = {};

  constructor(private http: HttpClient, private userService: UserService) {
    if (OptimizationService.isRunning()) {
      OptimizationService.optimizationInfo = OptimizationService.getOptimizationInfo();
      this.startEventListener(OptimizationService.optimizationInfo);
    }
    if (OptimizationService.getPLA()) {
      OptimizationService.onSelectPLA.emit(OptimizationService.getPLA());
    }
    for (let url of Object.keys(OptimizationService.i18nURLs)) {
      this.getInternationalization(OptimizationService.i18nURLs[url])
        .subscribe(result => {
          OptimizationService.i18n[url] = result;
        });
    }
  }

  public static getI18n(key) {
    return OptimizationService.i18n[OptimizationService.language][key]
  }

  public static getOptimizationInfo(): OptimizationInfo {
    return JSON.parse(localStorage.getItem("optimizationInfo"));
  }

  public static clearOptimizationInfo() {
    OptimizationService.source.close();
    OptimizationService.optimizationInfo = null;
    localStorage.removeItem("optimizationInfo");
    OptimizationService.onOptimizationInfo.emit(null);
  }

  public static isRunning() {
    return (localStorage.getItem("optimizationInfo") != null && (this.getOptimizationInfo().status === "RUNNING" || this.getOptimizationInfo().status === 'INTERACT'));
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
      OptimizationService.source = new EventSource(`${UserService.baseUrl}/optimization/optimization-info/${optimizationInfo.hash}?authorization=${UserService.user.token}`);
      OptimizationService.source.addEventListener('message', (e) => {
        if (e.data) {
          localStorage.setItem("optimizationInfo", e.data);
          let json = JSON.parse(e.data);
          OptimizationService.optimizationInfo = Object.assign(new OptimizationInfo(), json);
          OptimizationService.onOptimizationInfo.emit(OptimizationService.optimizationInfo);
          if (json.status === "COMPLETE") {
            OptimizationService.onOptimizationFinish.emit(json);
            OptimizationService.source.close();
            e.stopImmediatePropagation();
            e.stopPropagation();
            localStorage.removeItem("optimizationInfo");
          }
        }
      }, false);

      OptimizationService.source.addEventListener('open', function (e) {
        // Connection was opened.
      }, false);

      OptimizationService.source.addEventListener('error', function (e) {
        if (e['readyState'] == EventSource.CLOSED) {
          OptimizationService.source.close();
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
    return this.http.get<Config>(`${UserService.baseUrl}/optimization/config`, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }

  getInternationalization(url): Observable<any> {
    return this.http.get<any>(url)
      .pipe(catchError(this.errorHandler));
  }

  getOptimizationInfos(): Observable<any> {
    return this.http.get<any>(`${UserService.baseUrl}/optimization/optimization-infos`, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }

  killOptimizationProcess(id: any) {
    return this.http.post<any>(`${UserService.baseUrl}/optimization/kill-optimization-process/${id}`, null, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }

  getInteraction(id): Observable<any> {
    return this.http.get<any>(`${UserService.baseUrl}/optimization/interaction/${id}`, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }

  postInteraction(id, solutionSet): Observable<any> {
    return this.http.post<any>(`${UserService.baseUrl}/optimization/interaction/${id}`, solutionSet, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }

  getOptimizationOptions(): Observable<any> {
    return this.http.get<any>(`${UserService.baseUrl}/optimization/optimization-options`, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }

  getDto(): Observable<OptimizationDto> {
    return this.http.get<OptimizationDto>(`${UserService.baseUrl}/optimization/dto`, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }

  download(id): Observable<any> {
    return this.http.get(`${UserService.baseUrl}/optimization/download/${id}?authorization=${UserService.user.token}`, {responseType: 'arraybuffer'});
  }

  downloadOneAlternative(hash, id): Observable<any> {
    return this.http.get(`${UserService.baseUrl}/optimization/download-alternative/${hash}/${id}?authorization=${UserService.user.token}`, {responseType: 'arraybuffer'});
  }

  downloadAllAlternative(hash): Observable<any> {
    return this.http.get(`${UserService.baseUrl}/optimization/download-all-alternative/${hash}?authorization=${UserService.user.token}`, {responseType: 'arraybuffer'});
  }

  openOneAlternative(hash, id): Observable<any> {
    return this.http.get(`${UserService.baseUrl}/optimization/open-alternative/${hash}/${id}?authorization=${UserService.user.token}`);
  }

  openAllAlternative(hash): Observable<any> {
    return this.http.get(`${UserService.baseUrl}/optimization/open-all-alternative/${hash}?authorization=${UserService.user.token}`);
  }

  optimize(dto: OptimizationDto): Observable<OptimizationInfo> {
    return this.http.post<OptimizationInfo>(`${UserService.baseUrl}/optimization/optimize`, dto, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler), tap(data => {
        this.startEventListener(data);
        setTimeout(() => {
          OptimizationService.onOptimizationStart.emit(data);
        }, 2000)
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
    if (files.length === 1) {
      formData.append('file', files.item(0));
    } else {
      for (let filesKey in files) {
        if (files[filesKey] instanceof Blob) {
          formData.append("file", files[filesKey], files[filesKey]['webkitRelativePath']);
        }
      }
    }

    return this.http.post(`${UserService.baseUrl}/optimization/upload-pla`, formData, {
      headers: new HttpHeaders({'enctype': 'multipart/form-data', "authorization": UserService.user.token})
    });
  }
}
