package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import java.net.*;
import java.io.*;
import android.util.Log;


public class MainActivity extends AppCompatActivity {
    HotSpotTurnOn hotSpotTurnOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hotSpotTurnOn = new HotSpotTurnOn(this);

         //hotSpotTurnOn.showWritePremissionSettings(true);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        hotSpotTurnOn.showWritePremissionSettings(true);
    }
    
    public static final String EXTRA_MESSAGE =
        "com.example.myfirstapp.MESSAGE";
    private String messageFromServer = "MEssage ";

    public void sendMessage (View view) {

        Log.d("SEND","s");
        Sender("OMAR EBAT");
    }
    public void recvMessage (View view){
        Log.d("RECV", "r");
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    ServerSocket socket = new ServerSocket(6666);
                    Socket s = socket.accept();
                    Log.d("TYT", "ss");
                     BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String word = in.readLine();
                    Log.d("Message from server: ", word);
                    messageFromServer = word;
                    in.close();
                    s.close();
                    Intent intent = new Intent(MainActivity.this, DisplayMessageActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, messageFromServer);
                    startActivity(intent);
                }catch (Exception e) { e.printStackTrace();}

            }
        });
        thread.start();
    }
    private void Sender(final String msg){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    Socket s = new Socket("192.168.43.1", 6666);
                    Log.d("TYT", "ss");
                    OutputStream out = s.getOutputStream();
                    PrintWriter output = new PrintWriter(out);
                    output.println(msg);
                    output.flush();
                    output.close();
                    out.close();
                    s.close();
                }catch (Exception e) { e.printStackTrace();}
            }
        });
        thread.start();
    }

    public void showWiFi(View view){
        Log.d("PIDOR", "ZDES");
        Intent intent =
            new Intent(MainActivity.this, DisplayWiFiListActivity.class);
        startActivity(intent);
    }

    public void hotSpotHandler(View view){
       // HotSpotTurnOn hotSpotTurnOn = new HotSpotTurnOn(this);
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
         //       hotSpotTurnOn.showWritePremissionSettings(true);
                hotSpotTurnOn.setWifiApEnabled(hotSpotTurnOn.getWifiApConfiguration(),true);
            }
        });
        thread.start();
    }
    
    

}
