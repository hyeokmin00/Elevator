package com.example.elevator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorPost {


    @SerializedName("lift_id")
    @Expose
    private int lift_id;

    @SerializedName("lift_status")
    @Expose
    private  String lift_status;

    @SerializedName("lift_error")
    @Expose
    private String lift_error;

    public ErrorPost(int lift_id, String lift_status, String lift_error){
        this.lift_id = lift_id;
        this.lift_status = lift_status;
        this.lift_error = lift_error;
    }

    public int getLift_id(){return lift_id;}

    public String getLift_status(){
        return lift_status;
    }

    public String getLift_error(){
        return lift_error;
    }

    public void setLift_id(int lift_id) {
        this.lift_id = lift_id;
    }
    public void setLift_status(String lift_status) {
        this.lift_status = lift_status;
    }
    public void setLift_error(String lift_error){
        this.lift_error = lift_error;
    }


}
