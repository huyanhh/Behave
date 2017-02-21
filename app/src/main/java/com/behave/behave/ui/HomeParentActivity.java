package com.behave.behave.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.behave.behave.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by huyanh on 2017. 2. 6..
 */

public class HomeParentActivity extends AppCompatActivity {

    private Button mChildButton;
    private Button mAddChild;
    private Button mAddChild2;
    private Button mPrizeList;

    // when we get a reference it gets us a ref to the root of the json ref tree
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mKidRef = mRootRef.child("children").child("child1"); // creates `-/children` in db

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_parent_page);

        mChildButton = (Button) findViewById(R.id.button1);
        mPrizeList = (Button) findViewById(R.id.prizeList);
        mAddChild = (Button) findViewById(R.id.button2);
        mAddChild2 = (Button) findViewById(R.id.button3);
    }

    //whenever condition in the database changes we want to also update child
    //so in this case if we add a child we just change the name from child 1 to
    //the kids name just as a small example
    @Override
    protected void onStart() {
        super.onStart();
        //create value listener here
        //if another kid is added to the kid tree, listen and add another button
        mKidRef.child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                mChildButton.setText(text);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKidRef.child("name").setValue("little irish");
            }
        });

        mAddChild2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKidRef.child("name").setValue("little huyanh");
            }
        });

        mPrizeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RewardIntent = new Intent(HomeParentActivity.this, SetUpReward.class);
                HomeParentActivity.this.startActivity(RewardIntent);
            }
        });
    }
}
