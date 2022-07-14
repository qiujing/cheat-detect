import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Record} from '../_interface/record';
import {RecordResult} from '../_interface/record-result';
import {StringResult} from '../_interface/string-result';

@Injectable({
  providedIn: 'root'
})
export class RecordService {
  public static STATE_UNPROCESSED = 0;
  public static STATE_CHEAT = 1;
  public static STATE_NORMAL = 2;
  public static STATE_NOT_SURE = 3;

  constructor(private http: HttpClient) {
  }

  /**
   * 获取未处理的记录
   */
  getUnprocessedRecord(reviewerId: number, limit: number) {
    return this.http.get<Record[]>('/api/get-unprocessed-record?reviewer_id=' + reviewerId + '&limit=' + limit);
  }

  /**
   * 标记结果
   * @param record 结果
   * @param limit 返回的未审核代码数量
   */
  setResult(record: Record[], limit: number) {
    return this.http.post<Record[]>('/api/set-result?limit=' + limit, record);
  }

  /**
   * 加载结果
   * @param state 是否作弊
   * @param page 页码
   * @param pageSize 页大小
   * @param recordId 记录id
   */
  loadResult(state: number, page: number, pageSize: number, recordId: number) {
    return this.http.get<RecordResult>(`/api/load-result?state=${state}&page=${page}&pageSize=${pageSize}&recordId=${recordId}`);
  }

  /**
   * 加载题目
   * @param recordId 记录id
   */
  getQContentByRecordId(recordId: number) {
    return this.http.get<StringResult>('/api/get-q-content?record_id=' + recordId);
  }

  /**
   * 加载题目参考答案
   * @param recordId 记录id
   */
  getMContentByRecordId(recordId: number) {
    return this.http.get<StringResult>('/api/get-m-content?record_id=' + recordId);
  }
}
