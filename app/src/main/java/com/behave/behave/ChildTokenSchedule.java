package com.behave.behave;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Calvin on 2/17/2017.
 */

public class ChildTokenSchedule extends AppCompatActivity {

    private static int amountOfToken = 0;
    private static int amountOfTime = 0;
    private static String amountOfunit = "";
    private Spinner sTokenEnter;
    private Spinner sTime;
    private Spinner sTimeUnits;
    private Button bSetAutoToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_token_schedule);

        sTokenEnter = (Spinner) findViewById(R.id.sTokens);
        sTime = (Spinner) findViewById(R.id.sUnits);
        sTimeUnits = (Spinner) findViewById(R.id.sTimeUnits);
        bSetAutoToken = (Button) findViewById(R.id.bSetAutoTokenOK);

        final Integer tokenEntries[] = {0, 1, 2, 3, 4, 5};
        final Integer timeEntries[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        final String timeUnits[] = {"Hours", "Days", "Weeks", "Months"};


        ArrayAdapter<Integer> tokenAdapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, tokenEntries);
        tokenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<Integer> timeAdapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, timeEntries);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> timeUnitAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, timeUnits);
        timeUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sTokenEnter.setAdapter(tokenAdapter);
        sTime.setAdapter(timeAdapter);
        sTimeUnits.setAdapter(timeUnitAdapter);

        sTokenEnter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                amountOfToken = (int) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        sTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                amountOfTime = (int) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        sTimeUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                amountOfunit = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        assert bSetAutoToken != null;
        bSetAutoToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                //do something
                AlertDialog.Builder Alert = new AlertDialog.Builder(ChildTokenSchedule.this);
                Alert.setMessage("Auto-Token set for " + "Child");
                Alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent setUpReward = new Intent(ChildTokenSchedule.this, SetUpReward.class);
                        ChildTokenSchedule.this.startActivity(setUpReward);
                    }
                });
                Alert.create().show();
            }
        });

    }
}
