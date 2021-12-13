package com.example.elevator.api;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.elevator.api.model.ReportList;
import com.example.elevator.api.model.ErrorLift;
import com.example.elevator.api.model.LiftInfo;
import com.example.elevator.api.roomdb.Lift;
import com.example.elevator.api.roomdb.LiftDB;
import com.example.elevator.ui.main.MainActivity;


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


    private ArrayList<Lift> liftInfoArrayList = new ArrayList<>();
    private ArrayList<ErrorLift> errorLiftArrayList = new ArrayList<>();
    private ArrayList<ReportList> reportListArrayList = new ArrayList<>();
    private LiftInterface liftInterface;
    private LiftDB db = null;


    public void setRetrofitInit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://boas.asuscomm.com:10002/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        liftInterface = retrofit.create(LiftInterface.class);

    }

    // 엘레베이터 전체 정보 전송받음
    // todo LiftDao 참고하여 Insert 동작
    public void LiftList(Context context) {
        Call<ArrayList<LiftInfo>> call = liftInterface.getElevatorAllList();

        call.enqueue(new Callback<ArrayList<LiftInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<LiftInfo>> call, Response<ArrayList<LiftInfo>> response) {
                if (response.isSuccessful()) {


                    ArrayList<LiftInfo> result = response.body();

                    for (int i = 0; i < result.size(); i++) {
                        String liftId = result.get(i).getLiftId();
                        String name = result.get(i).getLiftName();
                        String status = result.get(i).getLiftStatus();
                        String addr = result.get(i).getAddress();
                        String createAt = result.get(i).getCreated_at();

                        //todo DB에 저장 insert - thread 에서 처리해야함

                        //DB는 mainThread 에서 접근 불가능함 -> Thread 이용해 접근해야함.
                        class InsertRunnable implements Runnable {
                            @Override
                            public void run() {
                                //todo 값이 중첩되어 저장되는지 확인
                                LiftDB.getInstance(context).liftDao().insert(new Lift(liftId, name, status, addr, createAt));


                                // 값 전체 삭제
                                //  LiftDB.getInstance(context).liftDao().deleteAll();
                            }
                        }
                        InsertRunnable insertRunnable = new InsertRunnable();
                        Thread t = new Thread(insertRunnable);
                        t.start();
                    }
                    context.startActivity(new Intent(context, MainActivity.class));


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
        liftInterface.SendReport(reportList).enqueue(new Callback<ReportList>() {
            @Override
            public void onResponse(Call<ReportList> call, Response<ReportList> response) {
                if (!response.isSuccessful()) {
                    Log.d("Test", "APIController - WriteRepoLift - error");
                    //  textViewResult.setText("code: " + response.code());
                } else {
                    ReportList result = response.body();
                    Log.d("Test", "result : " + result.getReport_date());
                }
            }

            @Override
            public void onFailure(Call<ReportList> call, Throwable t) {
            }
        });

    }
}