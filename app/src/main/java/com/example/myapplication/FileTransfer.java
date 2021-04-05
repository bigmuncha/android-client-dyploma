package com.example.myapplication;

import android.util.Log;

import com.example.myapplication.filemanager.FileItem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardCopyOption;

public class FileTransfer {
    private static int bufsize = 131072;
    private static final String TAG = "Transfer";
    private static int TRANSFER_PORT = 9999;
    public static final String storageDirectory = "/storage/emulated/0/OmarApplication";

    public static int getTransferPort(){
        return TRANSFER_PORT;
    }

    public static void SendOneFile(String ip, int port, FileItem fileItem) throws IOException {
        Log.d(TAG,"send");
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    byte[] bytes = new byte[bufsize];
                    Socket clientSocket = new Socket(ip,port);
                    final BufferedOutputStream outStream = new BufferedOutputStream(clientSocket.getOutputStream());
                    sendFile(fileItem,outStream,bufsize);
                    outStream.flush();
                    outStream.close();
                    clientSocket.close();
                }catch (Exception e) { e.printStackTrace();}
            }
        });
        thread.start();
    }

    public static void RecvOneFile(int port) throws IOException {
        Log.d(TAG,"recv");
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    ServerSocket serverSocket = new ServerSocket(port);
                    byte[] bytes = new byte[bufsize];
                    Socket client = serverSocket.accept();
                    final BufferedInputStream inStream = new BufferedInputStream(client.getInputStream());
                    recvFile(inStream,bufsize);
                    inStream.close();
                }catch (Exception e) { e.printStackTrace();}
            }
        });
        thread.start();
    }

    private static void recvFile(final BufferedInputStream inStreamSocket, int bufsize) throws IOException {
        byte[] bytes = new byte[bufsize];
        int count;
        count = inStreamSocket.read(bytes);
        String fileName = new String(bytes,StandardCharsets.UTF_8);
        if(fileName == null){
            fileName = "";
        }
        final BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(storageDirectory + "/"+ fileName));
        while((count = inStreamSocket.read(bytes)) > 0){
            outStream.write(bytes,0,count);
        }
        outStream.close();
    }

    private static void sendFile(FileItem fileItem,final  BufferedOutputStream outStreamSocket,int bufsize) throws IOException {
        int count;
        String fileName = fileItem.getExtension();
        byte[]bytes;
        bytes = fileName.getBytes();

        outStreamSocket.write(bytes,0,bytes.length);

        bytes = new byte[bufsize];
        final BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(fileItem.getPath()));
        while((count = inStream.read(bytes)) > 0){
            outStreamSocket.write(bytes,0,count);
        }

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

}
