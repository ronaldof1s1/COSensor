package com.example.ronaldofs.cosensor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()){
            case R.id.changeThreshold:
                i = new Intent(getBaseContext() , ChangeThreshold.class);
                startActivity(i);
                return true;
            case R.id.help:
                return true;
            case R.id.manageBluetooth:
                i = new Intent(getBaseContext(), BluetoothActivity.class);
                startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }

    }


}
