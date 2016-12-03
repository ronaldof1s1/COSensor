package com.example.ronaldofs.cosensor;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
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
                manualClick();
            }
        });
        Button assisted = (Button) findViewById(R.id.assistedButton);
        assisted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialog.setView(input);
        dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Editable eValue = input.getText();
                Toast.makeText(ChangeThreshold.this, eValue.toString(), Toast.LENGTH_SHORT).show();
                if(input.getText() != null){
                    int value = Integer.parseInt(eValue.toString());
                    MainScreen.setThreshold(value);
                }
                Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
    }

    public void assistedClick(){
        Intent i = new Intent(getApplicationContext(), AssistedChangeThreshold.class);
        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
        finish();
    }
}
