package com.behave.behave.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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


/* Created by Calvin Poon and Irish Marquez */


public class ParentRedeemPageListChild extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private FirebaseUser mFirebaseUser;
    private ArrayAdapter<String> adapter;

    final Map<String, String> childUID = new HashMap<>();      // KV-pair of child's UID and its value
    final Map<String, String> childPrize = new HashMap<>();
    final Map<String, String> childAmount = new HashMap<>();
    final Map<String, String> childToken = new HashMap<>();
    List<String> childList = new ArrayList<>();


    // when we get a reference it gets us a ref to the root of the json ref tree
    public static final String PARENTS_CHILD = "parents";       // "parents" is a db child
    public static final String CHILDREN_CHILD = "children";     // "children" is a db child

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference(); // points to root directory
    DatabaseReference mKidRef = mRootRef.child(CHILDREN_CHILD); // points to "children" directory
    DatabaseReference mParRef = mRootRef.child(PARENTS_CHILD);  // points to "parents" directory
    //String parentuid = mFirebaseUser.getUid();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Redeem Notification");
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
    }


    private void displayChildrenWhoWantToRedeem()
    {
        mKidRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                List<String> nameList = new ArrayList<>();

                for (DataSnapshot kid : dataSnapshot.getChildren()) {
                    String userUID = mFirebaseUser.getUid();
                    String parentUID = kid.child("parentId").getValue(String.class);

                    String name = kid.child("name").getValue().toString();
                    String uid = kid.child("uid").getValue().toString();
                    String isRedeeming = kid.child("isRedeeming").getValue().toString();
                    String amount = kid.child("amount").getValue().toString();
                    String prize = kid.child("prize").getValue().toString();
                    String token = kid.child("tokens").getValue().toString();

                    //if the user is this child's parent and the child wants to redeem
                    if (userUID.equalsIgnoreCase(parentUID) && isRedeeming.equalsIgnoreCase("true")) {
                        nameList.add(name);
                        childUID.put(name, uid); //add all of this parent's children to map
                        childAmount.put(name, amount);
                        childPrize.put(name, prize);
                        childToken.put(name, token);
                    }
                }
                childList = nameList;  // all the kids' names, whether redeeming or not

                final ListView lvParentList = (ListView) findViewById(R.id.lv_childList);   //gets reference to Listview
                final TextView tv_promptUser = (TextView) findViewById(R.id.tv_prompt);
                if (childList.size() != 0)
                {
                    tv_promptUser.setText("Child Redeemers List");
                    //attaches this list to the .xml file using adapter
                    // Use a custom adapter to display avatar with child name
                    ListAdapter adapter_allChildren = new CustomListViewAdapter(ParentRedeemPageListChild.this, childList);
                    lvParentList.setAdapter(adapter_allChildren);      // arrayadapter filled with friends' name
                    lvParentList.setOnItemClickListener(ParentRedeemPageListChild.this); //"listens" to the click
                }
                else
                    tv_promptUser.setText("Redeemers List is Empty");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //need this so ValueEventListener() doesn't cause error
            }
        });
    }


    //Listens for a child to be selected from the Listview
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String childName = childList.get(position); // retrieves child's name
        String uid       = childUID.get(childName);       // retrieves child's uid
        String amount    = childAmount.get(childName);  // retrieves prize amount
        String tokens    = childToken.get(childName);    // retrieves child's token count
        String prize     = childPrize.get(childName);   // retrieves child's prize

        Intent allowRedeemIntent = new Intent(this, ParentRedeemPageAllowRedemption.class);
        allowRedeemIntent.putExtra("childName", childName);
        allowRedeemIntent.putExtra("childId", childUID.get(childName));
        allowRedeemIntent.putExtra("prize", prize);
        allowRedeemIntent.putExtra("amount", amount);
        allowRedeemIntent.putExtra("tokens", tokens);
        this.startActivity(allowRedeemIntent);
    }


    //whenever condition in the database changes we want to also update child
    //so in this case if we add a child we just change the name from child 1 to
    //the kids name just as a small example
    @Override
    protected void onStart() {
        super.onStart();
        displayChildrenWhoWantToRedeem();         //gets children from database
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
