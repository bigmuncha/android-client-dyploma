package com.example.myapplication;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileTransfer {
    private static int bufsize = 8192;
    private static final String TAG = "Transfer";
    public static void SendOneFile(String ip, int port, String path) throws IOException {
        Log.d(TAG,"send");
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    byte[] bytes = new byte[bufsize];
                    Socket clientSocket = new Socket(ip,port);
                    final BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(path));
                    final BufferedOutputStream outStream = new BufferedOutputStream(clientSocket.getOutputStream());
                    int count;
                    while((count = inStream.read(bytes)) > 0){
                        outStream.write(bytes,0,count);
                    }
                    inStream.close();
                    outStream.close();
                }catch (Exception e) { e.printStackTrace();}
            }
        });
        thread.start();
    }

    public static void RecvOneFile(int port, String folderPath) throws IOException {
        Log.d(TAG,"recv");
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    ServerSocket serverSocket = new ServerSocket(port);
                    byte[] bytes = new byte[bufsize];
                    Socket client = serverSocket.accept();
                    final BufferedInputStream inStream = new BufferedInputStream(client.getInputStream());
                    final BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(folderPath + "/temp"));
                    int count;
                    while((count = inStream.read(bytes)) > 0){
                        outStream.write(bytes,0,count);
                    }
                    inStream.close();
                    outStream.close();
                }catch (Exception e) { e.printStackTrace();}
            }
        });
        thread.start();
    }
}
