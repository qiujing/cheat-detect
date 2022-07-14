import {Injectable} from '@angular/core';
import {Reviewer} from '../_interface/reviewer';
import {Observable, of, Subject} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {
  }

  /**
   * 是否已登录
   */
  get isLoggedIn(): Observable<boolean> {
    const r = AuthService.getReviewer();
    return r.reviewer_id === 0 ? of(false) : of(true);
  }

  static USER_KEY = 'currentUser';
  reviewer = new Subject<Reviewer>();

  /**
   * 获取已注册审核人
   */
  public static getReviewer(): Reviewer {
    const r = JSON.parse(localStorage.getItem(AuthService.USER_KEY));
    if (r) {
      return r;
    } else {
      return {reviewer_id: 0, user_name: null};
    }
  }

  /**
   * 注销
   */
  static logout() {
    localStorage.removeItem(AuthService.USER_KEY);
  }

  /**
   * 保存审核人信息
   * @param r 审核人
   */
  private setReviewer(r: Reviewer) {
    this.reviewer.next(r);
    localStorage.setItem(AuthService.USER_KEY, JSON.stringify(r));
  }

  /**
   * 注册
   * @param userName 姓名
   */
  reg(userName: string) {
    return this.http.post<Reviewer>('/api/reg', {reviewer_id: 0, user_name: userName} as Reviewer).pipe(map(r => {
      if (r) {
        this.setReviewer(r);
      }
      return r;
    }));
  }

  /**
   * 用户信息已改变
   */
  userInfoChanged() {
    return this.reviewer.asObservable();
  }
}
