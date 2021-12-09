package com.example.elevator.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LiftResult {

    @SerializedName("")
    @Expose
    private ArrayList<LiftInfo> liftInfo;

    public ArrayList<LiftInfo> getTotalLiftInfo() {
        return liftInfo;
    }

    public void setTotalLiftInfo(ArrayList<LiftInfo> liftInfo) {
        this.liftInfo = liftInfo;
    }
}
