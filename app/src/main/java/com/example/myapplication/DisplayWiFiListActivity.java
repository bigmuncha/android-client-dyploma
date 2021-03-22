package com.example.myapplication;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;



public class DisplayWiFiListActivity extends AppCompatActivity {

    MyAdapter adapter;
    WifiManager wifi;
    ArrayList<String> stringres;
    private final String TAG = "OMAR_MAIN_ACTIVITY";

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_wi_fi_list);
        Log.d(TAG,"Create");
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //ActivityCompat.requestPermissions(DisplayWiFiListActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ScanWifi();

        }

    private void scanSuccess(){
        List<ScanResult> results = wifi.getScanResults();
        stringres = new ArrayList<String>();
        for(int i =0; i<results.size(); i++){
            Log.d(TAG, String.valueOf(results.get(i)));
            stringres.add(String.valueOf(results.get(i)));
        }
        Log.d(TAG,"over");
        Log.d(TAG, String.valueOf(results.size()));

        fillAdapter();
    }

    private void ScanWifi(){
        Log.d(TAG,"click");
        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED,
                        false);
                if (success){
                    Log.d(TAG, "success ");
                    scanSuccess();

                }else {
                    Log.d(TAG, "failure");
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getApplicationContext().registerReceiver(wifiScanReceiver,intentFilter);

        boolean success = wifi.startScan();
    }

    private void fillAdapter(){
        RecyclerView recyclerView = findViewById(R.id.wifiList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(this,stringres);
        recyclerView.setAdapter(adapter);
    }

}
