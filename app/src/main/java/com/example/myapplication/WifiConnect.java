package com.example.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiConnect {
    private final  String TAG = "WifiConnect";
    private WifiConfiguration wifiConfiguration;
    private WifiManager wifiManager;
    private Context context;
    WifiConnect(Context context){
        this.context = context;
        wifiConfiguration = new WifiConfiguration();
        wifiManager = (WifiManager) this.context.getSystemService(this.context.WIFI_SERVICE);
    }
    public void ConnectToSpecificNetwork(String SSID,String KEY){
        wifiConfiguration.SSID = String.format("\"%s\"", SSID);
        wifiConfiguration.preSharedKey = String.format("\"%s\"", KEY);
        //wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        int netId = wifiManager.addNetwork(wifiConfiguration);
        //wifiManager.disconnect();
        //wifiManager.netw
        wifiManager.enableNetwork(netId,true);
        wifiManager.setWifiEnabled(true);
        //wifiManager.supp
        //wifiManager.reconnect();
        Log.d(TAG,"Connect");
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        

    }
}
