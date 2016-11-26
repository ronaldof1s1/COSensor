package com.example.ronaldofs.cosensor;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeThreshold extends MenuActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_threshold);
        Button manual = (Button) findViewById(R.id.manualButton);
        manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChangeThreshold.this, "YOU CLICKED ON MANUAL", Toast.LENGTH_SHORT).show();
            }
        });
        Button assisted = (Button) findViewById(R.id.assistedButton);
        assisted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChangeThreshold.this, "YOU CLICKED ON assisted", Toast.LENGTH_SHORT).show();
                manualClick();
            }
        });
    }

    public void manualClick(){
        Dialog dialog = new Dialog(this);
        dialog.setTitle("Manual");
        dialog.show();
    }
}
