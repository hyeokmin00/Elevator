package com.example.elevator.api;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.example.elevator.api.model.ReportList;
import com.example.elevator.api.model.ErrorLift;
import com.example.elevator.api.model.LiftInfo;
import com.example.elevator.ui.main.adapter.LiftRecyAdapter;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;

public class APIController {
    // json 객체 api에 전송
    //todo  response 후 wifi disable - lte 연결이 아닌 wifit 연결이 맞는지 확인 필요



    private ArrayList<LiftInfo> liftInfoArrayList = new ArrayList<>();
    private ArrayList<ErrorLift> errorLiftArrayList = new ArrayList<>();
    private ArrayList<ReportList> reportListArrayList = new ArrayList<>();
    private LiftInterface liftInterface;
    LiftRecyAdapter liftRecyAdapter;


    public void setRetrofitInit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://boas.asuscomm.com:10002/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        liftInterface = retrofit.create(LiftInterface.class);

    }

    public void LiftList(Context context, RecyclerView recycler, View view) {
        Call<ArrayList<LiftInfo>> call = liftInterface.getElevatorAllList();
        call.enqueue(new Callback<ArrayList<LiftInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<LiftInfo>> call, Response<ArrayList<LiftInfo>> response) {
                if (response.isSuccessful()) {

                    ArrayList<LiftInfo> result = response.body();
                    ArrayList<LiftInfo> totalLiftInfo = null;

                    liftRecyAdapter = new LiftRecyAdapter(result, context);


                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    recycler.setLayoutManager(linearLayoutManager);
                    recycler.setAdapter(liftRecyAdapter);
                    // Recycler view item click event 처리

                    Log.d("dataAll", "dataAll : ");
                } else {
                    Log.d("dataAll", 2 + "Error");
                }

            }

            @Override
            public void onFailure(Call<ArrayList<LiftInfo>> call, Throwable t) {
                t.printStackTrace();
                Log.d("retrofit", "ERROR");
            }
        });
    }

    public void UnitLiftInfo(int lift_id) {
        Call<ArrayList<LiftInfo>> call = liftInterface.getElevatorSelectList(lift_id);
        call.enqueue(new Callback<ArrayList<LiftInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<LiftInfo>> call, Response<ArrayList<LiftInfo>> response) {
                if (response.isSuccessful()) {
                    List<LiftInfo> date = (List<LiftInfo>) response.body();
                    Log.d("liftUnitInfo", lift_id + " : " + date);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LiftInfo>> call, Throwable t) {
                t.printStackTrace();
                Log.d("retrofit", "ERROR");
            }
        });
    }

    public void UpdatedLiftList(String date) {
            Call<ArrayList<LiftInfo>> call = liftInterface.UpdatedElevator(date);
            call.enqueue(new Callback<ArrayList<LiftInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<LiftInfo>> call, Response<ArrayList<LiftInfo>> response) {
                if (response.isSuccessful()) {
                    ArrayList<LiftInfo> date = (ArrayList<LiftInfo>) response.body();
                    Log.d("date", "date : " + date);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LiftInfo>> call, Throwable t) {
                t.printStackTrace();
                Log.d("retrofit", "ERROR");
            }
        });
    }

    public void createLift(LiftInfo liftInfo) {
        //  Lift lift = new Lift(10,"중앙도서관 01","정상","충북 충주시 대학로 50");
        Call<ArrayList<LiftInfo>> call = liftInterface.postLiftInfo(liftInfo);
        call.enqueue(new Callback<ArrayList<LiftInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<LiftInfo>> call, Response<ArrayList<LiftInfo>> response) {
                if (response.isSuccessful()) {
                    List<LiftInfo> date = (List<LiftInfo>) response.body();
                    Log.d("retrofit", "res : " + date);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LiftInfo>> call, Throwable t) {
                t.printStackTrace();
                Log.d("retrofit", "ERROR");
            }
        });
    }

    public void ErrorPost(ErrorLift errorLift) {
        //ErrorLift errorLift = new ErrorLift(10,"비정상","120");
        Call<ArrayList<ErrorLift>> call = liftInterface.ErrorPost(errorLift);
        call.enqueue(new Callback<ArrayList<ErrorLift>>() {
            @Override
            public void onResponse(Call<ArrayList<ErrorLift>> call, Response<ArrayList<ErrorLift>> response) {
                if (response.isSuccessful()) {
                    ArrayList<ErrorLift> date = (ArrayList<ErrorLift>) response.body();
                    Log.d("ErrorPost", "res : " + date);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ErrorLift>> call, Throwable t) {
                t.printStackTrace();
                Log.d("retrofit", "ERROR");
            }
        });
    }

    public void WriteRepoLift(ReportList reportList) {
        // ReportList reportList = new ReportList(10,"정상","김엔지니어/노후화된 전선 교체, 손잡이 교체작업");
        //  Call<ReportList> call = liftInterface.SendReport(reportList);
       // ReportList reportList = new ReportList(Integer.parseInt(liftId),  edContent.getText().toString(),tvDate.getText().toString());

        liftInterface.SendReport(reportList).enqueue(new Callback<ReportList>() {
            @Override
            public void onResponse(Call<ReportList> call, Response<ReportList> response) {
                if (!response.isSuccessful()) {

                    Log.d("Test", "APIController - WriteRepoLift - error");
                    //  textViewResult.setText("code: " + response.code());

                }else{
                    ReportList result = response.body();
                    Log.d("Test", "result : " + result.getReport_date());

                }
            }

            @Override
            public void onFailure(Call<ReportList> call, Throwable t) {

            }
        });



       /*
        Call<ReportList> call = liftInterface.SendReport(reportList);
        call.enqueue(new Callback<ReportList>() {
            @Override
            public void onResponse(Call<ReportList> call, Response<ReportList> response) {

                if (!response.isSuccessful()) {

                    Log.d("Test", "APIController - WriteRepoLift - error");
                  //  textViewResult.setText("code: " + response.code());

                }else{
                    ReportList result = response.body();
                    Log.d("Test", "result : " + result.getReport_date());

                }
            }

            @Override
            public void onFailure(Call<ReportList> call, Throwable t) {
                t.printStackTrace();
                Log.d("retrofit", "ERROR");

            }
        });*/
    }
}