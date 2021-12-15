package com.example.elevator.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ErrorResult {

    @SerializedName("lift_id")
    @Expose
    private String lift_id;
    public String getLiftId() {  return lift_id;  }
    public void setLiftId(String lift_id) {
        this.lift_id = lift_id;
    }

    @SerializedName("lift_status")
    @Expose
    private String lift_status;
    public String getLiftStatus() {
        return lift_status;
    }
    public void setLiftStatus(String lift_status) {
        this.lift_status = lift_status;
    }

    @SerializedName("lift_error")
    @Expose
    private String lift_error;
    public String getLiftError() {
        return lift_error;
    }
    public void setLiftError(String lift_error) {
        this.lift_error = lift_error;
    }

}

