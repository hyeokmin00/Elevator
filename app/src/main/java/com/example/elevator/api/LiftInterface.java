package com.example.elevator.api;

import com.example.elevator.api.model.ErrorResult;
import com.example.elevator.api.model.LiftResult;
import com.example.elevator.api.model.ReportList;
import com.example.elevator.api.model.LiftError;
import com.example.elevator.api.model.LiftInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LiftInterface {
    @GET("total")// 0
    Call<ArrayList<LiftInfo>> getElevatorAllList();

    @POST("total/")  // 1
    Call<ArrayList<LiftInfo>> postLiftInfo(
            @Body LiftInfo liftInfo);

    @GET("liftdetail/") // 2
    Call<ArrayList<LiftInfo>> getElevatorSelectList(@Query("lift_id/") int lift_id);

    @GET("afterdate/") // 6
    Call<LiftResult> UpdatedElevator (@Query("date") String date);

    //응답값이 code, message 와 같은 기본 값 외에 body 없으므로 call Void로 설정해줌
    @POST("posterr/") // 9
    Call<Void> ErrorPost(
            @Body ErrorResult errorResult);

    @POST("postrepo/") // 12
    Call <ReportList> SendReport(@Body ReportList reportList);
           // @Body ReportList reportList);

}