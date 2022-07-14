import { Injectable } from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {AlertMsg} from './alert-msg';

@Injectable({
  providedIn: 'root'
})
export class AlertService {
  private subject = new Subject<AlertMsg>();

  constructor() { }

  success(message: string) {
    this.subject.next({ type: 'success', text: message });
  }

  error(message: string) {
    this.subject.next({ type: 'error', text: message });
  }

  http_error(message: any) {
    this.subject.next({ type: 'error', text: JSON.stringify(message) });
  }

  getMessage(): Observable<AlertMsg> {
    return this.subject.asObservable();
  }
}
