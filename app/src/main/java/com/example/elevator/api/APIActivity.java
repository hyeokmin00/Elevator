package com.example.elevator.api;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.elevator.R;
import com.example.elevator.api.model.Checkinglist;
import com.example.elevator.api.model.ErrorLift;
import com.example.elevator.api.model.Lift;


import java.lang.reflect.Array;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.ArrayList;

public class APIActivity {
//public class APIActivity extends AppCompatActivity {
    private ArrayList<Lift> liftArrayList = new ArrayList<>();
    private ArrayList<ErrorLift> errorLiftArrayList = new ArrayList<>();
    private ArrayList<Checkinglist> checkinglistArrayList = new ArrayList<>();
    private LiftInterface liftInterface;
  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRetrofitInit(); // 서버 생성
        callList0(); // 전체 파일 받아오기
        callList1(); //  개별 승강기 정보 조회
        callList2(); // 오늘 날짜 이후로 업데이트 된 승강기 목록
        sendList0(); // 승강기 추가
        sendList1(); // 오류 코드 전송
        sendList2(); // 점검사항 등록
// commAPI
        Log.d("Test","commmit3");
        Log.d("Test","commmit3");
    }*/

    public void setRetrofitInit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://boas.asuscomm.com:10002/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        liftInterface = retrofit.create(LiftInterface.class);

    }

    public void LiftList(){
        Call<List<Lift>> call = liftInterface.getElevatorAllList();
        call.enqueue(new Callback<List<Lift>>() {
            @Override
            public void onResponse(Call<List<Lift>> call, Response<List<Lift>> response) {
                if(response.isSuccessful()){
                    ArrayList<Lift> date = (ArrayList<Lift>) response.body();

                    Log.d("dataAll","dataAll : " + date);
                }
            }
            @Override
            public void onFailure(Call<List<Lift>> call, Throwable t) {
                t.printStackTrace();
                Log.d("retrofit","ERROR");
            }
        });
    }

    public void callList1(int lift_id ){
        Call<List<Lift>> call = liftInterface.getElevatorSelectList(lift_id);
        call.enqueue(new Callback<List<Lift>>() {
            @Override
            public void onResponse(Call<List<Lift>> call, Response<List<Lift>> response) {
                if(response.isSuccessful()){
                    List<Lift> date = (List<Lift>) response.body();
                    Log.d("lift_id(6)","lift_id(6) : " + date);
                }
            }
            @Override
            public void onFailure(Call<List<Lift>> call, Throwable t) {
                t.printStackTrace();
                Log.d("retrofit","ERROR");
            }
        });
    }

    public void callList2(String date){
        Call<List<Lift>> call = liftInterface.UpdateElevator(date);
        call.enqueue(new Callback<List<Lift>>() {
            @Override
            public void onResponse(Call<List<Lift>> call, Response<List<Lift>> response) {
                if(response.isSuccessful()){
                    List<Lift> date = (List<Lift>) response.body();
                    Log.d("date","date : " + date);
                }
            }
            @Override
            public void onFailure(Call<List<Lift>> call, Throwable t) {
                t.printStackTrace();
                Log.d("retrofit","ERROR");
            }
        });
    }
    public void sendList0(Lift lift){
      //  Lift lift = new Lift(10,"중앙도서관 01","정상","충북 충주시 대학로 50");
        Call<List<Lift>>call = liftInterface.getLiftInfo(lift);
        call.enqueue(new Callback<List<Lift>>() {
            @Override
            public void onResponse(Call<List<Lift>> call, Response<List<Lift>> response) {
                if(response.isSuccessful()){
                    List<Lift> date = (List<Lift>) response.body();
                    Log.d("retrofit","res : " + date);
                }
            }
            @Override
            public void onFailure(Call<List<Lift>> call, Throwable t) {
                t.printStackTrace();
                Log.d("retrofit","ERROR");
            }
        });
    }

    public void sendList1(ErrorLift errorLift) {
        //ErrorLift errorLift = new ErrorLift(10,"비정상","120");
        Call<List<ErrorLift>> call = liftInterface.ErrorPost(errorLift);
        call.enqueue(new Callback<List<ErrorLift>>() {
            @Override
            public void onResponse(Call<List<ErrorLift>> call, Response<List<ErrorLift>> response) {
                if(response.isSuccessful()){
                    List<ErrorLift> date = (List<ErrorLift>) response.body();
                    Log.d("ErrorPost","res : " + date);
                }
            }
            @Override
            public void onFailure(Call<List<ErrorLift>> call, Throwable t){
                t.printStackTrace();
                Log.d("retrofit","ERROR");
            }

        });
    }

    public void sendList2( Checkinglist checkinglist) {
       // Checkinglist checkinglist = new Checkinglist(10,"정상","김엔지니어/노후화된 전선 교체, 손잡이 교체작업");
        Call<List<Checkinglist>> call = liftInterface.Checkinglist(checkinglist);
        call.enqueue(new Callback<List<Checkinglist>>() {
            @Override
            public void onResponse(Call<List<Checkinglist>> call, Response<List<Checkinglist>> response) {
                if(response.isSuccessful()){
                    List<Checkinglist> date = (List<Checkinglist>) response.body();
                    Log.d("Checkinglist","res : " + date);
                }
            }
            @Override
            public void onFailure(Call<List<Checkinglist>> call, Throwable t) {
                t.printStackTrace();
                Log.d("retrofit","ERROR");
            }
        });
    }
}