package com.example.myapplication.bluetooth;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MyAdapter;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BluetoothListActivity extends AppCompatActivity implements MyAdapter.ItemClickListener{

    private static final int REQUEST_ENABLE_BT = 1;

    private static final String TAG = "BluetoothListActivity";

    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    private BluetoothManager bluetoothManager;
    private BluetoothLeScanner bluetoothLeScanner;
    private BluetoothAdapter bluetoothAdapter = null;
    private BroadcastReceiver receiver;
    private boolean scanning;
    private Handler handler =new Handler();

    MyAdapter adapter;
    ArrayList<String> stringres;
    private HashMap<String,BluetoothDevice> mapa;
    private BluetoothConnectivity bluetoothConnectivity;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        Log.d("BLE","create");
        bluetoothManager = getSystemService(BluetoothManager.class);
        if(bluetoothManager != null){
            bluetoothAdapter = bluetoothManager.getAdapter();
        }

        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        stringres = new ArrayList<>();
        mapa = new HashMap<>();
       bluetoothConnectivity = new BluetoothConnectivity(getApplicationContext());
        bluetoothScanning();
    }
    public void bluetoothScanning()
    {
        //turnOffBluetooth();
        Log.d("BLUETOOTH", "here");
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getApplicationContext().registerReceiver(mReceiver, filter);
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.startDiscovery();
    }

    public void turnOnBluetooth(){
        Intent intent = new Intent(bluetoothAdapter.ACTION_REQUEST_ENABLE);
        if(!bluetoothAdapter.isEnabled()) {
            startActivityForResult(intent, REQUEST_ENABLE_BT);
            bluetoothAdapter.disable();
        }
    }
    public void turnOffBluetooth(){
        if(bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
        }
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device!= null && device.getBondState() != BluetoothDevice.BOND_BONDED){
                    String deviceName = device.getName();
                    String deviceHardwareAddress = device.getAddress(); // MAC address
                    stringres.add(deviceName + '\n' + deviceHardwareAddress);
                    mapa.put(deviceHardwareAddress,device);
                    Log.i("Device Name: " , "device " + deviceName);
                    Log.i("deviceHardwareAddress " , "hard "  + deviceHardwareAddress);
                    fillAdapter();
                }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    fillAdapter();
                }
            }
        }
    };



    private void fillAdapter(){
        RecyclerView recyclerView = findViewById(R.id.bluetoothList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(this,stringres);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        bluetoothAdapter.cancelDiscovery();
        String info = adapter.getItem(position);
        String address = info.substring(info.length() - 17);
        bluetoothConnectivity.connectToDevice(mapa.get(address));
        Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
        finish();
    }
}