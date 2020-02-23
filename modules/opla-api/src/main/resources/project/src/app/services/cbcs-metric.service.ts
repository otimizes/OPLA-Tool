import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PersistenceService} from "./persistence.service";

@Injectable({
  providedIn: 'root'
})
export class CbcsMetricService extends PersistenceService {

  constructor(http: HttpClient) {
    super("cbcs-metic", http);
  }
}
