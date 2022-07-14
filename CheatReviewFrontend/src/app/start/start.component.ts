import {Component, OnInit} from '@angular/core';
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {faListUl, faSearch, faBars, faSignOutAlt, faUserCircle, faCode, faCheckSquare} from '@fortawesome/free-solid-svg-icons';
import {AuthService} from '../_service/auth.service';
import {Reviewer} from '../_interface/reviewer';
import {Router} from '@angular/router';

@Component({
  selector: 'app-start',
  templateUrl: './start.component.html',
  styleUrls: ['./start.component.css']
})
export class StartComponent implements OnInit {
  faListUl = faListUl;
  faSearch = faSearch;
  faBars = faBars;
  faSignOutAlt = faSignOutAlt;
  faUserCircle = faUserCircle;
  faCode = faCode;
  faCheckSquare = faCheckSquare;

  reviewer = {reviewer_id: 0, user_name: null} as Reviewer;

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches)
    );

  constructor(private breakpointObserver: BreakpointObserver,
              private authService: AuthService,
              private router: Router) {
  }

  /**
   * 注销
   */
  onLogout() {
    this.reviewer = {reviewer_id: 0, user_name: null};
    AuthService.logout();
    this.router.navigate(['/reg']);
  }

  ngOnInit(): void {
    this.reviewer = AuthService.getReviewer();
    this.authService.userInfoChanged().subscribe(data => {
      if (data != null) {
        this.reviewer = data;
      }
    });
  }
}
