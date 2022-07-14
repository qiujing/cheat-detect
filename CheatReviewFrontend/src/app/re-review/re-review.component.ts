import {AfterViewInit, Component, EventEmitter, OnInit, ViewChild} from '@angular/core';
import {MatDialog, MatPaginator} from '@angular/material';
import {RecordService} from '../_service/record.service';
import {Record} from '../_interface/record';
import {merge, of} from 'rxjs';
import {catchError, map, startWith, switchMap} from 'rxjs/operators';
import {AlertService} from '../alert.service';
import {QContentDlgComponent} from '../qcontent-dlg/qcontent-dlg.component';

interface ReviewState {
  value: number;
  label: string;
}

@Component({
  selector: 'app-re-review',
  templateUrl: './re-review.component.html',
  styleUrls: ['./re-review.component.css']
})
export class ReReviewComponent implements OnInit, AfterViewInit {
  state = RecordService.STATE_NORMAL;
  resultsLength = 0;
  isLoadingResults = true;
  data: Record[] = [];
  @ViewChild(MatPaginator) paginator: MatPaginator;
  states: ReviewState[] = [
    {value: 0, label: 'Unprocessed'},
    {value: 1, label: 'Cheat'},
    {value: 2, label: 'Normal'},
    {value: 3, label: 'Not Sure'}];
  refreshData: EventEmitter<boolean> = new EventEmitter<boolean>(true);
  recordId = 0;

  constructor(private recordService: RecordService,
              private alertService: AlertService,
              public dialog: MatDialog) {
  }

  ngOnInit() {
  }

  ngAfterViewInit(): void {
    merge(this.paginator.page, this.refreshData)
      .pipe(
        startWith({}),
        switchMap(() => {
          this.isLoadingResults = true;
          return this.recordService.loadResult(this.state, this.paginator.pageIndex, this.paginator.pageSize, this.recordId);
        }),
        map(data => {
          // Flip flag to show that loading has finished.
          this.isLoadingResults = false;
          this.resultsLength = data.total;
          return data.data;
        }),
        catchError(() => {
          this.isLoadingResults = false;
          return of([]);
        })
      ).subscribe(data => this.data = data);
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

  change() {
    this.paginator.pageIndex = 0;
    this.refreshData.emit(true);
  }

  setResult(record: Record, b: number) {
    record.state = b;
    this.recordService.setResult([record], 0).subscribe(data => {
      // this.alertService.success('操作成功');
    }, error => {
      this.alertService.error('操作失败');
    });
  }

  getQContent(recordId: number) {
    this.recordService.getQContentByRecordId(recordId).subscribe(d => {
      this.dialog.open(QContentDlgComponent, {
        data: d.data
      });
    });
  }

  getMContent(recordId: number) {
    this.recordService.getMContentByRecordId(recordId).subscribe(d => {
      this.dialog.open(QContentDlgComponent, {
        data: d.data
      });
    });
  }
}
