package com.example.myapplication.wifi;

import android.util.Log;

import com.example.myapplication.filemanager.FileItem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

public class TempFileTransfer {

    private static int bufsize = 131072;
    private static final String TAG = "Transfer";
    private static int TRANSFER_PORT = 9999;
    public static final String storageDirectory = "/storage/emulated/0/OmarApplication";

    public static int getTransferPort(){
        return TRANSFER_PORT;
    }
    public static void SendMultipleFiles(String ip, int port, HashMap<String,FileItem> mapa) {
        Log.d(TAG,"send");
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    byte[] bytes = new byte[bufsize];
                    Socket clientSocket = new Socket(ip,port);
                    Log.d(TAG,"good Connect");

                    final BufferedOutputStream outStream = new BufferedOutputStream(clientSocket.getOutputStream());
                    bytes = longToBytes(mapa.size());
                    outStream.write(bytes,0,bytes.length);
                    outStream.flush();
                    Log.d(TAG, String.valueOf(bytes.length));
                    for (FileItem fItem: mapa.values()) {
                        sendFile(fItem,clientSocket,bufsize);
                    }
                    clientSocket.close();
                }catch (Exception e) { e.printStackTrace();}
            }
        });
        thread.start();
    }
    public static void sendFile(FileItem fileItem, Socket socket, int bufsize) throws IOException {

        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());

        String fileExtension = fileItem.getExtension();
        long fileSize = fileItem.getSize();
        String result_message = fileExtension + "/" + String.valueOf(fileSize);

        byte[]bytes;
        int count;
        bytes = result_message.getBytes();

        Log.d(TAG,result_message);
        bufferedOutputStream.write(bytes,0,result_message.length());
        bufferedOutputStream.flush();

        bytes = new byte[bufsize];

        final BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(fileItem.getPath()));
        int sum = 0;
        while (sum < fileSize){
            count = inStream.read(bytes);
            bufferedOutputStream.write(bytes,0,count);
            bufferedOutputStream.flush();
        }
        bufferedOutputStream.close();
        inStream.close();
    }

    public static String requestParser(byte[] bytes){
        String names = "";
        for (int i = 0; i < bytes.length; i++) {
            if(bytes[i] == 0){
                break;
            }
            names = names + (char)bytes[i];
        }
        return  names;
    }
    public static byte[] longToBytes(long l) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte)(l & 0xFF);
            l >>= 8;
        }
        return result;
    }

    public static long bytesToLong(final byte[] b) {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 8;
            result |= (b[i] & 0xFF);
        }
        return result;
    }
}
