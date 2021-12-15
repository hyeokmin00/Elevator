package com.example.elevator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import android.os.PatternMatcher;
import android.provider.Settings;
import android.widget.Toast;

public class ConnectionMgr extends AppCompatActivity {
    // 와이파이 사용가능하게 하고 연결된 wifi 기기의 ssid 반환
    // wifi disable

    Context context;

    static final int PERMISSIONS_REQUEST = 0x0000001;
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;


    public void enableWifi(String ssidPattern, String password) {
        // 와이파이 사용가능하게 하고 연결된 wifi 기긱의 ssid 반환

        OnCheckPermission();
        checkSystemPermission();

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                WifiNetworkSpecifier.Builder builder = new WifiNetworkSpecifier.Builder();
                builder.setSsidPattern(new PatternMatcher(ssidPattern,PatternMatcher.PATTERN_PREFIX));
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

    void disableWifi() {
        // wifi disable
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

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

        }

        return permission;
    }
}