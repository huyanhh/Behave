package com.behave.behave.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.behave.behave.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/* Created by Irish Marquez */


public class ParentRedeemPageAllowRedemption extends AppCompatActivity implements View.OnClickListener {



    private String childName;   // stores child's name from ParentRedeemPageListChild
    public String childId;
    private String prize;
    private String amount;
    private String tokens;
    private ArrayAdapter<String> adapter;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mKidRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Allow Redeem");
        setContentView(R.layout.activity_parent_redeem_page_allow_redemption);

        /* The following code creates an intent to receive child's name and uid
            from ParentRedeemPageListChild and display it
            on ParentRedeemPageAllowRedemption in textview. */
        Intent childNameIntent = getIntent();
        childName = childNameIntent.getStringExtra("childName");
        childId   = childNameIntent.getStringExtra("childId");
        prize     = childNameIntent.getStringExtra("prize");
        amount    = childNameIntent.getStringExtra("amount");
        tokens     = childNameIntent.getStringExtra("tokens");

        final TextView tv_childName = (TextView) findViewById(R.id.tv_child_name);
        tv_childName.setText("Allow " + childName + " to redeem " + prize +
                            " for " + amount + " token(s)?");


        //Creates a button listener for APPROVE NOW button
        Button btn_approve = (Button) findViewById(R.id.btn_approve_now);
        /* setOnClickListener cannot be applied to btn_approve so
             add "implements to View.OnClickListener" first to class declaration.
             onClick() method is also required to be implemented. After that,
             then setOnClickListener should work */
        //btn_approve.setOnClickListener(ParentRedeemPageAllowRedemption.this);
        btn_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int newTokens = Integer.parseInt(tokens) - Integer.parseInt(amount);  // token -= amount
                mKidRef =  mRootRef.child("children").child(childId);
                mKidRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mKidRef.child("tokens").setValue(newTokens);    // update token count
                        mKidRef.child("isRedeeming").setValue(false);   // set isRedeeming to false
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Intent goToSuccessScreen = new Intent
                        (ParentRedeemPageAllowRedemption.this, ParentRedeemPageSuccess.class);
                goToSuccessScreen.putExtra("childName", childName);
                String newTokenStr = String.valueOf(newTokens);
                goToSuccessScreen.putExtra("newTokenStr", newTokenStr);
                startActivity(goToSuccessScreen);
            }
        });


        Button btn_maybe_later = (Button) findViewById(R.id.btn_maybe_later);
        btn_maybe_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToHomeIntent = new Intent(ParentRedeemPageAllowRedemption.this, ParentRedeemPageListChild.class);
                startActivity(goToHomeIntent);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    public void onClick(View view) {

    }
}
