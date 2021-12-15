package com.example.elevator.api.model;

import com.google.gson.annotations.SerializedName;

public class ReportList {
    @SerializedName("lift_id")
    private int lift_id;
    @SerializedName("content")
    private String content;
    @SerializedName("report_date")
    private String report_date;


    //git push 
   public ReportList(int lift_id, String content, String report_date){
        this.lift_id = lift_id;
        this.content = content;
        this.report_date = report_date;
    }

    public int getLift_id(){
        return lift_id;
    }

    public String getContent(){
        return content;
    }
    public String getReport_date(){ return report_date; }

    public void setLift_id(int lift_id) {this.lift_id = lift_id;}
    public void setContent(String content) {this.content = content;}
    public void setReport_date(String report_date) {this.report_date = report_date;}
}
