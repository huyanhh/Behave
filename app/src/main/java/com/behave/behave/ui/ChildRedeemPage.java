package com.behave.behave.ui;

import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.behave.behave.R;
import com.behave.behave.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChildRedeemPage extends AppCompatActivity {
    String prize;
    TextView childprize;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mKidRef = mRootRef.child(Constants.CHILDREN_CHILD);

    String childId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_redeem_page);
        prize = "Candy";

        Intent childNameIntent = getIntent();
        childId = childNameIntent.getStringExtra("childId");


        childprize = (TextView) findViewById(R.id.Prize);

        mKidRef.child(childId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                prize = (dataSnapshot.child("prize").getValue().toString());
                childprize.setText(prize);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void backHome(View view) {
        Intent backhome = new Intent(ChildRedeemPage.this, HomeChildrenPage.class);
        backhome.putExtra("childId", childId);
        this.startActivity(backhome);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.child_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_option1:
                Intent settingsIntent = new Intent(this, ChildAbout.class);
                settingsIntent.putExtra("childId", childId);
                this.startActivity(settingsIntent);
                break;
            case R.id.item_option2:
                LoginActivity.clearUsername();
                Intent logoutIntent = new Intent(this, MainActivity.class);
                this.startActivity(logoutIntent);
                break;
            case R.id.item_option3:
                Intent goHome = new Intent(this, HomeChildrenPage.class);
                goHome.putExtra("childId", childId);
                this.startActivity(goHome);
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}



