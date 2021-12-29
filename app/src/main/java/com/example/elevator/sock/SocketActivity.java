package com.example.elevator.sock;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.elevator.ConnectionMgr;
import com.example.elevator.R;
import com.example.elevator.api.APIController;
import com.example.elevator.ui.report.WriteReportActivity;
import com.example.elevator.utils.NetStat;

import org.json.JSONObject;

import java.util.ArrayList;

//todo EnableWifi 를 통해 기기 연결 후 에러 목록 변수에 저장하기
// lte 네트워크 연결 APIController에서 ErroPost로 보냄 -> WriteActivity로 화면 전환

public class SocketActivity extends AppCompatActivity {
    /* EnableWifi 이용해서 와이파이 기기 연결 후 ErrorLift 저장
    -> wifi 연결 해제 후 ErrorPost 메서드 이용해 서버로 전송
    -> liftID WriteReportActivity에 전달 및 화면 전환*/

    String ssidPattern;
    String password;
    public String liftId;

    static final int PERMISSIONS_REQUEST = 0x0000001;
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;

    APIController apiController = new APIController();
    Context context = this;
    NetStat netStat = new NetStat();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        ConnectionMgr connectionMgr = new ConnectionMgr(context);
        APIController apiController = new APIController();
        SockClient sc = new SockClient();

        Intent intent = getIntent();
        liftId = intent.getStringExtra("lift_id");

        Log.d("Test", "SocketActivity OnCreated");

        Boolean wifiStat = netStat.isWIFIConnected(this);
        Boolean mobileStat = netStat.isMOBILEConnected(this);
        //wifi 기기와 연결 - 기기가 하나뿐이라 임시로 하드 코딩함
        ssidPattern = "CarKey";
        password = "1234qqqq";

        int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
        if (status == NetworkStatus.TYPE_WIFI){
            startActivity(new Intent(Settings.Panel.ACTION_WIFI));
            Toast.makeText(this, "와이파이 연결을 해제해주세요.", Toast.LENGTH_LONG).show();
        }


        if (!wifiStat) {
            if (status != NetworkStatus.TYPE_WIFI){
                startActivity(new Intent(Settings.Panel.ACTION_WIFI));
                Toast.makeText(this, "와이파이를 연결해주세요.", Toast.LENGTH_LONG).show();
            }

            //enableWifi()
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            try {
                if (!wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(true);
                }

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                    WifiNetworkSpecifier.Builder builder = new WifiNetworkSpecifier.Builder();
                    builder.setSsidPattern(new PatternMatcher("CarKey",PatternMatcher.PATTERN_PREFIX));
                    builder.setWpa2Passphrase("1234qqqq");

                    WifiNetworkSpecifier wifiNetworkSpecifier = builder.build();

                    final NetworkRequest.Builder networkRequestBuilder = new NetworkRequest.Builder();
                    networkRequestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
                    networkRequestBuilder.setNetworkSpecifier(wifiNetworkSpecifier);

                    NetworkRequest networkRequest = networkRequestBuilder.build();

                    connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
                    connectivityManager.requestNetwork(networkRequest, networkCallback);


                } else {
                    WifiConfiguration wifiConfiguration = new WifiConfiguration();
                    wifiConfiguration.SSID = String.format("\"%s\"", "Carkey_WiFi11"); // 연결하고자 하는 SSID
                    wifiConfiguration.preSharedKey = String.format("\"%s\"", "1234qqqq"); // 비밀번호
                    int wifiId = wifiManager.addNetwork(wifiConfiguration);
                    wifiManager.enableNetwork(wifiId, true);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {

                JSONObject sendObj = new JSONObject();
                sendObj.put("cmd", (byte) 0x21);
                sendObj.put("length", (byte) 0x06);
                sendObj.put("data", null);

                ThreadSendAndRecieve sarThread = new ThreadSendAndRecieve(sendObj);
                Thread thread = new Thread(sarThread);

                thread.start();
                try {
                    thread.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                JSONObject Data = sarThread.getResult();

                // disableWifi();
                try {
                    if (wifiManager.isWifiEnabled()) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                            connectivityManager.unregisterNetworkCallback(networkCallback);

                        } else {
                            if (wifiManager.getConnectionInfo().getNetworkId() == -1) {

                            } else {
                                int networkId = wifiManager.getConnectionInfo().getNetworkId();
                                wifiManager.removeNetwork(networkId);
                                wifiManager.saveConfiguration();
                                wifiManager.disconnect();
                            }
                        }

                    } else
                        Toast.makeText(getApplicationContext(), "Wifi 꺼짐", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "연결 해제 예외 : " + e.toString(), Toast.LENGTH_SHORT).show();
                }


       /*
        jsonObject ErrorPost = JSONObject ErrorPost 맞게 형변환
        ErrorResult errorResult;
                String lift_id = "1234572";
                ArrayList<LiftError> lift_errors = new ArrayList<LiftError>();
                String errCode = "128";
                String datetime = "2021-11-18 21:21:01";

                lift_errors.add(new LiftError(Integer.parseInt(errCode), datetime));
                errorResult = new ErrorResult(Integer.parseInt(lift_id), lift_errors);

                if (networkCallback == true) {
                    apiController.setRetrofitInit();
                    apiController.ErrorPost(errorResult);
                    apiController return값이 200 인 경우 화면전환
                } */
                Log.d("Test", Data.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        Intent intent2 = new Intent(context, WriteReportActivity.class);
        intent2.putExtra("lift_id", liftId);
        context.startActivity(intent2);
        finish();


    }


    public void OnCheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //사용자가 권한 요청을 명시적으로 거부한 경우 - true 처음 보거나 다시 묻지 않음 선택한 경우 false 반환
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "앱 실행을 위해서는 권한을 설정해야 합니다", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_REQUEST);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSIONS_REQUEST);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "앱 실행을 위한 권한이 설정 되었습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "앱 실행을 위한 권한이 취소 되었습니다", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

class NetworkStatus {
    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_NOT_CONNECTED = 3;

    public static int getConnectivityStatus(Context context){ //해당 context의 서비스를 사용하기위해서 context객체를 받는다.
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo != null){
            int type = networkInfo.getType();
            if(type == ConnectivityManager.TYPE_MOBILE){//쓰리지나 LTE로 연결된것(모바일을 뜻한다.)
                return TYPE_MOBILE;
            }else if(type == ConnectivityManager.TYPE_WIFI){//와이파이 연결된것
                return TYPE_WIFI;
            }
        }
        return TYPE_NOT_CONNECTED;  //연결이 되지않은 상태
    }
}
