package com.behave.behave.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.behave.behave.R;

public class ParentRedeemPageAllowRedemption extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_redeem_page_allow_redemption);
    }


    public void goToHomePage(View view) {
        Intent goToHomeIntent = new Intent(this, HomeParentActivity.class);
        startActivity(goToHomeIntent);
    }

    //Creates Overflow Menu
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.parent_menu, menu);
        return super.onCreateOptionsMenu(menu);
        //  return true;
    }

    //Overflow menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mHome:
                Intent homeIntent = new Intent(this, HomeParentActivity.class);
                this.startActivity(homeIntent);
                break;
//            case R.id.mChild:
//                break;
            case R.id.mRedeemNotification:
                Intent redeemIntent = new Intent(this, ParentRedeemPageListChild.class);
                this.startActivity(redeemIntent);
                break;
//            case R.id.mPrizeList:
//                break;
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
