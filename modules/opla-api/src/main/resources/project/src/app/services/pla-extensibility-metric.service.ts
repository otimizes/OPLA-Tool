import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PersistenceService} from "./persistence.service";

@Injectable({
  providedIn: 'root'
})
export class PlaExtensibilityMetricService extends PersistenceService {

  constructor(http: HttpClient) {
    super("pla-extensibility-metric", http);
  }
}
