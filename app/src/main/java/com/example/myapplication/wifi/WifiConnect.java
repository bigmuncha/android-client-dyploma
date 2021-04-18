package com.example.myapplication.wifi;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.InetAddresses;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.myapplication.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public String getDeviceWlanIp(){

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        int ipAddress = wifiInfo.getIpAddress();
        String ipString = String.format("%d.%d.%d.%d",
                (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        return ipString;
    }

    public String parseGateway(String dhcpinfo){
        String regex = ".+ gateway (\\d+\\.\\d+\\.\\d+\\.\\d+) ";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(dhcpinfo);
        if(matcher.find()){
            return matcher.group(1);
        }else{
            return null;
        }
    }

    public String getRouterIp() throws IOException {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i("routes ", connectivityManager.getLinkProperties(connectivityManager.getActiveNetwork()).getRoutes().toString());
           // Log.i("domains ", connectivityManager.getLinkProperties(connectivityManager.getActiveNetwork()).getDomains());
            Log.i("ip address ", connectivityManager.getLinkProperties(connectivityManager.getActiveNetwork()).getLinkAddresses().toString());
            Log.i("dns address ", connectivityManager.getLinkProperties(connectivityManager.getActiveNetwork()).getDnsServers().toString());

        }
        if(connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI) {
            Log.i("myType ", "wifi");
            DhcpInfo d =wifiManager.getDhcpInfo();
            Log.i("info", d.toString()+"");
            return parseGateway(d.toString());
        }
        return "";

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
