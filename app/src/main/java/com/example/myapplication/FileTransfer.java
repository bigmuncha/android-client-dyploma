package com.example.myapplication;

import android.util.Log;

import com.example.myapplication.filemanager.FileItem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
    private static int TRANSFER_PORT = 9999;
    public static final String storageDirectory = "/storage/emulated/0/OmarApplication";

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

                    final BufferedOutputStream outStream = new BufferedOutputStream(clientSocket.getOutputStream());
                    bytes = longToBytes(mapa.size());
                    outStream.write(bytes,0,bytes.length);
                    outStream.flush();
                    for (FileItem fItem: mapa.values()) {
                        sendFile(fItem,outStream,bufsize);
                    }
                    outStream.flush();
                    outStream.close();
                    clientSocket.close();
                }catch (Exception e) { e.printStackTrace();}
            }
        });
        thread.start();
    }

    public static void RecvMultipleFiles(int port, String folderPath) throws IOException {

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                try{

                    ServerSocket serverSocket = new ServerSocket(port);
                    byte[] bytes = new byte[bufsize];
                    System.out.println("Wait");
                    Socket client = serverSocket.accept();
                    long start = System.currentTimeMillis();
                    System.out.println("accept normal");
                    final BufferedInputStream inStream = new BufferedInputStream(client.getInputStream());

                    inStream.read(bytes);
                    long fileAmount = bytesToLong(bytes);

                    for (int i = 0; i < fileAmount; i++) {
                        recvFile(inStream,bufsize);
                    }
                    inStream.close();
                    client.close();
                    serverSocket.close();
                    long elapsed = System.currentTimeMillis() - start;
                    System.out.print(elapsed/1000F);
                }catch (Exception e) { e.printStackTrace();}
            }
        });
        thread.start();
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

        String extension = requestParser(bytes);

        count = inStreamSocket.read(bytes);
        long sizeFile = bytesToLong(bytes);

        String fileName = storageDirectory + "/" + UUID.randomUUID().toString() + "." + extension;
        File file = new File(fileName);
        final BufferedOutputStream outStream;

        int sum=0;
        outStream = new BufferedOutputStream(new FileOutputStream(file));
        System.out.println("while");
        while (sum < sizeFile){
            count = inStreamSocket.read(bytes);
            sum+=count;
            outStream.write(bytes, 0, count);
        }
        outStream.flush();
        outStream.close();


    }

    private static void sendFile(FileItem fileItem,final  BufferedOutputStream outStreamSocket,int bufsize) throws IOException {
        int count;
        String fileExtension = fileItem.getExtension();
        long fileSize = fileItem.getSize();
        byte[]bytes;

        String result_message = fileExtension + "/" + String.valueOf(fileSize);
        bytes = result_message.getBytes();

        outStreamSocket.write(bytes,0,bytes.length);
        outStreamSocket.flush();
        Log.d(TAG, result_message);

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
