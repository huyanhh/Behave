package com.behave.behave.ui;

import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.behave.behave.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.behave.behave.utils.Constants;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeChildrenPage extends AppCompatActivity {


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mKidRef = mRootRef.child(Constants.CHILDREN_CHILD); // creates `-/children` in db
    String childId;
    ArrayList<Pair<String, Integer>> prizes;
    TextView tvTokenAmount;
    TextView tvGreetings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_children_page);

        childId = "a6a3-60611b13ed9c";
        Intent childNameIntent = getIntent();
        childId = childNameIntent.getStringExtra("childId");
        // Example of a call to a native method
        //  TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());
        tvTokenAmount = (TextView) findViewById(R.id.tokenAmount);
        tvGreetings = (TextView) findViewById(R.id.textView3);
        // TODO:- Calvin pass in the data from the first view and put it here
        mKidRef.child(childId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvTokenAmount.setText(dataSnapshot.child("tokens").getValue().toString() + " tokens");
                tvGreetings.setText("Hi " + dataSnapshot.child("name").getValue(String.class));
                for (DataSnapshot prize: dataSnapshot.child(Constants.PRIZES_CHILD).getChildren())
                    prizes.add(new Pair(prize.getKey(), prize.getValue(Integer.class)));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
   // @Override
    public void redeemPrize (View view){
        // TODO: this stuff
//        mRootRef.child(Constants.REDEEMING_CHILD).child(childId).
        // pick whichever prize is in the prize list then pass it to the parent
        // first check if prize and token count are valid
        String prize = "coolio prize here ok jimmy you take care of this";
        Intent intent = new Intent (this, ChildRedeemPage.class);
        // intent.putExtra("prize", prize);
        startActivity(intent);
        mRootRef.child(Constants.REDEEMING_CHILD).child(childId).child(prize).setValue(true);
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.child_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
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
