package com.example.myapplication.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.FileTransfer;
import com.example.myapplication.R;
import com.example.myapplication.filemanager.FileContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DisplayWiFiListActivity extends AppCompatActivity implements MyAdapter.ItemClickListener {

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
        String regex ="SSID: (\\S+),";
        Pattern pattern = Pattern.compile(regex);

        //stringres.add("OMAR");
        for(int i =0; i<results.size(); i++){
            String data= String.valueOf(results.get(i));
            Log.d(TAG, data);

            Matcher matcher = pattern.matcher(data);

            if(matcher.find()){
                stringres.add(matcher.group(1));
                Log.d(TAG, "match find");
            }else{
                Log.d(TAG, "No matches");
                //stringres.add("OMar");
            }
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
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "Click");
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        WifiConnect connection = new WifiConnect(this);
        //connection.ConnectToProtectedNetwork(adapter.getItem(position),"89287222482");
        connection.ConnectToOpenNetwork(adapter.getItem(position));
        Toast.makeText(this, "Try recognizze ip" , Toast.LENGTH_SHORT).show();


        try {
            Toast.makeText(this,"Router ip: " + connection.getRouterIp(),Toast.LENGTH_SHORT).show();
            FileTransfer.SendMultipleFiles(connection.getRouterIp(),FileTransfer.getTransferPort(), FileContainer.getFiles());
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
