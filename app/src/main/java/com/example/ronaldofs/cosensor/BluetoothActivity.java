package com.example.ronaldofs.cosensor;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothActivity extends MenuActivity {

    BluetoothAdapter mBluetoothAdapter;
    BluetoothManager btManager = MainScreen.btManager;
    Set<BluetoothDevice> devicesFound;
    final int BLUETOOTH_ENABLED_REQUEST_CODE = 1;
    ArrayAdapter devicesAdapter;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//    String deviceAddress = "";
    String deviceInfo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        devicesAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        startBluetooth();


    }

    private void startBluetooth() {
        mBluetoothAdapter = btManager.getmBluetoothAdapter();
        if (!mBluetoothAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, BLUETOOTH_ENABLED_REQUEST_CODE);
        }

        if (mBluetoothAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Not compatible")
                    .setMessage("Your phone does not support Bluetooth")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        findDevice();

    }

    private void findDevice() {
        listPairedDevices();
        Button searchButton = (Button) findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                searchDevices();
            }
        });

    }
//
//    private void searchDevices() {
//        mBluetoothAdapter.startDiscovery();
//    }
//
//    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                // Get the BluetoothDevice object from the Intent
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                // Add the name and address to an array adapter to show in a ListView
//                devicesAdapter.add(device.getName() + "\n" + device.getAddress());
//            }
//
//        }
//    }

    private void listPairedDevices() {
        final ListView deviceList = (ListView) findViewById(R.id.devicesList);

        Button searchButton;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        Set<BluetoothDevice> pairedDevices  = btManager.getBondedDevices();

        if (pairedDevices.size() > 0){
            for(BluetoothDevice device : pairedDevices){
                devicesAdapter.add(device.getName() + "\n" + device.getAddress());
            }
            deviceList.setAdapter(devicesAdapter);
//            alertDialog.setView(deviceList);
            deviceList.setOnItemClickListener(myListClickListener);
        }
        else{
            alertDialog.setMessage("No devices found paired");
            alertDialog.show();
        }
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            deviceInfo = ((TextView) view).getText().toString();
//            Toast.makeText(BluetoothActivity.this, "Clicked on device " + deviceInfo, Toast.LENGTH_SHORT).show();
            String deviceAddress = deviceInfo.substring(deviceInfo.length() - 17);
            int result = btManager.connectToDevice(deviceAddress);
            if(result == 1){
                Toast.makeText(BluetoothActivity.this, "Connected to " + deviceInfo, Toast.LENGTH_SHORT).show();
                MainScreen.isConnected = true;
                finish();
            }
            else{
                Toast.makeText(BluetoothActivity.this, "Failed to connect", Toast.LENGTH_SHORT).show();
            }
        }


    };
//
//    private void connectToDevice(String info, String address) {
//
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLUETOOTH_ENABLED_REQUEST_CODE){
            if(resultCode == RESULT_OK){

            }
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, "MUST USE BLUETOOTH", Toast.LENGTH_SHORT).show();
                System.exit(0);
            }
        }
    }

    private void disconnect(){
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();}
        }
        finish();
    }

}
