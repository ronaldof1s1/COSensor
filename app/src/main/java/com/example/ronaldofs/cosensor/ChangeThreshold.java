package com.example.ronaldofs.cosensor;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
                manualClick();
            }
        });
        Button assisted = (Button) findViewById(R.id.assistedButton);
        assisted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChangeThreshold.this, "YOU CLICKED ON assisted", Toast.LENGTH_SHORT).show();
                assistedClick();
            }
        });
    }

    public void manualClick(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Manual");
        dialog.setMessage("Enter PPM value");
        final EditText input = new EditText(ChangeThreshold.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        dialog.setView(input);
        dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int value = Integer.parseInt(input.getText().toString());
                MainScreen.setThreshold(value);
                Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    public void assistedClick(){

    }
}
