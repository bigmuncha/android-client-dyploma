package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.container.Pair;
import com.example.myapplication.filemanager.FileItem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.UUID;

public class FileTransfer {
    private static int bufsize = 131072;
    private static final String TAG = "Transfer";
    public static int TRANSFER_PORT = 9999;
    public static final String storageDirectory = "/storage/emulated/0/OmarAppFolder";
    public static int getTransferPort(){
        return TRANSFER_PORT;
    }

    public static void SendMultipleFiles(String ip, int port, HashMap<String,FileItem> mapa){
        Log.d(TAG,"send");
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    byte[] bytes = new byte[bufsize];
                    Socket clientSocket = new Socket(ip,port);
                    Log.d(TAG,"good Connect");
                    DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                    dataOutputStream.writeInt(mapa.size());
                    dataOutputStream.flush();
                    for (FileItem fItem: mapa.values()) {
                        sendFile(fItem,clientSocket,bufsize);
                        Log.d(TAG,"send one file");
                    }

                    clientSocket.close();
                }catch (Exception e) { e.printStackTrace();}
            }
        });
        thread.start();
    }

    public static void RecvMultipleFiles(int port, String folderPath,Context context) throws IOException {

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                try{

                    ServerSocket serverSocket = new ServerSocket(port);
                    byte[] bytes = new byte[bufsize];
                    Socket client = serverSocket.accept();
                    DataInputStream dataInputStream = new DataInputStream(client.getInputStream());
                    int countFiles = dataInputStream.readInt();

                    for (int i = 0; i < countFiles; i++) {
                        recvFile(client,bufsize);
                        Log.d(TAG,"recv " + String.valueOf(i) + " file");
                    }

                    client.close();
                    serverSocket.close();
                }catch (Exception e) { e.printStackTrace();}
            }
        });
        thread.start();
    }


    private static void recvFile(Socket socket, int bufsize) throws IOException {
        long fileSize;
        String extension;
        String response;

        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        response = dataInputStream.readUTF();

        Pair<String, Long> para = getResponse(response);
        extension = para.getLeft();
        fileSize = para.getRight();
        byte[] buffer = new byte[bufsize];
        int count =0;
        long sum = 0;

        String PathToFile = storageDirectory + "/" + UUID.randomUUID().toString() + "." +extension;
        Log.d(TAG,PathToFile);

        InputStream inputStream = socket.getInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream(PathToFile);

        while(sum < fileSize){
            long temp;
            if(fileSize - sum > bufsize){
                temp = bufsize;
            }else{
                temp = fileSize - sum;
            }
            count = inputStream.read(buffer,0,(int) temp);
            sum+=count;
            fileOutputStream.write(buffer,0,count);
            fileOutputStream.flush();
        }
        fileOutputStream.close();
        Log.d(TAG, "complete");
    }

    private static Pair<String, Long> getResponse(String response) {
        int i = response.lastIndexOf('/');


        String ext = response.substring(0,i);

        Long size = Long.parseLong(response.substring(i+1));
        //System.out.print(ext + "  "+  size + '\n');
        Pair<String,Long> pair = new Pair<String,Long>(ext,size);
        return pair;

    }

    private static void makeToast(Context context){
        Toast.makeText(context,"Recv",Toast.LENGTH_SHORT).show();
    }

    private static void sendFile(FileItem fileItem,Socket socket,int bufsize) throws IOException {

        String fileExtension = fileItem.getExtension();
        long fileSize = fileItem.getSize();

        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        String result_message = fileExtension + "/" + String.valueOf(fileSize);
        dataOutputStream.writeUTF(result_message);

        Log.d(TAG, result_message);


        FileInputStream fileInputStream = new FileInputStream(fileItem.getPath());
        OutputStream outputStream = socket.getOutputStream();

        byte[] buffer = new byte[bufsize];
        int count;
        int sum =0;

        while ((count = fileInputStream.read(buffer,0,bufsize)) > 0){
            sum +=count;
            outputStream.write(buffer,0,count);
            outputStream.flush();
        }
        fileInputStream.close();
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
