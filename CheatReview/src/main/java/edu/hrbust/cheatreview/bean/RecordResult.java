package edu.hrbust.cheatreview.bean;

public class RecordResult {
    private Integer total;
    private Record[] data;

    public RecordResult() {
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Record[] getData() {
        return data;
    }

    public void setData(Record[] data) {
        this.data = data;
    }
}
