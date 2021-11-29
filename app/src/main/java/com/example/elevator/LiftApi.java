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
    Call<List<Post>> createPost(
            @Field("lift_id") int lift_id,
            @Field("lift_name") String lift_name,
            @Field("lift_status") String lift_status,
            @Field("lift_address") String lift_address);

    @GET("liftdetail/") // 2
    Call<List<Post>> getElevatorSelectList(@Query(":lift_id/") int lift_id);

    @GET("afterdate/") // 6
    Call<List<Post>> UpdateElevator (@Query(":date") String date);

    @POST("posterr/") // 9
    Call<List<ErrorPost>> ErrorPost(
            @Field("lift_id") int life_id,
            @Field("lift_status") String lift_status,
            @Field("lift_error") String lift_error);

    @POST("postrepo/") // 12
    Call<List<Checkinglist>> Checkinglist(
            @Field("life_id") int life_id,
            @Field("lift_status") String  lift_status,
            @Field("content") String content);


}
