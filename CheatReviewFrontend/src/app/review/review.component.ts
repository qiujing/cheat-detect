import {Component, OnInit} from '@angular/core';
import {RecordService} from '../_service/record.service';
import {AlertService} from '../alert.service';
import {AuthService} from '../_service/auth.service';
import {Record} from '../_interface/record';
import {faSave, faQuestion, faTimes, faCheck} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.css']
})
export class ReviewComponent implements OnInit {
  code = '';
  isLoadingResults = false;
  records: Record[] = null;
  limit = 10;
  faSave = faSave;
  faQuestion = faQuestion;
  faTimes = faTimes;
  faCheck = faCheck;

  constructor(private recordService: RecordService,
              private alertService: AlertService) {
  }

  ngOnInit() {
    this.isLoadingResults = true;
    this.recordService.getUnprocessedRecord(AuthService.getReviewer().reviewer_id, this.limit).subscribe(data => {
        if (data) {
          this.records = data;
          for (const record of this.records) {
            record.code = this.getCodeFromXml(record.xml_data);
            record.state_new = RecordService.STATE_NORMAL;
          }
        }
        this.isLoadingResults = false;
      },
      error1 => {
        this.alertService.http_error(error1);
        this.isLoadingResults = false;
      });
  }

  /**
   * 从xml中提取代码
   * @param xml 原始记录
   */
  getCodeFromXml(xml: string): string {
    const start = xml.indexOf('</q><v>');
    if (start !== -1) {
      const end = xml.indexOf('</v></d>');
      if (end !== -1) {
        const start2 = '</q><v>'.length + start;
        return decodeURIComponent(xml.substr(start2, end - start2)).trim();
      }
    }
    return '';
  }

  /**
   * 设置结果
   * @param record 记录
   * @param state 是否作弊
   */
  setResult(record: Record, state: number) {
    if (record) {
      record.state_new = state;
    } else {
      for (const r of this.records) {
        r.state_new = state;
      }
    }
  }

  /**
   * 重置
   */
  reset() {
    for (const r of this.records) {
      r.state_new = RecordService.STATE_NORMAL;
    }
  }

  /**
   * 提交
   */
  submit() {
    this.isLoadingResults = true;
    for (const r of this.records) {
      r.state = r.state_new;
    }
    this.recordService.setResult(this.records, this.limit).subscribe(data => {
      if (data) {
        this.records = data;
        for (const record of this.records) {
          record.code = this.getCodeFromXml(record.xml_data);
          record.state_new = RecordService.STATE_NORMAL;
        }
      }
      this.isLoadingResults = false;
    }, error1 => {
      this.alertService.http_error(error1);
      this.isLoadingResults = false;
    });
  }

  countState(state: number) {
    return this.records.filter(r => r.state_new === state).length.toString();
  }

  countCheat() {
    return this.countState(RecordService.STATE_CHEAT);
  }

  countNormal() {
    return this.countState(RecordService.STATE_NORMAL);
  }

  countNotSure() {
    return this.countState(RecordService.STATE_NOT_SURE);
  }
}
