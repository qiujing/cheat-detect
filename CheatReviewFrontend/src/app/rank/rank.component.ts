import {Component, OnInit} from '@angular/core';
import {RankItem} from '../_interface/rank-item';
import {RankService} from '../_service/rank.service';
import {AlertService} from '../alert.service';

@Component({
  selector: 'app-rank',
  templateUrl: './rank.component.html',
  styleUrls: ['./rank.component.css']
})
export class RankComponent implements OnInit {
  dataSource: RankItem[] = [];
  displayedColumns: string[] = ['id', 'user_name', 'number'];
  isLoadingResults = false;

  constructor(private rankService: RankService,
              private alertService: AlertService) {
  }

  ngOnInit() {
    this.isLoadingResults = true;
    this.rankService.getRankList().subscribe(data => {
        this.dataSource = data;
        this.isLoadingResults = false;
      },
      error1 => {
        this.alertService.http_error(error1);
        this.isLoadingResults = false;
      });
  }

}
