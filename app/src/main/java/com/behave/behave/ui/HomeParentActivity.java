package com.behave.behave.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

/**
 * Created by huyanh on 2017. 2. 6..
 */

public class HomeParentActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private FirebaseUser mFirebaseUser;

    //    private Button mChildButton;
//    private Button mAddChild;
//    private Button mAddChild2;
//    private Button mPrizeList;
    private ArrayAdapter<String> adapter;
    final Map<String, String> childUID = new HashMap<>();
    List<String> childList = new ArrayList<>();
    //final List<ArrayList<HashMap<String, String>>> childList = new ArrayList<ArrayList<HashMap<String,String>>>();
    //final List<HashMap<String, String>> tableGenerator = new ArrayList<HashMap<String, String>>();
    // when we get a reference it gets us a ref to the root of the json ref tree
    public static final String PARENTS_CHILD = "parents";
    public static final String CHILDREN_CHILD = "children";

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mKidRef = mRootRef.child(CHILDREN_CHILD); // creates `-/children` in db
    DatabaseReference mParRef = mRootRef.child(PARENTS_CHILD);

//    Parent user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_parent_page);

        final TextView tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        final Button bAddChild = (Button) findViewById(R.id.bParentAddChild);
        final Button bViewRewards = (Button) findViewById(R.id.bViewRewards);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mParRef = mParRef.child(mFirebaseUser.getUid());

        ValueEventListener parListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                user = dataSnapshot.getValue(Parent.class);
                tvWelcome.setText("Welcome back, " + dataSnapshot.child("name").getValue());
//                System.out.println(user.name);
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        };
        mParRef.addValueEventListener(parListener);

        bViewRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MAYBE CHANGE THIS???
                Intent seeRewardIntent = new Intent (HomeParentActivity.this, SetUpReward.class);
//                seeRewardIntent.putExtra("True", true);
                HomeParentActivity.this.startActivity(seeRewardIntent);
            }
        });

        bAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addChildIntent = new Intent(HomeParentActivity.this, AddChild.class);
                HomeParentActivity.this.startActivity(addChildIntent);
            }
        });

    }

    private void readChildren() {

        mParRef.child("children").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                final ListView lvParentList = (ListView) findViewById(R.id.lvParentList);
                if (childList.size() != 0) {
                    adapter = new ArrayAdapter<String>(HomeParentActivity.this, android.R.layout.simple_list_item_1, childList);
                    lvParentList.setAdapter(adapter);      // arrayadapter filled with friends' name
                    lvParentList.setOnItemClickListener(HomeParentActivity.this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //whenever condition in the database changes we want to also update child
    //so in this case if we add a child we just change the name from child 1 to
    //the kids name just as a small example
    @Override
    protected void onStart() {
        super.onStart();

        //get children from database
        readChildren();

    }

    //Menu option
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.parent_menu, menu);
        return super.onCreateOptionsMenu(menu);
        //  return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mHome:
                Intent homeIntent = new Intent(this, HomeParentActivity.class);
                this.startActivity(homeIntent);
                break;
//            case R.id.mChild:
//                break;
            case R.id.mRedeemNotification:
                Intent redeemIntent = new Intent(this, ParentRedeemPageListChild.class);
                this.startActivity(redeemIntent);
                break;
//            case R.id.mPrizeList:
//                break;
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
                FirebaseAuth.getInstance().signOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //Menu option end

    //Listen for a child to be selected from the Listview
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent tokenStatus = new Intent(this, TokenStatus.class);
        tokenStatus.putExtra("childName", childList.get(position));
        tokenStatus.putExtra("childUID", childUID.get(childList.get(position)));
        this.startActivity(tokenStatus);
    }
}