package com.example.myapplication.bluetooth;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.MyAdapter;

import java.util.List;

public class BluetoothActivity extends AppCompatActivity implements MyAdapter.ItemClickListener {


    private BluetoothAdapter mBluetoothAdapter  = null;
    private BluetoothLeScanner mBluetoothLeScanner = null;

    public static final int REQUEST_BT_PERMISSIONS = 0;
    public static final int REQUEST_BT_ENABLE = 1;

    private boolean mScanning = false;
    private Handler mHandler = null;
    private static final long SCAN_PERIOD = 10000;

    private MyAdapter leDeviceListAdapter;


    private ScanCallback mLeScanCallback =
            new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    Log.d("BLE", result.getDevice().getName());
                    Log.d("BLE", "success");
                }
                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);
                    Log.d("BLE", "error");
                }
            };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        this.mHandler = new Handler();


        checkBtPermissions();
        enableBt();

    }

    public void onBtnScan(View v){
        if(mScanning){
            mScanning = false;
            scanLeDevice(false);
        }else {
            mScanning = true;
            scanLeDevice(true);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkBtPermissions() {
        this.requestPermissions(
                new String[]{
                        Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN
                },
                REQUEST_BT_PERMISSIONS);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void enableBt(){
        if(mBluetoothAdapter !=null && !mBluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent,REQUEST_BT_ENABLE);
        }
    }


    public void scanLeDevice(final boolean enable){
        if(enable){
            mScanning = true;
            Log.d("Scanning", "start");
            mBluetoothLeScanner.startScan(mLeScanCallback);
        }else {
            Log.d("Scanning", "stop");
            mScanning = false;
            mBluetoothLeScanner.stopScan(mLeScanCallback);
        }
    }



    @Override
    public void onItemClick(View view, int position) {

    }
}





















