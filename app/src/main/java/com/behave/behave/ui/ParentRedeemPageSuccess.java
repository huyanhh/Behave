package com.behave.behave.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.behave.behave.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/* Created by Irish Marquez */

public class ParentRedeemPageSuccess extends AppCompatActivity {


    DatabaseReference mChildTokenCtRef;         // db reference to child's token count
    DatabaseReference mChildPrizeAmountRef;     // db reference to child's prize amount

    /* Stores child's name and uid from ParentRedeemPageAllowRedemption.
        childId will be needed for updating child's tokens */
    private String childName;
    private String childId;     // random key assigned by Firebase to child

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Success");
        setContentView(R.layout.activity_parent_redeem_page_success);

        /* The following code creates an intent to receive child's name and childUID
           from ParentRedeemPageAllowRedemption */
        Intent childInfoIntent = getIntent();
        childName = childInfoIntent.getStringExtra("childName");
        childId = childInfoIntent.getStringExtra("childId");


        // Displays child's name
        final TextView TV_CHILDNAME = (TextView) findViewById(R.id.tv_child_name);
        TV_CHILDNAME.setText(childName + " now has ");

        //Retrieves child's tokens
        final TextView TV_TOKENCOUNT = (TextView) findViewById(R.id.tv_token_count);
        mChildTokenCtRef = FirebaseDatabase.getInstance().getReference().child("children").child(childId).child("tokens");
        mChildTokenCtRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tokenCt = dataSnapshot.getValue().toString();
//                TV_TOKENCOUNT.setText(tokenCt + " token(s) left.");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // Retrieves child's prize amount
        mChildPrizeAmountRef = FirebaseDatabase.getInstance().getReference().child("children").child(childId).child("prizes");




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
