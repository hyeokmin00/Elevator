package com.example.elevator.ui.splash;

import static java.lang.Thread.sleep;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.elevator.R;
import com.example.elevator.api.APIController;
import com.example.elevator.sock.SocketActivity;
import com.example.elevator.ui.main.MainActivity;
import com.example.elevator.utils.NetStat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SplashActivity extends AppCompatActivity {
/* LiftList 받은 후 SharedPref에 저장(기기에 저장된 최근 조회 날짜와 오늘 날짜 비교하여 목록 갱신)*/

    // api 서버로부터 엘리베이터 데이터 요청 - sharedPreferences에 저장
    // cmd가 1인 경우 해당 날짜 이후에 추가된 엘리베이터 정보 요청
    // cmd가 2인 경우 : 시나리오에 나와있지 않아 임의로 전체 엘레베이터 정보 불러옴

    //날짜 default 값은 오늘 날짜 불러옴. 그 외에는 사용자가 입력하여 변경 가능하나
    // 해당 부분 타임피커 이용한 변경권장
    APIController apiController = new APIController();
    Context context = this;

    String today;
    String date;

    static final String UPDATEDATE = "UPDATEDATE";
    static final String LASTDATE = "LASTDATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        //sharedPref 에 date 저장하고 불러 올 때마다 날짜 갱신.
        //최초 실행이 아닌 경우 날짜가 갱신됨.
        if (getUpdatedDate(UPDATEDATE) == null) {
            date = "1997-09-24";
            Log.d("Test", "SplashActivity - 승강기 목록 최초 갱신");
        } else {
            date = getUpdatedDate(UPDATEDATE);
            Log.d("Test", "SplashActivity - 최종 갱신일 : " + date);
        }

        //날짜 기본 값 : 오늘 날짜
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        today = sdf.format(dt);


        //최종 갱신일과 오늘 날짜가 동일한 경우 DB 갱신 없이 승강기 목록으로 이동
        if (date.equals(today)) {
            Log.d("Test", "date == today -> ");
            context.startActivity(new Intent(context, MainActivity.class));
            finish();
        } else {
            Log.d("Test", "mobile Stat == true");
            apiController.setRetrofitInit();
            //날짜 기본 값 : 오늘 날짜
            apiController.UpdatedLiftList(context, date);
            putUpdatedDate(UPDATEDATE, sdf.format(dt));
            context.startActivity(new Intent(context, MainActivity.class));
            finish();

        }
    }

    //승강기 목록 갱신일 저장 sharedPref
    private void putUpdatedDate(String key, String value) {
        Log.d("Test", "Put " + key + " (value : " + value + " ) to " + UPDATEDATE);
        SharedPreferences preferences = getSharedPreferences(UPDATEDATE, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String getUpdatedDate(String key) {
        Log.d("Test", "Get " + key + " from " + UPDATEDATE);
        return getSharedPreferences(UPDATEDATE, 0).getString(key, null);
    }
}
