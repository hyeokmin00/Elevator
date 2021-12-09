package com.example.elevator;

import com.google.gson.annotations.SerializedName;

public class Checkinglist {
    private int lift_id;
    private String lift_status;
    private String content;

    @SerializedName("body")
    private String bodyValue;


    public Checkinglist(int lift_id, String lift_status, String content){
        this.lift_id = lift_id;
        this.lift_status = lift_status;
        this.content = content;
    }

    public int getLift_id(){
        return lift_id;
    }

    public String getLift_status(){
        return lift_status;
    }

    public String getContent(){
        return content;
    }

    public void setLift_id(int lift_id) {this.lift_id = lift_id;}
    public void setLift_status(String lift_status) {this.lift_status = lift_status;}
    public void setContent(String content) {this.content = content;}
}
