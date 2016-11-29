package com.example.ronaldofs.cosensor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by ronaldofs on 28/11/16.
 */

public class BluetoothManager {
    boolean connectSuccess = true;
    BluetoothAdapter mBluetoothAdapter;
    //    Set<BluetoothDevice> devicesFound;
//    final int BLUETOOTH_ENABLED_REQUEST_CODE = 1;
//    ArrayAdapter devicesAdapter;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;

    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    final int CONNECTION_RESULT_OK = 1;

    final int CONNECTION_RESULT_FAILED = -1;


    public BluetoothAdapter getmBluetoothAdapter() {
        return mBluetoothAdapter;
    }


    BluetoothManager(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public Set<BluetoothDevice> getBondedDevices() {
        Set<BluetoothDevice> bondedSet = mBluetoothAdapter.getBondedDevices();
        return bondedSet;

    }

    public int connectToDevice(String deviceAddress) {
        if(btSocket != null){
            try {
                btSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ConnectBT bt = new ConnectBT(deviceAddress);
        bt.execute();
        return (connectSuccess) ? CONNECTION_RESULT_OK : CONNECTION_RESULT_FAILED;
    }

    public int getActualPPM() {
        int actualPPM = 0;
        byte[] bytes = new byte[4];
        try {
            actualPPM = btSocket.getInputStream().read(bytes);

            System.out.println("could not get ppm");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return actualPPM;
    }

    public void setThreshold(int thresholdValue) {
        try {
            btSocket.getOutputStream().write(thresholdValue);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("could not send threshold");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

    class ConnectBT extends AsyncTask<Void, Void, Void> {
        String address = "";
        ConnectBT(String a){
            address = a;
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if(btSocket == null || !isBtConnected){
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    btSocket = device.createInsecureRfcommSocketToServiceRecord(myUUID);
                    btSocket.connect();
                }
            }
            catch (IOException e) {
                System.out.print("erro aqui");
                e.printStackTrace();
                connectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!connectSuccess){
            }
            else{
                isBtConnected = true;
            }
        }
    }

}
