export interface Record {
  record_id: number;
  xml_data: string;
  state: number; // 0 未处理 1 作弊 2 正常代码 3 不确定
  code: string;
  state_new: number;
}
