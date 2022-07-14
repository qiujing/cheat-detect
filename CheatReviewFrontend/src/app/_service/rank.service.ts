import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {RankItem} from '../_interface/rank-item';

@Injectable({
  providedIn: 'root'
})
export class RankService {

  constructor(private http: HttpClient) {
  }

  getRankList() {
    return this.http.get<RankItem[]>('/api/get-rank-list');
  }
}
