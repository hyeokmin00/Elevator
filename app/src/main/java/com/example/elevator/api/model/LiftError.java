package com.example.elevator.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LiftError {
    @SerializedName("errCode")
    @Expose
    private int errCode;

    public int getErrCode() {  return errCode;  }
    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    @SerializedName("datetime")
    @Expose
    private String datetime;
    public String getDatetime() {
        return datetime;
    }
    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    //dummy data 저장
    public LiftError(int errCode, String datetime) {
        this.errCode = errCode;
        this.datetime = datetime;
    }

}
