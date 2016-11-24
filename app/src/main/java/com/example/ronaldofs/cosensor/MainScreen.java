package com.example.ronaldofs.cosensor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        TextView actualPPM = (TextView)findViewById(R.id.actualPPM);
        TextView threshold = (TextView)findViewById(R.id.thresholdValue);
        actualPPM.setText("1234");
        threshold.setText("3214");

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
}
