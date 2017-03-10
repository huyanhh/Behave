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
import com.behave.behave.utils.Constants;
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
    private ArrayAdapter<String> adapter;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Allow Redeem");
        setContentView(R.layout.activity_parent_redeem_page_allow_redemption);

        /* The following code creates an intent to receive child's name
            from ParentRedeemPageListChild and display it
            on ParentRedeemPageAllowRedemption in textview. */
        Intent childNameIntent = getIntent();
        childName = childNameIntent.getStringExtra("childName");
        childId = childNameIntent.getStringExtra("childId");
        final TextView tv_childName = (TextView) findViewById(R.id.tv_child_name);
   //     tv_childName.setText(childName + " wants to redeem ");

        // retrieve prize names here
        mRootRef.child(Constants.REDEEMING_CHILD).child(childId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                prize = dataSnapshot.child("children").child("prize").getValue().toString();  //dataSnapshot.getKey();

                tv_childName.setText(childName + " wants to redeem for " + prize + ".");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Creates a button listener for APPROVE NOW button
        Button btn_approve = (Button) findViewById(R.id.btn_approve_now);
        Button btn_maybe_later = (Button) findViewById(R.id.btn_maybe_later);
        /* setOnClickListener cannot be applied to btn_approve so
             add "implements to View.OnClickListener" first to class declaration.
             onClick() method is also required to be implemented. After that,
             then setOnClickListener should work */
        btn_approve.setOnClickListener(ParentRedeemPageAllowRedemption.this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.btn_approve_now:
                Intent passChildInfoIntent = new Intent (ParentRedeemPageAllowRedemption.this, ParentRedeemPageSuccess.class);
                passChildInfoIntent.putExtra("childName", childName);
                passChildInfoIntent.putExtra("childId", childId);
                startActivity(passChildInfoIntent);
                break;
            case R.id.btn_maybe_later:
                Intent goToHomeIntent = new Intent(this, HomeParentActivity.class);
                startActivity(goToHomeIntent);
                break;

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    // Takes the user to ParentRedeemPageSuccess activity
    public void goToSuccessActivity(View view) {           //<------------ may need to delete later
        Intent gotoSuccessIntent = new Intent(this, ParentRedeemPageSuccess.class);
        startActivity(gotoSuccessIntent);
    }

    // Takes the user back to HomeParentActivity
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
