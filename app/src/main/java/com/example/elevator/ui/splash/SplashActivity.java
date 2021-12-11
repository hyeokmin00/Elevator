package com.example.elevator.ui.splash;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.elevator.R;

public class SplashActivity extends AppCompatActivity {
    // api 서버로부터 엘리베이터 데이터 요청 - sharedPreferences에 저장
    // cmd가 1인 경우 해당 날짜 이후에 추가된 엘리베이터 정보 요청
    // cmd가 2인 경우 : 시나리오에 나와있지 않아 임의로 전체 엘레베이터 정보 불러옴


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
}
