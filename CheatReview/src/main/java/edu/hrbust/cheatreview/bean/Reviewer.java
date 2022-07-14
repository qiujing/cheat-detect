package edu.hrbust.cheatreview.bean;

public class Reviewer {
    private Integer reviewer_id;
    private String user_name;

    public Reviewer() {
    }

    public Integer getReviewer_id() {
        return reviewer_id;
    }

    public void setReviewer_id(Integer reviewer_id) {
        this.reviewer_id = reviewer_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
