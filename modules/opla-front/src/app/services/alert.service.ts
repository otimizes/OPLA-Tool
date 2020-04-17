import {Injectable} from '@angular/core';
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class AlertService {
  constructor(public snackBar: MatSnackBar) {
  }

  public createErrorAlert(msg: string, action: string) {
    this.snackBar.open(msg, action, {
      duration: 2000,
      panelClass: 'vic-bg-warn'
    });
  }

  public createSuccessAlert(msg: string, action: string) {
    this.snackBar.open(msg, action, {
      duration: 2000,
    });
  }

  public createWarningAlert(msg: string, action: string) {
    this.snackBar.open(msg, action, {
      duration: 2000,
      panelClass: 'vic-bg-accent'
    });
  }

}
