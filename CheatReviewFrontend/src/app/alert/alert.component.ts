import {Component, OnInit} from '@angular/core';
import {NotificationsService} from 'angular2-notifications';
import {AlertMsg} from '../alert-msg';
import {AlertService} from '../alert.service';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.css']
})
export class AlertComponent implements OnInit {
  message: AlertMsg;

  constructor(private alertService: AlertService,
              private notificationsService: NotificationsService) {
  }

  ngOnInit() {
    this.alertService.getMessage().subscribe(
      data => {
        // this.message = data;
        switch (data.type) {
          case 'success':
            const toast = this.notificationsService.success('', data.text, {
              timeOut: 5000,
              showProgressBar: true,
              pauseOnHover: true,
              clickToClose: false,
              clickIconToClose: true,
              animate: 'fromRight'
            });
            break;
          case 'error':
            this.notificationsService.error('', data.text, {
              timeOut: 5000,
              showProgressBar: true,
              pauseOnHover: true,
              clickToClose: false,
              clickIconToClose: true,
              animate: 'fromRight'
            });
            break;
        }
      }
    );
  }

}
