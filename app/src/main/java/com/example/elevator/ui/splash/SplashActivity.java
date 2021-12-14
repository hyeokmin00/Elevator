package com.example.elevator.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elevator.ConnectionMgr;
import com.example.elevator.R;
import com.example.elevator.api.APIController;
import com.example.elevator.api.roomdb.LiftDB;
import com.example.elevator.ui.main.MainActivity;
import com.example.elevator.ui.main.adapter.LiftRecyAdapter;
import com.example.elevator.utils.NetStat;

import java.util.concurrent.ThreadPoolExecutor;

public class SplashActivity extends AppCompatActivity {
    // api 서버로부터 엘리베이터 데이터 요청 - sharedPreferences에 저장
    // cmd가 1인 경우 해당 날짜 이후에 추가된 엘리베이터 정보 요청
    // cmd가 2인 경우 : 시나리오에 나와있지 않아 임의로 전체 엘레베이터 정보 불러옴

    //todo 시간으로 설정된 것 sharedPref에 값 저장으로 변경해야함
    int TIMEOUT_LIMITS = 100;
    LiftRecyAdapter liftRecyAdapter;
    APIController apiController = new APIController();
    Context context;
    NetStat netStat;
    ConnectionMgr connectionMgr;
    LiftDB liftdb ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;

        Boolean wifiStat = netStat.isWIFIConnected(this);
        Boolean mobileStat = netStat.isMOBILEConnected(this);


        //인터넷이 아예 연결되지 않은 경우
        if(wifiStat == false & mobileStat == false){
            //todo 애니메이션 종료 후 화면 변경
            context.startActivity(new Intent(this, MainActivity.class));
            Log.d("Test","인터넷 연결되지 않음 Stat == true");

        }else if(wifiStat == true){
            //todo 와이파이 연결됨 -> 에러 데이터 포스트로 전달함
         //   connectionMgr.enableWifi();
            //todo  아래 두 줄은 임시로 lte 연결이 된 경우에 실행되는 코드를 작성하였음
            Log.d("Test","wifi Stat == true");
            apiController.setRetrofitInit();
            apiController.LiftList(this);

        }else{
            Log.d("Test","mobile Stat == true");
            apiController.setRetrofitInit();
            apiController.LiftList(this);
        }
    }

/*
    // network 연결 여부 확인
    public static boolean isConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();


        // 순간적인 상태 가져오기
        Network currentNetwork = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            currentNetwork = manager.getActiveNetwork();
        }
        //상태 확인
        NetworkCapabilities caps = manager.getNetworkCapabilities(currentNetwork);
        LinkProperties linkProperties = manager.getLinkProperties(currentNetwork);


        manager.registerNetworkCallback(builder.build(), new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                // 네트워크를 사용할 준비가 되었을 때
            }

            @Override
            public void onLost(@NonNull Network network) {
                // 네트워크가 끊겼을 때
            }
        });


        return false;
    }*/


}