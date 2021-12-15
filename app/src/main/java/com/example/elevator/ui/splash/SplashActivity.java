package com.example.elevator.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.elevator.ConnectionMgr;
import com.example.elevator.R;
import com.example.elevator.api.APIController;
import com.example.elevator.api.roomdb.LiftDB;
import com.example.elevator.ui.main.MainActivity;
import com.example.elevator.ui.main.adapter.LiftRecyAdapter;
import com.example.elevator.utils.NetStat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SplashActivity extends AppCompatActivity {
    // api 서버로부터 엘리베이터 데이터 요청 - sharedPreferences에 저장
    // cmd가 1인 경우 해당 날짜 이후에 추가된 엘리베이터 정보 요청
    // cmd가 2인 경우 : 시나리오에 나와있지 않아 임의로 전체 엘레베이터 정보 불러옴

    //날짜 default 값은 오늘 날짜 불러옴. 그 외에는 사용자가 입력하여 변경 가능하나
    // 해당 부분 타임피커 이용한 변경권장
    int TIMEOUT_LIMITS = 100;
    LiftRecyAdapter liftRecyAdapter;
    APIController apiController = new APIController();
    Context context;
    NetStat netStat;
    ConnectionMgr connectionMgr;
    LiftDB liftdb;
    String date;
    String today;

    static final String UPDATEDAT = "UPDATEDAT";
    static final String LASTDATE = "LASTDATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        //sharedPref 에 date 저장하고 불러 올 때마다 날짜 갱신.
        //최초 실행이 아닌 경우 날짜가 갱신됨.

        if (getUpdatedDate(UPDATEDAT) == null) {
            date = "1997-09-24";
            Log.d("Test", "SplashActivity - 승강기 목록 최초 갱신");
        } else {
            date = getUpdatedDate(UPDATEDAT);
            Log.d("Test", "SplashActivity - 최종 갱신일 : " + date);
        }

        Boolean wifiStat = netStat.isWIFIConnected(this);
        Boolean mobileStat = netStat.isMOBILEConnected(this);

        //날짜 기본 값 : 오늘 날짜
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        today = sdf.format(dt);
        Log.d("Test","Today == date"+ (today.toString()==date.toString()));
        Log.d("Test","Today == date type"+ (today).length());
        Log.d("Test","Today == date type"+ (date).length());
        Log.d("Test","Today : "+today);

        //인터넷이 아예 연결되지 않은 경우
        if (!wifiStat & !mobileStat) {
            //화면 변경
            context.startActivity(new Intent(this, MainActivity.class));
            Log.d("Test", "인터넷 연결되지 않음 Stat == true");

        } else if (wifiStat == true) {
            //todo 와이파이 연결됨 -> 에러 데이터 포스트로 전달함
            //connectionMgr.enableWifi();
            //아래 두 줄은 임시로 lte 연결이 된 경우에 실행되는 코드를 작성하였음
            Log.d("Test", "wifi Stat == true");

            if (date.equals(today)) {
                context.startActivity(new Intent(context, MainActivity.class));
                finish();
            } else {
                Log.d("Test", "today : " + today);
                Log.d("Test", "date : " + date);
                apiController.setRetrofitInit();
                apiController.UpdatedLiftList(this, date);
                finish();
                putUpdatedDate(UPDATEDAT, sdf.format(dt));
            }
        } else {
            Log.d("Test", "mobile Stat == true");
          /*






               //최근 갱신일
            if(date != today){
                    apiController.setRetrofitInit();
                    apiController.UpdatedLiftList(this,date);
                    putUpdatedDate(UPDATEDAT, sdf.format(dt));
            }*/
        }
    }

    //승강기 목록 갱신일 저장 sharedPref
    private void putUpdatedDate(String key, String value) {
        Log.d("Test", "Put " + key + " (value : " + value + " ) to " + UPDATEDAT);
        SharedPreferences preferences = getSharedPreferences(UPDATEDAT, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String getUpdatedDate(String key) {
        Log.d("Test", "Get " + key + " from " + UPDATEDAT);
        return getSharedPreferences(UPDATEDAT, 0).getString(key, null);
    }
}
