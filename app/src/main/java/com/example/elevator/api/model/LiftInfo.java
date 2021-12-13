package com.example.elevator.api.model;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiftInfo {
    @SerializedName("lift_id")
    @Expose
    private String liftId;
    public String getLiftId() {
        return liftId;
    }
    public void setLiftId(String liftId) {
        this.liftId = liftId;
    }


    @SerializedName("lift_name")
    @Expose
    private String liftName;
    public String getLiftName() {
        return liftName;
    }
    public void setLiftName(String liftName) {
        this.liftName = liftName;
    }


    @SerializedName("lift_status")
    @Expose
    private String liftStatus;
    public String getLiftStatus() {
        return liftStatus;
    }
    public void setLiftStatus(String liftStatus) {
        this.liftStatus = liftStatus;
    }


    @SerializedName("lift_address")
    @Expose
    private String address;
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    @SerializedName("created_at")
    @Expose
    private String created_at;
    public String getCreated_at() {
        return created_at;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


}