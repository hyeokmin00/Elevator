package com.example.elevator.api;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.elevator.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Post> postArrayList = new ArrayList<>();
    private ArrayList<ErrorPost> errorPostArrayList = new ArrayList<>();
    private ArrayList<Checkinglist> checkinglistArrayList = new ArrayList<>();
    private LiftApi liftApi;
    @Override
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

    }

    public void setRetrofitInit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://boas.asuscomm.com:10002/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        liftApi = retrofit.create(LiftApi.class);

    }

    public void callList0(){
        Call<List<Post>> call = liftApi.getElevatorAllList();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful()){
                    List<Post> date = (List<Post>) response.body();
                    Log.d("dataAll","dataAll : " + date);
                }
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                Log.d("retrofit","ERROR");
            }
        });
    }

    public void callList1(){
        Call<List<Post>> call = liftApi.getElevatorSelectList(6);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful()){
                    List<Post> date = (List<Post>) response.body();
                    Log.d("lift_id(6)","lift_id(6) : " + date);
                }
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                Log.d("retrofit","ERROR");
            }
        });
    }

    public void callList2(){
        Call<List<Post>> call = liftApi.UpdateElevator("date");
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful()){
                    List<Post> date = (List<Post>) response.body();
                    Log.d("date","date : " + date);
                }
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                Log.d("retrofit","ERROR");
            }
        });
    }
    public void sendList0(){
        Post post = new Post (10,"중앙도서관 01","정상","충북 충주시 대학로 50");
        Call<List<Post>>call = liftApi.Post(post);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful()){
                    List<Post> date = (List<Post>) response.body();
                    Log.d("retrofit","res : " + date);
                }
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
                Log.d("retrofit","ERROR");
            }
        });
    }

    public void sendList1() {
        ErrorPost post = new ErrorPost(10,"비정상","120");
        Call<List<ErrorPost>> call = liftApi.ErrorPost(post);
        call.enqueue(new Callback<List<ErrorPost>>() {
            @Override
            public void onResponse(Call<List<ErrorPost>> call, Response<List<ErrorPost>> response) {
                if(response.isSuccessful()){
                    List<ErrorPost> date = (List<ErrorPost>) response.body();
                    Log.d("ErrorPost","res : " + date);
                }
            }
            @Override
            public void onFailure(Call<List<ErrorPost>> call, Throwable t){
                t.printStackTrace();
                Log.d("retrofit","ERROR");
            }

        });
    }

    public void sendList2() {
        Checkinglist post = new Checkinglist(10,"정상","김엔지니어/노후화된 전선 교체, 손잡이 교체작업");
        Call<List<Checkinglist>> call = liftApi.Checkinglist(post);
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