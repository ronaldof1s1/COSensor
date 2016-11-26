package com.example.ronaldofs.cosensor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainScreen extends MenuActivity {

    public String actualPPMValue;
    public String thresholdValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        actualPPMValue = "" + 2;
        thresholdValue = "" + 3;

        TextView actualPPM = (TextView)findViewById(R.id.actualPPM);
        TextView threshold = (TextView)findViewById(R.id.thresholdValue);
        actualPPM.setText(actualPPMValue);
        threshold.setText(thresholdValue);

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


    }


    public void changeThreshold(MenuItem item){

        Intent i = new Intent(getApplicationContext(), ChangeThreshold.class);
        startActivity(i);
    }
}
