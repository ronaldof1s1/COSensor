package com.example.ronaldofs.cosensor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AssistedChangeThreshold extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assisted_change_threshold);

        Button okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ppmValue = calculatePpmValue();
                MainScreen.setThreshold(ppmValue);
                Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }
        });

        Spinner fuelTypes = (Spinner) findViewById(R.id.fuelTypeValue);
        ArrayAdapter<CharSequence> fuelTypesArray = ArrayAdapter
                .createFromResource(this, R.array.fuelTypesArray, android.R.layout.simple_spinner_item);
        fuelTypesArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fuelTypes.setAdapter(fuelTypesArray);
    }

    private int calculatePpmValue() {
        EditText carYearET = (EditText) findViewById(R.id.CarYearValue);

        int carYearValue = Integer.parseInt(carYearET.getText().toString());
        int yearValue;

        Spinner fuelSpinner = (Spinner) findViewById(R.id.fuelTypeValue);
        String fuelTypeName = fuelSpinner.getSelectedItem().toString();
        int fuelValue;

        int actualPPM = 0;

        yearValue = getYearValue(carYearValue);
        fuelValue = getFuelValue(fuelTypeName);

        actualPPM = calculate(yearValue, fuelValue);

        return actualPPM;
    }

    private int calculate(int yearValue, int fuelValue) {
        int actualPPM = -1;
        switch (yearValue){
            case 0:
                switch (fuelValue){
                    case 0:
                        actualPPM = 60000;
                        break;
                    case 1:
                        actualPPM = 60000;
                        break;
                    case 2:
                        actualPPM = 60000;
                        break;
                    default:
                        break;
                }
                break;
            case 1:
                switch (fuelValue){
                    case 0:
                        actualPPM = 50000;
                        break;
                    case 1:
                        actualPPM = 50000;
                        break;
                    case 2:
                        actualPPM = 50000;
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                switch (fuelValue){
                    case 0:
                        actualPPM = 40000;
                        break;
                    case 1:
                        actualPPM = 40000;
                        break;
                    case 2:
                        actualPPM = 40000;
                        break;
                    default:
                        break;
                }
                break;
            case 3:
                switch (fuelValue){
                    case 0:
                        actualPPM = 35000;
                        break;
                    case 1:
                        actualPPM = 35000;
                        break;
                    case 2:
                        actualPPM = 35000;
                        break;
                    default:
                        break;
                }
                break;
            case 4:
                switch (fuelValue){
                    case 0:
                        actualPPM = 30000;
                        break;
                    case 1:
                        actualPPM = 30000;
                        break;
                    case 2:
                        actualPPM = 30000;
                        break;
                    default:
                        break;
                }
                break;
            case 5:
                switch (fuelValue){
                    case 0:
                        actualPPM = 10000;
                        break;
                    case 1:
                        actualPPM = 10000;
                        break;
                    case 2:
                        actualPPM = 10000;
                        break;
                    default:
                        break;
                }
                break;
            case 6:
                switch (fuelValue){
                    case 0:
                        actualPPM = 5000;
                        break;
                    case 1:
                        actualPPM = 5000;
                        break;
                    case 2:
                        actualPPM = 10000;
                        break;
                    default:
                        break;
                }
                break;
            case 7:
                switch (fuelValue){
                    case 0:
                        actualPPM = 3000;
                        break;
                    case 1:
                        actualPPM = 3000;
                        break;
                    case 2:
                        actualPPM = 10000;
                        break;
                    default:
                        break;
                }
                break;
            default:
                actualPPM = -1;
                break;
        }
        return actualPPM;
    }

    public int getYearValue(int carYearValue){
        int yearValue;

        if(carYearValue <= 1979){
            yearValue = 0;
        }
        else if(carYearValue >= 1980 && carYearValue <= 1988 ){
            yearValue = 1;
        }
        else if(carYearValue == 1989){
            yearValue = 2;
        }
        else if(carYearValue == 1990 || carYearValue == 1991){
            yearValue = 3;
        }
        else if (carYearValue >= 1992 && carYearValue <= 1996){
            yearValue = 4;
        }
        else if (carYearValue >= 1996 && carYearValue <= 2002){
            yearValue = 5;
        }
        else if (carYearValue >= 2003 && carYearValue <= 2005){
            yearValue = 6;
        }
        else{
            yearValue = 7;
        }

        return yearValue;
    }

    public int getFuelValue(String fuelTypeName){
        int fuelValue;
        if (fuelTypeName.equals("Gasoline")){
            fuelValue = 0;
        }
        else if(fuelTypeName.equals("Alcohool")){
            fuelValue = 1;
        }
        else{
            fuelValue = 2;
        }
        return fuelValue;
    }
}
