package com.example.myapplication.wifi;

import android.Manifest;
import android.app.Activity;
import android.content.*;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.*;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.lang.reflect.*;


public class WifiApManager {
    private final WifiManager mWifiManager;
    private Context context;
    private WifiManager.LocalOnlyHotspotReservation mReservation;
    private boolean isHotspotEnabled = false;

    public WifiApManager(Context context) {
        this.context = context;
        mWifiManager = (WifiManager) this.context.getSystemService(context.WIFI_SERVICE);
    }

    public void showWritePermissionSettings(boolean force) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this.context)) {
                Toast.makeText(context, "Get me permission", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.context.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.context.startActivity(intent);
            } else {
                return;
            }
        }
    }

    public boolean setWifiApEnabled(WifiConfiguration wifiConfig, boolean enabled) {
        try {
            if (enabled) {
                mWifiManager.setWifiEnabled(false);
            }
            Method method = mWifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            return (Boolean) method.invoke(mWifiManager, wifiConfig, enabled);
        } catch (Exception e) {
            Log.e(this.getClass().toString(), "", e);
            return false;
        }
    }

    public void turnOnHotSpotOnAllSdkVersion(){

        if(Build.VERSION_CODES.O > Build.VERSION.SDK_INT) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    setWifiApEnabled(getWifiApConfiguration(), true);
                }
            });
            thread.start();
        }else{
            turnOnHotSpot();
        }
    }
    public boolean isLocationPermissionEnable() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void turnOnHotSpot() {
        //WifiManager.LocalOnlyHotspotReservation mReservation;
        if (!isLocationPermissionEnable()) {
            return;
        }
        if (mWifiManager != null) {
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

            mWifiManager.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {

                @Override
                public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {
                    super.onStarted(reservation);
                    mReservation = reservation;

                    isHotspotEnabled = true;
                }

                @Override
                public void onStopped(){
                    super.onStopped();
                    isHotspotEnabled = false;
                }

                @Override
                public void onFailed(int reason){
                    super.onFailed(reason);

                    isHotspotEnabled = false;
                }

            },new Handler());
        }
    }


    public WIFI_AP_STATE getWifiApState(){
        try {
            Method method = mWifiManager.getClass().getMethod("getWifiApState");

            int tmp = ((Integer) method.invoke(mWifiManager));

            if (tmp >=10){
                tmp = tmp - 10;
            }

            return  WIFI_AP_STATE.class.getEnumConstants()[tmp];
        }catch (Exception e) {
            Log.e(this.getClass().toString(), "", e);
            return WIFI_AP_STATE.WIFI_AP_STATE_FAILED;
        }
    }
    public  boolean isWifiApEnabled(){
        return getWifiApState() == WIFI_AP_STATE.WIFI_AP_STATE_ENABLED;
    }

    public WifiConfiguration getWifiApConfiguration(){
        try{
            Method method = mWifiManager.getClass().getMethod("getWifiApConfiguration");
            return (WifiConfiguration) method.invoke(mWifiManager);
        } catch (Exception e){
            Log.e(this.getClass().toString(), "", e);
            return null;
        }
    }
    public Boolean setWifiApConfiguration(WifiConfiguration wifiConfig){
        try {
            Method method = mWifiManager.getClass().getMethod("setWifiApConfiguration",WifiConfiguration.class);
            return (Boolean) method.invoke(mWifiManager,wifiConfig);
        }catch (Exception e){
            Log.e(this.getClass().toString(), "", e);
            return false;
        }
    }
    public boolean configureHotspot(String name){
        WifiConfiguration configuration = getCustomConfigs(name);
        return configure(configuration);
    }
    private  boolean configure(WifiConfiguration wifiConfiguration){
        try{
            Method setConfigMethod = mWifiManager.getClass().getMethod("setWifiApConfiguration",
                    WifiConfiguration.class);
            boolean status = (boolean) setConfigMethod.invoke(mWifiManager,wifiConfiguration);
            return status;
        } catch (IllegalArgumentException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private WifiConfiguration getCustomConfigs(String ssid){
        WifiConfiguration wifiConfig = null;
        try {
            Method getConfigMethod = mWifiManager.getClass().getMethod("getWifiApConfiguration");
            wifiConfig = (WifiConfiguration) getConfigMethod.invoke(mWifiManager);
            if (!TextUtils.isEmpty(ssid))
                wifiConfig.SSID = ssid;
            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            return wifiConfig;
        } catch (IllegalArgumentException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (wifiConfig == null) {
            wifiConfig = new WifiConfiguration();
            if (!TextUtils.isEmpty(ssid))
                wifiConfig.SSID = ssid;


            wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
//            wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
  //          wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        return wifiConfig;
    }

}

