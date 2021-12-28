package com.example.elevator.sock;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import com.example.elevator.api.model.ErrorResult;
import com.example.elevator.api.model.LiftError;
import com.example.elevator.utils.NetStat;

import org.json.JSONException;
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
        ConnectionMgr connectionMgr = new ConnectionMgr();
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
        enableWifi(ssidPattern, password);

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

            Log.d("Test",Data.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }


        //todo ErrorCode dummydata 연결함. -> Socket 연결 부분 수정 후 변경 필요
        ErrorResult errorResult;
        String lift_id = "1234572";
        ArrayList<LiftError> lift_errors = new ArrayList<LiftError>();
        String errCode = "128";
        String datetime = "2021-11-18 21:21:01";

        lift_errors.add(new LiftError(Integer.parseInt(errCode), datetime));
        Log.d("Test", "SocketActivity - lift_error.get(0).getErrCode() : " + lift_errors.get(0).getErrCode());
        errorResult = new ErrorResult(Integer.parseInt(lift_id), lift_errors);
        Log.d("Test", "SocketActivity - errorResult.get(0).getLiftId() : " + errorResult.getLiftId());


        //wifi 기기는 서버로 통신 불가
        connectionMgr.disableWifi();

        //todo jo를 LiftError로 변환 후 ErrorPost -> finish();
        apiController.setRetrofitInit();
        apiController.ErrorPost(errorResult);

        Intent intent2 = new Intent(context, SocketActivity.class);
        intent2.putExtra("lift_id", liftId);
        context.startActivity(intent2);
        finish();

    }

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
                        Toast.makeText(getApplicationContext(), "네트워크 연결됨", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUnavailable() {
                        Log.d("Test", "SplashActivity - Enabled : onUnavailable");

                    }
                };
                connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
                connectivityManager.requestNetwork(networkRequest, networkCallback);
            } else {
                Log.d("Test", "SplashActivity - Enabled : onUnavailable");
                WifiConfiguration wifiConfiguration = new WifiConfiguration();
                wifiConfiguration.SSID = String.format("\"%s\"", "wifi 이름"); // 연결하고자 하는 SSID
                wifiConfiguration.preSharedKey = String.format("1234qqqq"); // 비밀번호
                int wifiId = wifiManager.addNetwork(wifiConfiguration);
                wifiManager.enableNetwork(wifiId, true);
                Toast.makeText(getApplicationContext(), "연결됨", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "연결 예외 : " + e.toString(), Toast.LENGTH_SHORT).show();
        }
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
                        Toast.makeText(getApplicationContext(), "연결 끊김 ", Toast.LENGTH_SHORT).show();
                    }
                }
            } else
                Toast.makeText(getApplicationContext(), "Wifi 꺼짐", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "연결 해제 예외 : " + e.toString(), Toast.LENGTH_SHORT).show();
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
