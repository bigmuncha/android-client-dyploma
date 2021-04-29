package com.example.myapplication.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.FileTransfer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothConnectivity {
    BluetoothAdapter mAdapter;
    private static final String TAG = "BluetoothConnectivity";

    private static final UUID MY_UUID=
            UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private  AcceptThread acceptThread;
    private  ConnectThread connectThread;
    Context mContext;
    public  BluetoothConnectivity(Context context){
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mContext = context;
    }

    public void connectToDevice(BluetoothDevice device){
        connectThread = new ConnectThread(device);
        connectThread.run();
    }

    public void acceptDevice(){
        acceptThread = new AcceptThread();
        acceptThread.run();
    }

    private class AcceptThread extends Thread{
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread(){
            BluetoothServerSocket tmp = null;

            try {
                tmp = mAdapter.listenUsingInsecureRfcommWithServiceRecord(
                        mAdapter.getName(),MY_UUID);
            } catch (IOException e){
                Log.d(TAG, e.toString());
            }
            mmServerSocket = tmp;
        }

        public void run(){
            BluetoothSocket socket= null;

            while (true){
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.d(TAG, e.toString());
                    break;
                }
                if(socket != null){
                    ConnectedThread connectedThread = new ConnectedThread(socket);
                    connectedThread.run();
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {

                    }
                    break;
                }
            }
        }
        public void cancel(){
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.d(TAG, e.toString());
            }
        }
    }

    private class ConnectThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device){
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.d(TAG, e.toString());
            }
            mmSocket = tmp;
        }

        public void run(){
            mAdapter.cancelDiscovery();

            try {
                mmSocket.connect();
            } catch (IOException e) {
                try{
                    mmSocket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                return;
            }
            ConnectedThread connectedThread = new ConnectedThread(mmSocket);
            String data = "Omar send msg";
            byte[] buffer = data.getBytes();
            connectedThread.write(buffer);
        }

        public void cancel(){
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private class ConnectedThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket){
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;

            while (true){
                try {
                    bytes = mmInStream.read(buffer);
                    Toast.makeText(mContext, FileTransfer.requestParser(buffer),Toast.LENGTH_SHORT).show();
                    Log.d("RECV BLUETOOTH", FileTransfer.requestParser(buffer));
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
        public void write(byte[] bytes){
            try {
                mmOutStream.write(bytes);
                Toast.makeText(mContext, FileTransfer.requestParser(bytes), Toast.LENGTH_SHORT).show();
                Log.d("SEND BLUETOOTH", FileTransfer.requestParser(bytes) + "   ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void cancel(){
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
