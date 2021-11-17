package com.example.elevator;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
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
                    List<Post> date = response.body();
                }


            }


            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void callList1(){
        Call<List<Post>> call = liftApi.getElevatorSelectList(6);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful()){
                    List<Post> date = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void callList2(){
        Call<List<Post>> call = liftApi.UpdateElevator("date");
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(response.isSuccessful()){
                    List<Post> date = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    public void sendList0(){
        Post post = new Post (23, "중앙도서관","정상","충북 충주시 대학로 50");
        Call<Post> call = liftApi.createPost(post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()){
                    List<Post> date = (List<Post>) response.body();
                    //textViewResult.setText(response.body().bodyPost)
                    //bodyValue
                    //sendList 0 ,1 ,2  ui쪽으로 넘기기
                }
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }
    public void sendList1() {
        ErrorPost post = new ErrorPost(1,"비정상","120");
        Call<Post> call = liftApi.errorPost(post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()){
                    List<Post> date = (List<Post>) response.body();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }
    public void sendList2() {
        Checkinglist post = new Checkinglist(1,"정상","정상작동함, 점검 완료");
        Call<Post> call = liftApi.Checkinglist(post);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()){
                    List<Post> date = (List<Post>) response.body();
                }
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t) {
            }
        });
    }

    public void apiCall() {

    }

    public void sockCall() {

    }

    public void jsonCall() {

    }

    public void makeUi() {

    }
}