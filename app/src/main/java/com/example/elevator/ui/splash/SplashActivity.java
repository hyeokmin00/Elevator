package com.example.elevator.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elevator.R;
import com.example.elevator.api.APIController;
import com.example.elevator.ui.main.MainActivity;
import com.example.elevator.ui.main.adapter.LiftRecyAdapter;

import java.util.concurrent.ThreadPoolExecutor;

public class SplashActivity extends AppCompatActivity {
    // api 서버로부터 엘리베이터 데이터 요청 - sharedPreferences에 저장
    // cmd가 1인 경우 해당 날짜 이후에 추가된 엘리베이터 정보 요청
    // cmd가 2인 경우 : 시나리오에 나와있지 않아 임의로 전체 엘레베이터 정보 불러옴

    //todo 시간으로 설정된 것 sharedPref에 값 저장으로 변경해야함
    int TIMEOUT_LIMITS = 100;
    LiftRecyAdapter liftRecyAdapter;
    APIController apiController = new APIController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        //retrofitInit - Retrofit 객체 생성
        apiController.setRetrofitInit();
        //현재 apiacontroller의 liftlist에서 바로 recycler뷰로 데이터 붙이게 되어있음
        //해당 내용 시나리오에 맞춰 변경 필요

        apiController.LiftList();





/*
        try {
            Thread.sleep(TIMEOUT_LIMITS); //대기 초 설정
            // TIMEOUT_LIMITS초가 지난 후 실행할 Activity class
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } catch (Exception e) {
            Log.e("Error", "SplashActivity ERROR", e);
            e.printStackTrace();
        }
*/



    }
}
