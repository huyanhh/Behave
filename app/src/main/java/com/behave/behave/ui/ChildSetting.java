package com.behave.behave.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.behave.behave.R;

public class ChildSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_setting);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.item_option1:
                Intent settingsIntent = new Intent(this, ChildSetting.class);
                this.startActivity(settingsIntent);
                break;
            case R.id.item_option2:
                LoginActivity.clearUsername();
                Intent logoutIntent = new Intent(this, MainActivity.class);
                this.startActivity(logoutIntent);
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
