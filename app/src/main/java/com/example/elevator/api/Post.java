package com.example.elevator.api;

import com.google.gson.annotations.SerializedName;

public class Post {
    private Integer lift_id;
    private String lift_name;
    private String lift_status;
    private String lift_address;

    @SerializedName("body")
    private String bodyValue;

    public Post(Integer lift_id, String lift_name, String lift_status, String lift_address) {
        this.lift_id = lift_id;
        this.lift_name = lift_name;
        this.lift_status = lift_status;
        this.lift_address = lift_address;
    }

    public int getLift_id(){
        return lift_id;
    }

    public String getLift_name(){
        return lift_name;
    }

    public String getLift_status(){
        return lift_status;
    }

    public String getLift_address(){
        return lift_address;
    }


    public void setLift_id(int lift_id) {this.lift_id = lift_id;}

    public void setLift_name(String lift_name) {
        this.lift_name = lift_name;
    }

    public void setLift_status(String lift_status) {
        this.lift_status = lift_status;
    }

    public void setLift_address(String lift_address) {
        this.lift_address = lift_address;
    }


}
