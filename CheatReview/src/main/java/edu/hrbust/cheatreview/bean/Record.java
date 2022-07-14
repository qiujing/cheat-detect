package edu.hrbust.cheatreview.bean;

public class Record {
    private Integer record_id;
    private String xml_data;
    private Integer state;
    private Integer reviewer_id;

    public Record() {
    }

    public Integer getRecord_id() {
        return record_id;
    }

    public void setRecord_id(Integer record_id) {
        this.record_id = record_id;
    }

    public String getXml_data() {
        return xml_data;
    }

    public void setXml_data(String xml_data) {
        this.xml_data = xml_data;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getReviewer_id() {
        return reviewer_id;
    }

    public void setReviewer_id(Integer reviewer_id) {
        this.reviewer_id = reviewer_id;
    }
}
