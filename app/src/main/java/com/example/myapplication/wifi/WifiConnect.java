package com.example.myapplication.wifi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.myapplication.MainActivity;

import java.util.List;

public class WifiConnect {
    private final String TAG = "WifiConnect";
    private WifiConfiguration wifiConfiguration;
    private WifiManager wifiManager;
    private Context context;

    public WifiConnect(Context context) {
        this.context = context;
        wifiConfiguration = new WifiConfiguration();
        wifiManager = (WifiManager) this.context.getSystemService(this.context.WIFI_SERVICE);
    }

    public void ConnectToProtectedNetwork(String SSID, String KEY) {
        wifiConfiguration.SSID = "\"" + SSID + "\"";
        wifiConfiguration.preSharedKey = "\"" + KEY + "\"";
        helper(SSID);

    }
    public void ConnectToOpenNetwork(String SSID) {
        wifiConfiguration.SSID = "\"" + SSID + "\"";
        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        helper(SSID);
    }


    private void helper(String SSID){
        int netId = wifiManager.addNetwork(wifiConfiguration);
        wifiManager.setWifiEnabled(true);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for(WifiConfiguration i : list){
            if(i.SSID !=null && i.SSID.equals("\"" + SSID + "\"")){
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId,true);
                wifiManager.reconnect();

                break;
            }
        }
    }


}
