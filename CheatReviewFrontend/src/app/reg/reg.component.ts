import {Component, OnInit} from '@angular/core';
import {
  faSignInAlt
} from '@fortawesome/free-solid-svg-icons';
import {first} from 'rxjs/operators';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {AuthService} from '../_service/auth.service';
import {ActivatedRoute, Router} from '@angular/router';
import {AlertService} from '../alert.service';

@Component({
  selector: 'app-reg',
  templateUrl: './reg.component.html',
  styleUrls: ['./reg.component.css']
})
export class RegComponent implements OnInit {
  faSignInAlt = faSignInAlt;
  loading: boolean;
  returnUrl: string;
  userName = new FormControl('', [Validators.required, Validators.maxLength(20)]);

  constructor(private fb: FormBuilder,
              private authService: AuthService,
              private router: Router,
              private route: ActivatedRoute,
              private alertService: AlertService) {
  }

  ngOnInit() {
    this.returnUrl = this.route.snapshot.queryParams.returnUrl;
    if (this.returnUrl === undefined || this.returnUrl.length === 0 || this.returnUrl === '/') {
      this.returnUrl = '/review';
    }
  }

  getErrorMessage() {
    return this.userName.hasError('required') ? 'Please input your name' : '';
  }

  // 提交
  onSubmit() {
    this.loading = true;
    this.authService.reg(this.userName.value)
      .pipe(first()).subscribe(
      data => {
        if (data) {
          this.router.navigate([this.returnUrl]);
        } else {
          this.alertService.error('Register Failed');
        }
        this.loading = false;
      },
      error => {
        this.alertService.http_error(error);
        this.loading = false;
      });
  }
}
