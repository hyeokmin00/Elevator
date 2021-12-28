package com.example.elevator.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ErrorResult {

    @SerializedName("lift_id")
    @Expose
    private int lift_id;

    public int getLiftId() {  return lift_id;  }
    public void setLiftId(int lift_id) {
        this.lift_id = lift_id;
    }

    @SerializedName("lift_errors")
    @Expose
    private ArrayList<LiftError> lift_errors;
    public ArrayList<LiftError> getLiftErrors() {
        return lift_errors;
    }
    public void setLiftError(ArrayList<LiftError> lift_errors) {
        this.lift_errors = lift_errors;
    }

    public ErrorResult(int lift_id, ArrayList<LiftError> lift_errors) {
        this.lift_id = lift_id;
        this.lift_errors = lift_errors;
    }
}

