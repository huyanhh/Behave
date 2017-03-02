package com.behave.behave.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.behave.behave.R;
import com.behave.behave.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Calvin on 2/20/2017.
 */

public class TokenStatus extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private List<String> tokenList = new ArrayList<>();
    private HashMap<String, String> tokenKeyList = new HashMap<>();
    private ArrayAdapter<String> adapter;
    int tokenCount;

    TextView tvTokenCount;
    private String childUID;// = "a6a3-60611b13ed9c";
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mKidRef = mRootRef.child(Constants.CHILDREN_CHILD);
    DatabaseReference mAddToken;// = mKidRef.child(childUID).child("tokenDescription");
    private ListView lvTokenList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_status);

        Intent intent = getIntent();
        String childName = intent.getStringExtra("childName");  //gets the child's name
        childUID = intent.getStringExtra("childUID");   // gets the child's uid
        final TextView tvChildName = (TextView) findViewById(R.id.tvChildNameTokenStatus);
        tvTokenCount = (TextView) findViewById(R.id.tvTokenCount);
        final Button bTokenMinus = (Button) findViewById(R.id.bTokenStatusMinus);
        final Button bTokenAdd = (Button) findViewById(R.id.bTokenStatusAdd);
        lvTokenList = (ListView) findViewById(R.id.lvTokenStatusList);
        tvChildName.setText(childName + " Token Status:");
        mAddToken = mKidRef.child(childUID).child("tokenDescription");

       //put lists onto ListView, lvTokenList
        mAddToken.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> tempList = new ArrayList<String>();
                for(DataSnapshot des : dataSnapshot.getChildren())
                {
                    String tokenDes = des.getValue(String.class);
                    String tokenKey = des.getKey();
                    if(tokenDes != null)
                    {
                        tempList.add(tokenDes);
                    }
                    if(!tokenKeyList.containsKey(tokenKey))
                    {
                        tokenKeyList.put(tokenDes, tokenKey);
                    }
                }
                tokenList = tempList;

                if(tokenList.size() != 0) {
                    adapter = new ArrayAdapter<String>(TokenStatus.this, android.R.layout.simple_list_item_1, tokenList);
                    lvTokenList.setAdapter(adapter);      // arrayadapter filled with friends' name
                    lvTokenList.setOnItemClickListener(TokenStatus.this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //take away a token; REDUDANT AS bTokenAdd NEEDS TO BE IMPROVE
        bTokenMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = TokenStatus.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                AlertDialog.Builder alert = new AlertDialog.Builder(TokenStatus.this);
                alert.setTitle("Takeaway a token");

                final EditText tokenDescription = new EditText(TokenStatus.this);
                tokenDescription.setHint("Enter reason to take away a token");
                tokenDescription.setInputType(InputType.TYPE_CLASS_TEXT);
                //alert.setView(prize);
                layout.addView(tokenDescription);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder alert1 = new AlertDialog.Builder(TokenStatus.this);

                        if(tokenDescription.getText().toString().isEmpty())
                        {
                            alert1.setTitle("Warning");
                            alert1.setMessage("Token Description cannot be empty");
                            alert1.setPositiveButton("OK", null);
                            tokenDescription.requestFocus();
                            alert1.create().show();
                        }
                        else {
                            mKidRef.child(TokenStatus.this.childUID).child("tokens").setValue(--tokenCount);
                            mAddToken.push().setValue(tokenDescription.getText().toString());
                        }
                    }
                });
                alert.setNegativeButton("Cancel", null);
                alert.setView(layout);
                alert.show();
            }
        });

        //add token; REDUDANT AS bTokenMinus NEEDS TO BE IMPROVE
        bTokenAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = TokenStatus.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                AlertDialog.Builder alert = new AlertDialog.Builder(TokenStatus.this);
                alert.setTitle("Add Token Description");

                final EditText tokenDescription = new EditText(TokenStatus.this);
                tokenDescription.setHint("Enter reason for token");
                tokenDescription.setInputType(InputType.TYPE_CLASS_TEXT);
                //alert.setView(prize);
                layout.addView(tokenDescription);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder alert1 = new AlertDialog.Builder(TokenStatus.this);

                        if(tokenDescription.getText().toString().isEmpty())
                        {
                            alert1.setTitle("Warning");
                            alert1.setMessage("Token Description cannot be empty");
                            alert1.setPositiveButton("OK", null);
                            tokenDescription.requestFocus();
                            alert1.create().show();
                        }
                        else {
                            mKidRef.child(TokenStatus.this.childUID).child("tokens").setValue(++tokenCount);
                            mAddToken.push().setValue(tokenDescription.getText().toString());
                        }
                    }
                });
                alert.setNegativeButton("Cancel", null);
                alert.setView(layout);
                alert.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ValueEventListener kidListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tokenCount = dataSnapshot.child(childUID).child("tokens").getValue(Integer.class);
                tvTokenCount.setText("Token Count: " + tokenCount);
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        };
        mKidRef.addValueEventListener(kidListener);
    }

    //pick a token to edit in the list. can click on token and edit the description
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final String key;
        if(tokenKeyList.containsKey(tokenList.get(position)));
            key = tokenKeyList.get(tokenList.get(position));

        Context context = TokenStatus.this;
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        AlertDialog.Builder alert = new AlertDialog.Builder(TokenStatus.this);
        alert.setTitle("Edit Token Description");

        final EditText tokenDescription = new EditText(TokenStatus.this);
        tokenDescription.setHint("Enter new description");
        tokenDescription.setInputType(InputType.TYPE_CLASS_TEXT);
        //alert.setView(prize);
        layout.addView(tokenDescription);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(TokenStatus.this);

                if(tokenDescription.getText().toString().isEmpty())
                {
                    alert1.setTitle("Warning");
                    alert1.setMessage("Token Description cannot be empty");
                    alert1.setPositiveButton("OK", null);
                    tokenDescription.requestFocus();
                    alert1.create().show();
                }
                else {
                    //mKidRef.child(TokenStatus.this.childUID).child("tokens").setValue(++tokenCount);
                    mAddToken.child(key).setValue(tokenDescription.getText().toString());
                }
            }
        });
        alert.setNegativeButton("Cancel", null);
        alert.setView(layout);
        alert.show();
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //Menu option end
}
