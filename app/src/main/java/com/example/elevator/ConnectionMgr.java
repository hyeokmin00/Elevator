package com.example.elevator;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

public class ConnectionMgr extends MainActivity {
    String enableWifi(){

            /*
            String WIFI_NAME = "";
            String WIFI_PASSWORD = "";
            WifiConfiguration wificonfig = new WifiConfiguration();
            wificonfig.SSID = String.format("\"%s\"", WIFI_NAME);
            wificonfig.preSharedKey = String.format("\"%s\"", WIFI_PASSWORD);

            WifiManager wifiManagerAboutConnect =(WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
            int netId = wifiManagerAboutConnect.addNetwork(wificonfig);
            wifiManagerAboutConnect.disconnect();
            wifiManagerAboutConnect.enableNetwork(netId, true);
            wifiManagerAboutConnect.reconnect();
            */


        WifiManager wifiManager = (WifiManager)getApplicationContext( ).getSystemService(WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);


        String ssid = null;

        ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


        if (networkInfo.isConnected()) {

            WifiManager wifiManagerAboutSSID = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo connectionInfo = wifiManagerAboutSSID.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
        }

        return ssid;
    }

    public void disableWifi() {

        WifiManager wifiManager = (WifiManager)getApplicationContext( ).getSystemService(WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
    }
}
