import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PersistenceService} from "./persistence.service";

@Injectable({
  providedIn: 'root'
})
export class WocsClassObjectiveFunctionService extends PersistenceService {

  constructor(http: HttpClient) {
    super("wocs-class-objective-function", http);
  }
}
