package com.example.elevator;

import com.example.elevator.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LiftApi {
    @GET("total")// 0
    Call<List<Post>> getElevatorAllList();

    @POST("total/")  // 1
    Call<Post> createPost(@Body Post post);

    @GET("liftdetail/:lift_id/") // 2
    Call<List<Post>> getElevatorSelectList(@Query("lift_id") int lift_id);

    @GET("afterdate/:date") // 6
    Call<List<Post>> UpdateElevator (@Query("date") String date);

    @POST("posterr/") // 9
    Call<Post> errorPost(@Body ErrorPost errorPost);

    @POST("postrepo/") // 12
    Call<Post> Checkinglist(@Body Checkinglist checkinglist);
}
