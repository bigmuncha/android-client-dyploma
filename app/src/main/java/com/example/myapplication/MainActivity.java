package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import java.io.*;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.filemanager.FileItem;
import com.example.myapplication.filemanager.FileListActivity;
import com.example.myapplication.wifi.WifiApManager;


public class MainActivity extends AppCompatActivity {
    WifiApManager wifiApManager;
    private String TAG = "MAIN";
    private static final  int STORAGE_PERMISSION_CODE = 101;
    private static final  int FINE_LOCATION_PERMISSION_CODE = 102;
    private static  final String FolderName = "OmarAppFolder";
    private String AbsolutePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiApManager = new WifiApManager(this);

        if(Build.VERSION_CODES.O <= Build.VERSION.SDK_INT){
            Toast.makeText(this,"Oreo", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Not Oreo", Toast.LENGTH_LONG).show();
        }

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                        ,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        wifiApManager.showWritePermissionSettings(true);

        FolderCreator.create(this,FolderName);
        AbsolutePath = FolderCreator.getAbsolutePathToFolder(FolderName);
    }

    public static final String EXTRA_MESSAGE =
        "com.example.myfirstapp.MESSAGE";
    private String messageFromServer = "MEssage ";

    public void sendMessage (View view) throws IOException {

        Intent intent = new Intent(MainActivity.this, FileListActivity.class);
        startActivity(intent);

        //FileTransfer.SendOneFile("192.168.43.133",9999,"/storage/emulated/0/omar.txt");

    }
    public void recvMessage (View view){

    }


    public void showWiFi(View view){
        Log.d("PIDOR", "ZDES");
        Intent intent =
            new Intent(MainActivity.this, DisplayWiFiListActivity.class);
        startActivity(intent);
    }

    public void hotSpotHandler(View view){
        //wifiApManager.configureHotspot("OMar");
       // wifiApManager.turnOnHotSpotOnAllSdkVersion();
        //WifiConfiguration temp = wifiApManager.getWifiApConfiguration();





        //Log.d("MAIN", temp.SSID);
    }


    public void checkPermission(String permission, int requestCode){
        if(ContextCompat.checkSelfPermission(MainActivity.this,permission)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{permission},
                    requestCode);
        }else {
            Toast.makeText(MainActivity.this,"Permission already granted",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode ==STORAGE_PERMISSION_CODE){
            if(grantResults.length >0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this,
                        "Storage permission granted", Toast.LENGTH_SHORT)
                        .show();
            }else {
                Toast.makeText(MainActivity.this,
                        "Storage permission denied", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode ==FINE_LOCATION_PERMISSION_CODE){
            if(grantResults.length >0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this,
                        " Fine location permission granted", Toast.LENGTH_SHORT)
                        .show();
            }else {
                Toast.makeText(MainActivity.this,
                        "Fine location permission denied", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
