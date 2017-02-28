package com.behave.behave.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.behave.behave.ui.ChildRedeemPage;
import com.behave.behave.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.behave.behave.utils.Constants;
import com.google.firebase.database.ValueEventListener;

public class HomeChildrenPage extends AppCompatActivity {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mKidRef = mRootRef.child(Constants.CHILDREN_CHILD); // creates `-/children` in db
    TextView tvTokenAmount;
    TextView tvGreetings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_children_page);

        // Example of a call to a native method
        //  TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());
        tvTokenAmount = (TextView) findViewById(R.id.tokenAmount);
        tvGreetings = (TextView) findViewById(R.id.textView3);
        // TODO:- Calvin pass in the data from the first view and put it here
        mKidRef.child("a6a3-60611b13ed9c").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvTokenAmount.setText(dataSnapshot.child("tokens").getValue().toString() + " tokens");
                tvGreetings.setText("Hi " + dataSnapshot.child("name").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void redeemPrize(View view){
        Intent startNewActivity = new Intent (this,ChildRedeemPage.class);
        startActivity(startNewActivity);
    }

}
