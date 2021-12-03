package com.example.elevator;

import com.example.elevator.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LiftApi {
    @GET("total")// 0
    Call<List<Post>> getElevatorAllList();

    @POST("total/")  // 1
    Call<List<Post>> Post(
            @Body Post post);


    @GET("liftdetail/") // 2
    Call<List<Post>> getElevatorSelectList(@Query("lift_id/") int lift_id);

    @GET("afterdate/") // 6
    Call<List<Post>> UpdateElevator (@Query("date") String date);

    @POST("posterr/") // 9
    Call<List<ErrorPost>> ErrorPost(
            @Body ErrorPost errorpost);

    @POST("postrepo/") // 12
    Call<List<Checkinglist>> Checkinglist(
            @Body Checkinglist checkinglist);


}
