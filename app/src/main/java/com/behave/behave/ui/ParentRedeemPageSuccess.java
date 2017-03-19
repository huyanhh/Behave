package com.behave.behave.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.behave.behave.R;
import com.google.firebase.database.DatabaseReference;


/* Created by Irish Marquez */

public class ParentRedeemPageSuccess extends AppCompatActivity {


    DatabaseReference mChildTokenCtRef;         // db reference to child's token count
    DatabaseReference mChildPrizeAmountRef;     // db reference to child's prize amount

    /* Stores child's name and uid from ParentRedeemPageAllowRedemption.
        childId will be needed for updating child's tokens */
    private String childName;
    //private String childId;     // random key assigned by Firebase to child
    private String newTokenStr;
    Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Success");
        setContentView(R.layout.activity_parent_redeem_page_success);

        /* The following code creates an intent to receive child's name and childUID
           from ParentRedeemPageAllowRedemption */
        Intent childInfoIntent = getIntent();
        childName = childInfoIntent.getStringExtra("childName");
        newTokenStr = childInfoIntent.getStringExtra("newTokenStr");

        // Displays child's name
        final TextView TV_CHILDNAME = (TextView) findViewById(R.id.tv_child_name);
        TV_CHILDNAME.setText("Success! " + childName + " now has " + newTokenStr + " token(s) left.");

        okButton = (Button) findViewById(R.id.btn_OK);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentRedeemPageSuccess.this, ParentRedeemPageListChild.class);
                startActivity(intent);
            }
        });

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


    public static class ParentEditChildListChild extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_parent_edit_child_list_child);
        }
    }
}
