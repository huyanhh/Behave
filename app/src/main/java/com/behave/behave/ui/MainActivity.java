package com.behave.behave.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.behave.behave.ui.HomeParentActivity;
import com.behave.behave.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(SaveSharedPreference.getUserName(MainActivity.this).length() == 0)
        {
            startActivity(new Intent(this, LoginActivity.class));
        }
        //need to check what type of account based on the databse
        else
        {
            startActivity(new Intent(this, HomeParentActivity.class));
        }
//        if children account
//        {
//            //startActivity(new Intent(this, HomeChildrenPage.class));
//        }
    }
}
