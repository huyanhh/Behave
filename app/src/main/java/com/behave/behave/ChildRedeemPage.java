package com.behave.behave;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChildRedeemPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_redeem_page);
    }
    public void returnChild(View view){
        Intent startNewActivity = new Intent (this,HomeChildrenPage.class);
        startActivity(startNewActivity);


        // need token counter and prize name, as well as prize picture from database
    }
}
