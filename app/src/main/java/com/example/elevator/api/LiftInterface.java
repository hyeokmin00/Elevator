package com.example.elevator.api;

import com.example.elevator.api.model.Checkinglist;
import com.example.elevator.api.model.ErrorLift;
import com.example.elevator.api.model.Lift;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LiftInterface {
    @GET("total")// 0
    Call<List<Lift>> getElevatorAllList();

    @POST("total/")  // 1
    Call<List<Lift>> getLiftInfo(
            @Body Lift lift);

    @GET("liftdetail/") // 2
    Call<List<Lift>> getElevatorSelectList(@Query("lift_id/") int lift_id);

    @GET("afterdate/") // 6
    Call<List<Lift>> UpdateElevator (@Query("date") String date);

    @POST("posterr/") // 9
    Call<List<ErrorLift>> ErrorPost(
            @Body ErrorLift errorpost);

    @POST("postrepo/") // 12
    Call<List<Checkinglist>> Checkinglist(
            @Body Checkinglist checkinglist);


}
