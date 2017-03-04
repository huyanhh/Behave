package com.behave.behave.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.behave.behave.R;


/* Created by Irish Marquez */

public class ParentRedeemPageSuccess extends AppCompatActivity {

    private static final String TAG = "myMessage";

    private String childName;   // stores child's name from ParentRedeemPageAllowRedemption

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "PRSuccess.onCreate_begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_redeem_page_success);

        /* The following code creates an intent to receive child's name
           from ParentRedeemPageAllowRedemption and display it
           on ParentRedeemPageSuccess in textview. */
        Intent childNameIntent = getIntent();
        childName = childNameIntent.getStringExtra("childName");
        TextView tv_childName = (TextView) findViewById(R.id.tv_child_name);
        tv_childName.setText(childName + "'s token count: ");
        Log.i(TAG, "PRSuccess.onCreate_end");
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
