package com.example.ronaldofs.cosensor;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainScreen extends MenuActivity {

    private static int actualPPMValue = 2;
    private static int thresholdValue = 3;
    TextView actualPPM;
    TextView threshold;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        actualPPM = (TextView)findViewById(R.id.actualPPM);
        threshold = (TextView)findViewById(R.id.thresholdValue);
        actualPPM.setText("" + actualPPMValue);
        threshold.setText("" + thresholdValue);

//        Menu menu = new Menu(this);

        Button goToMaps = (Button)findViewById(R.id.goToMaps);
        goToMaps.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i =  new Intent(getApplicationContext(), GoogleMaps.class);
                startActivity(i);
            }
        });

        handler = new Handler();
        handler.post(updater);
//
    }
    boolean clickedMapsButton = true;

    public Runnable updater = new Runnable() {
        public void run() {
            mainLoop();
            if (clickedMapsButton){
                handler.postDelayed(this, 1000);
            }
        }
    };

    private void mainLoop() {
        actualPPM.setText("" + actualPPMValue);
        threshold.setText("" + thresholdValue);

        boolean clicked = false;
        if(actualPPMValue > thresholdValue){
            actualPPM.setTextColor(Color.RED);
            alertUser();

        }
        else if (actualPPMValue > 0.8 * thresholdValue){
            actualPPM.setTextColor(Color.YELLOW);
            alertUser();

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


    public static void setThreshold(int value){
        thresholdValue = value;
    }

    public static void setActualPPM(int value){
        actualPPMValue = value;
    }

}
