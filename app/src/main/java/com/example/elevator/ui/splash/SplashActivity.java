package com.example.elevator.ui.splash;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
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
import com.example.elevator.api.roomdb.LiftDB;
import com.example.elevator.sock.SockClient;
import com.example.elevator.ui.main.MainActivity;
import com.example.elevator.ui.main.adapter.LiftRecyAdapter;
import com.example.elevator.utils.NetStat;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

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
    ConnectionMgr connectionMgr = new ConnectionMgr();
    Context context = this;
    NetStat netStat = new NetStat();
    SockClient sockClient = new SockClient();

    String date;
    String today;
    String ssidPattern;
    String password;

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

        //인터넷이 아예 연결되지 않은 경우
        if (!wifiStat & !mobileStat) {
            //화면 변경
            context.startActivity(new Intent(this, MainActivity.class));
            Log.d("Test", "인터넷 연결되지 않음 Stat == true");
        } else {
            //임시로 ssidPattern, pw 하드코딩함
            //wifi 기기와 연결
            ssidPattern = "CarKey";
            password = "1234qqqq";
            enableWifi(ssidPattern, password);

            if (wifiStat == true) {
                //todo 와이파이 연결됨 -> 에러 데이터 포스트로 전달함
                Log.d("Test", "wifi Stat == true");

                try {
                    JSONObject testObj = new JSONObject();
                    testObj.put("cmd", (byte) 0x21);
                    testObj.put("length", (byte) 0x06);
                    testObj.put("data", null);
                    sockClient.send(testObj);
                    JSONObject errorList = sockClient.recv();
                    Log.d("Test", "SplashActivity - SockClient.recv return 1 : " + errorList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //  disableWifi();
                //todo json Object를 Array로 변환하는 과정 필요
                //또는 해당 json Obj 바로 전송 가능한지 확인

            } else {
                Log.d("Test", "mobile Stat == true");
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
            }
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


    static final int PERMISSIONS_REQUEST = 0x0000001;
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;

    public void enableWifi(String ssidPattern, String password) {
        // 와이파이 사용가능하게 하고 연결된 wifi 기기의 ssid 반환
        OnCheckPermission();
        checkSystemPermission();
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                WifiNetworkSpecifier.Builder builder = new WifiNetworkSpecifier.Builder();
                builder.setSsidPattern(new PatternMatcher(ssidPattern, PatternMatcher.PATTERN_PREFIX));
                builder.setWpa2Passphrase(password);

                WifiNetworkSpecifier wifiNetworkSpecifier = builder.build();

                final NetworkRequest.Builder networkRequestBuilder = new NetworkRequest.Builder();
                networkRequestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
                networkRequestBuilder.setNetworkSpecifier(wifiNetworkSpecifier);

                NetworkRequest networkRequest = networkRequestBuilder.build();
                networkCallback = new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(@NonNull Network network) {
                        super.onAvailable(network);
                        connectivityManager.bindProcessToNetwork(network);
                        Toast.makeText(getApplicationContext(), "연결됨", Toast.LENGTH_SHORT).show();
                    }
                };
                connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
                connectivityManager.requestNetwork(networkRequest, networkCallback);


            } else {
                WifiConfiguration wifiConfiguration = new WifiConfiguration();
                wifiConfiguration.SSID = String.format("\"%s\"", "wifi 이름"); // 연결하고자 하는 SSID
                wifiConfiguration.preSharedKey = String.format("\"%s\"", "비밀번호"); // 비밀번호
                int wifiId = wifiManager.addNetwork(wifiConfiguration);
                wifiManager.enableNetwork(wifiId, true);
                Toast.makeText(getApplicationContext(), "연결됨", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "연결 예외 : " + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public void disableWifi() {
        // wifi disable
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            if (wifiManager.isWifiEnabled()) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    connectivityManager.unregisterNetworkCallback(networkCallback);
                    Toast.makeText(getApplicationContext(), "연결 끊김", Toast.LENGTH_SHORT).show();

                } else {
                    if (wifiManager.getConnectionInfo().getNetworkId() == -1) {
                        Toast.makeText(getApplicationContext(), "연결", Toast.LENGTH_SHORT).show();

                    } else {
                        int networkId = wifiManager.getConnectionInfo().getNetworkId();
                        wifiManager.removeNetwork(networkId);
                        wifiManager.saveConfiguration();
                        wifiManager.disconnect();
                        Toast.makeText(getApplicationContext(), "연결 끊김", Toast.LENGTH_SHORT).show();
                    }
                }
            } else
                Toast.makeText(getApplicationContext(), "Wifi 꺼짐", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "연결 해제 예외 : " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void OnCheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //사용자가 권한 요처을 명시적으로 거부한 경우 - true 처음 보거나 다시 묻지 않음 선택한 경우 false 반환
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

    public boolean checkSystemPermission() {

        boolean permission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   //23버전 이상
            permission = Settings.System.canWrite(this);
            if (permission) {
            } else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 2127);
                permission = false;
            }
        } else {

            Log.d("Test", "ConnectionMgr - checkSysPermission");
        }

        return permission;
    }


}
