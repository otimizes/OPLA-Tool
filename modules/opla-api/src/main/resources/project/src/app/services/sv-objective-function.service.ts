import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PersistenceService} from "./persistence.service";

@Injectable({
  providedIn: 'root'
})
export class SvObjectiveFunctionService extends PersistenceService {

  constructor(http: HttpClient) {
    super("sv-objective-function", http);
  }
}
