package com.behave.behave.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.behave.behave.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/* Created by Irish Marquez */


public class ParentRedeemPageListChild extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "myMessage";

    private FirebaseUser mFirebaseUser;
    private ArrayAdapter<String> adapter;
    final Map<String, String> childUID = new HashMap<>();
    List<String> childList = new ArrayList<>();

    // when we get a reference it gets us a ref to the root of the json ref tree
    public static final String PARENTS_CHILD = "parents";
    public static final String CHILDREN_CHILD = "children";

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mKidRef = mRootRef.child(CHILDREN_CHILD); // creates `-/children` in db
    DatabaseReference mParRef = mRootRef.child(PARENTS_CHILD);


    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "PRListChild.onCreate_begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_redeem_page_list_child);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();    //used to get the parent from firebase
        mParRef = mParRef.child(mFirebaseUser.getUid());


        // Used to display "Welcome back, parentName" on the screen
       ValueEventListener parListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //need this so ValueEventListener() doesn't cause error
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //need this so ValueEventListener() doesn't cause error
            }
        };
        Log.i(TAG, "PRListChild.onCreate_end");
    }


    private void readChildren()
    {
        Log.i(TAG, "PRListChild.readChildren_start");
        mParRef.child("children").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Log.i(TAG, "PRListChild.onDataChange_begin");
                List<String> nameList = new ArrayList<>();
                for (DataSnapshot kid : dataSnapshot.getChildren()) {
                    String name = kid.child("name").getValue(String.class);
                    String uid = kid.child("uid").getValue(String.class);
                    if (name != null) {
                        nameList.add(name);
                        childUID.put(name, uid);
                    }
                }
                childList = nameList;
                final ListView lvParentList = (ListView) findViewById(R.id.lv_childList);   //gets reference to Listview
                if (childList.size() != 0)
                {   //attaches this list to the .xml file using adapter
                    adapter = new ArrayAdapter<String>(ParentRedeemPageListChild.this, android.R.layout.simple_list_item_1, childList);
                    lvParentList.setAdapter(adapter);      // arrayadapter filled with friends' name
                    lvParentList.setOnItemClickListener(ParentRedeemPageListChild.this); //"listens" to the click
                }
                Log.i(TAG, "PRListChild.onDataChange_end");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //need this so ValueEventListener() doesn't cause error
            }
        });
        Log.i(TAG, "PRListChild.readChildren_end");
    }


    //Listens for a child to be selected from the Listview
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "PRListChild.onItemClick_begin");
        Intent allowRedeemIntent = new Intent(this, ParentRedeemPageAllowRedemption.class);
        String childName = childList.get(position);
        allowRedeemIntent.putExtra("childName", childName);
        allowRedeemIntent.putExtra("childId", childUID.get(childName));
        this.startActivity(allowRedeemIntent);          //<--------------- why does this skip to redeempagesuccess?
        Log.i(TAG, "PRListChild.onItemClick_end");
    }


    //whenever condition in the database changes we want to also update child
    //so in this case if we add a child we just change the name from child 1 to
    //the kids name just as a small example
    @Override
    protected void onStart() {
        Log.i(TAG, "PRListChild.onStart_begin");
        super.onStart();
        readChildren();         //gets children from database
        Log.i(TAG, "PRListChild.onStart_end");
    }


    //Creates Overflow Menu
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.parent_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Overflow menu
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
