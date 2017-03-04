package com.behave.behave.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.behave.behave.R;


/* Created by Irish Marquez */


public class ParentRedeemPageAllowRedemption extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "myMessage";

    private String childName;   // stores child's name from ParentRedeemPageListChild
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "PRAllowRedemption.onCreate_begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_redeem_page_allow_redemption);

        /* The following code creates an intent to receive child's name
            from ParentRedeemPageListChild and display it
            on ParentRedeemPageAllowRedemption in textview. */
        Intent childNameIntent = getIntent();
        childName = childNameIntent.getStringExtra("childName");
        TextView tv_childName = (TextView) findViewById(R.id.tv_child_name);
        tv_childName.setText(childName);

        //Creates a button listener for APPROVE NOW button
        Button btn_approve = (Button) findViewById(R.id.btn_approve_now);

        /* setOnClickListener cannot be applied to btn_approve so
             add "implements to View.OnClickListener" first to class declaration.
             onClick() method is also required to be implemented. After that,
             then this should work */
        btn_approve.setOnClickListener(ParentRedeemPageAllowRedemption.this);


        /* The following code allows the passing of childName from
           ParentRedeemPageAllowRedemption to ParentRedeemPageSuccess. */

        Log.i(TAG, "PRAllowRedemption.onCreate_end");
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "PRAllowRedemption.onClick_begin");
        Intent gotoSuccessIntent = new Intent(this, ParentRedeemPageSuccess.class);
        startActivity(gotoSuccessIntent);
        Log.i(TAG, "PRAllowRedemption.onClick_end");                                                                            //<------------
    }


    @Override
    protected void onStart() {
        Log.i(TAG, "PRAllowRedemption.onStart_begin");
        super.onStart();
                                                                                             //<------------
        //readChildren();         //gets children from database
        Log.i(TAG, "PRAllowRedemption.onStart_end");
    }


    // Takes the user to ParentRedeemPageSuccess activity                                   //<------------ may need to delete later
    public void goToSuccessActivity(View view) {
        Log.i(TAG, "PRAllowRedemption.goToSuccessActivity_begin");
        Intent gotoSuccessIntent = new Intent(this, ParentRedeemPageSuccess.class);
        startActivity(gotoSuccessIntent);
        Log.i(TAG, "PRAllowRedemption.goToSuccessActivity_end");
    }

    // Takes the user back to HomeParentActivity
    public void goToHomePage(View view) {
        Log.i(TAG, "PRAllowRedemption.goToHomePage_begin");
        switch()
        Intent goToHomeIntent = new Intent(this, HomeParentActivity.class);
        startActivity(goToHomeIntent);
        Log.i(TAG, "PRAllowRedemption.goToHomePage_end");
    }

    //Creates Overflow Menu
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.parent_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //What happens when user clicks on an item in overflow menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mHome:
                Intent homeIntent = new Intent(this, HomeParentActivity.class);
                this.startActivity(homeIntent);
                break;
            case R.id.mRedeemNotification:
                Intent redeemIntent = new Intent(this, ParentRedeemPageListChild.class);
                this.startActivity(redeemIntent);
                break;
            case R.id.mSetting:
                Intent settingIntent = new Intent(this, ParentSettings.class);
                this.startActivity(settingIntent);
                break;
            case R.id.mAbout:
                Intent aboutIntent = new Intent(this, MenuAbout.class);
                this.startActivity(aboutIntent);
                break;
            case R.id.mLogOut:
                LoginActivity.clearUsername();
                Intent logoutIntent = new Intent(this, MainActivity.class);
                this.startActivity(logoutIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
