import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PersistenceService} from "./persistence.service";
import {Observable} from "rxjs";
import {UserService} from "./user.service";
import {catchError} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class LLMService extends PersistenceService {

  constructor(http: HttpClient) {
    super("llm", http);
  }


  obj(q): Observable<any> {
    return this.http.get(`${UserService.baseUrl}/${this.collection}/obj?q=` + q, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }

  suggestion(q): Observable<any> {
    return this.http.get(`${UserService.baseUrl}/${this.collection}/suggestion?q=` + q, {headers: this.createAuthorizationHeader()})
      .pipe(catchError(this.errorHandler));
  }
}
