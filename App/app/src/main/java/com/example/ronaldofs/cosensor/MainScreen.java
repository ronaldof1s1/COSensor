package com.example.ronaldofs.cosensor;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreen extends MenuActivity {

    private static int actualPPMValue = 50;
    private static int thresholdValue = 100;
    static TextView actualPPM;
    static TextView threshold;
    private Handler handler;
    final int BLUETOOTH_ENABLED_REQUEST_CODE = 1;
    BluetoothAdapter mBluetoothAdapter;
    static BluetoothManager btManager;
    static boolean isConnected = false;
    private static boolean thresholdHasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        actualPPM = (TextView)findViewById(R.id.actualPPM);
        threshold = (TextView)findViewById(R.id.thresholdValue);
        actualPPM.setText("" + actualPPMValue);
        threshold.setText("" + thresholdValue);

        Button goToMaps = (Button)findViewById(R.id.goToMaps);
        goToMaps.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i =  new Intent(getApplicationContext(), GoogleMaps.class);
                startActivity(i);
            }
        });

        btManager = new BluetoothManager();
        startBluetooth();

        startLoop();

    }

    private void startLoop() {
        handler = new Handler();
        handler.post(updater);
    }


    private void startBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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

    }

    boolean clickedMapsButton = true;

    public Runnable updater = new Runnable() {
        public void run() {
            mainLoop();
            handler.postDelayed(this, 1000 * 10);

        }
    };

    private void mainLoop() {
        if (btManager.btSocket != null){
            if(btManager.btSocket.isConnected()){
                setActualPPM(btManager.getActualPPM());
                if (thresholdHasChanged){
                    btManager.setThreshold(thresholdValue);
                    thresholdHasChanged = false;
                }
            }
            else{
//                Toast.makeText(this, "bluetooth disconnected", Toast.LENGTH_SHORT).show();
            }
        }

        if(actualPPMValue > thresholdValue){
            actualPPM.setTextColor(Color.RED);
            if (clickedMapsButton) {
                synchronized (this) {
                    alertUser();
                }
            }
        }
        else if (actualPPMValue > 0.8 * thresholdValue){
            actualPPM.setTextColor(Color.YELLOW);
            if (clickedMapsButton) {
                synchronized (this) {
                    alertUser();
                }
            }
        }
        else{
            actualPPM.setTextColor(Color.GREEN);
        }
    }

    private void alertUser() {
        clickedMapsButton = false;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("ALERT");
        alertDialog.setMessage("CO EMISSION ABOVE OR NEAR THRESHOLD");
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setNeutralButton("Search for car Workshops", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(), GoogleMaps.class);
                startActivity(intent);
                clickedMapsButton = true;
                handler.post(updater);
            }
        });
        alertDialog.show();
    }


    synchronized public static void setThreshold(int value){
        thresholdValue = (value <= 60000 && value > 0) ? value : 60000;
        threshold.setText(thresholdValue + "");
        thresholdHasChanged = true;

    }

    synchronized public static void setActualPPM(int value){
        actualPPMValue = (value > 0) ? value : actualPPMValue;
        actualPPM.setText(actualPPMValue + "");
    }

}
