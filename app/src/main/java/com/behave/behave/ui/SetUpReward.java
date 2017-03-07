package com.behave.behave.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.behave.behave.R;
import com.behave.behave.utils.Constants;
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
import java.util.Set;

/**
 * Created by Calvin on 2/20/2017.
 */

public class SetUpReward extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private ListView rewards;
    private ArrayAdapter<String> adapter;
    private String newPrize;
    private int tokenAmount;
    final Map<String, Integer> reward = new HashMap<String, Integer>();
    private List<String> rewardsList = new ArrayList<String>();
    private Map<String, Integer> rewardKeyList = new HashMap<>();
    private List<String> kids = new ArrayList<>();

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mPrizesRef;
    DatabaseReference mChildrenRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_reward);
        mChildrenRef = mRootRef.child(Constants.CHILDREN_CHILD);

        mPrizesRef = mRootRef.child("parents").child(mFirebaseUser.getUid()).child(Constants.PRIZES_CHILD);

        final Button bAddReward = (Button) findViewById(R.id.bSetUpAdd);
        final Button bOk = (Button) findViewById(R.id.bSetUpRewards);

        rewards = (ListView) findViewById(R.id.lRewardList);

        mRootRef.child("parents").child(mFirebaseUser.getUid()).child(Constants.CHILDREN_CHILD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot kid : dataSnapshot.getChildren())
                    kids.add(kid.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mPrizesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List <String> tempList = new ArrayList<String>();
                for(DataSnapshot reward : dataSnapshot.getChildren()){
                    Integer rewardDes = reward.getValue(Integer.class);
                    String rewardKey = reward.getKey();
                    if(rewardDes != null)
                        tempList.add(rewardKey + "\t" + " for " +  rewardDes.toString().concat(" Tokens"));
                    if(!rewardKeyList.containsKey(rewardKey))
                        rewardKeyList.put(rewardKey, rewardDes);
                }
                rewardsList = tempList;

                if(rewardsList.size() != 0)
                {
                    adapter = new ArrayAdapter<String>(SetUpReward.this, android.R.layout.simple_list_item_1, rewardsList);
                    rewards.setAdapter(adapter);      // arrayadapter filled with friends' name
                    rewards.setOnItemClickListener(SetUpReward.this);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        bAddReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = SetUpReward.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                AlertDialog.Builder alert = new AlertDialog.Builder(SetUpReward.this);
                alert.setTitle("Add Reward");

                final EditText prize = new EditText(SetUpReward.this);
                prize.setHint("Enter Prize");
                prize.setInputType(InputType.TYPE_CLASS_TEXT);
                //alert.setView(prize);
                layout.addView(prize);

                final EditText tokens = new EditText(SetUpReward.this);
                tokens.setInputType(InputType.TYPE_CLASS_NUMBER);
                tokens.setHint("Enter amount");
                //alert.setView(tokens);
                layout.addView(tokens);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder alert1 = new AlertDialog.Builder(SetUpReward.this);

                        if(prize.getText().toString().isEmpty())
                        {
                            alert1.setTitle("Warning");
                            alert1.setMessage("Prize entry can't be empty");
                            alert1.setPositiveButton("OK", null);
                            prize.requestFocus();
                            alert1.create().show();
                        }
                        else if(tokens.getText().toString().isEmpty())
                        {
                            alert1.setTitle("Warning");
                            alert1.setMessage("Amount of tokens entry can't be empty");
                            alert1.setPositiveButton("OK", null);
                            //dfstokens.requestFocus();
                            alert1.create().show();
                        }
                        else {
                            newPrize = prize.getText().toString();
                            tokenAmount = Integer.parseInt(tokens.getText().toString());
                            int x = (newPrize.length()) - 1;
                            while (newPrize.charAt(x--) == ' ') {
                            }
                            //                        newPrize.ind
                            // if(newPrize.charAt(x) == ' ')
                            newPrize = newPrize.substring(0, x + 2);

                            if (!reward.containsKey(newPrize.toLowerCase())) {
                                reward.put(newPrize, tokenAmount);
                                rewardsList.add(newPrize.concat(" ".concat(String.valueOf(tokenAmount).concat(" Tokens"))));

                                alert1.setTitle("Reward added");
                                alert1.setMessage(newPrize + " has been added");
                                alert1.setPositiveButton("OK", null);
                                alert1.create().show();

                                writePrize(newPrize, tokenAmount);
                                //adapter.notifyDataSetChanged();
                            } else {

                                alert1.setTitle("Duplicate Reward");
                                alert1.setMessage("Duplicate Reward");
                                alert1.setPositiveButton("OK", null);
                                alert1.create().show();
                            }
                        }
                    }
                });
                alert.setNegativeButton("Cancel", null);
                alert.setView(layout);
                alert.show();
            }
        });

        bOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String goBack;
//                if(getIntent().getStringExtra("True")!=null)
//                {
//                    Intent HomeIntent = new Intent(SetUpReward.this, HomeParentActivity.class);
//                    SetUpReward.this.startActivity(HomeIntent);
//                }
                //do something
                AlertDialog.Builder Alert = new AlertDialog.Builder(SetUpReward.this);
                Alert.setMessage("Do you want to add child now?");
                Alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent addChildNow = new Intent(SetUpReward.this, AddChild.class);
                        SetUpReward.this.startActivity(addChildNow);
                    }
                });
                Alert.setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent addChildLater = new Intent(SetUpReward.this, HomeParentActivity.class);
                        SetUpReward.this.startActivity(addChildLater);
                    }
                });
                Alert.create().show();
            }
        });
    }

    //fb write
    private void writePrize(String prize, Integer cost) {
        mPrizesRef.child(prize).setValue(cost);
        for (String childId: kids)
            mChildrenRef.child(childId).child(Constants.PRIZES_CHILD).child(prize).setValue(cost);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final String key = rewardsList.get(position).substring(0, rewardsList.get(position).indexOf("\t"));

        Context context = SetUpReward.this;
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        AlertDialog.Builder alert = new AlertDialog.Builder(SetUpReward.this);
        alert.setTitle("Edit Rewards");

        final EditText prize = new EditText(SetUpReward.this);
        prize.setHint("Enter Prize");
        prize.setInputType(InputType.TYPE_CLASS_TEXT);
        //alert.setView(prize);
        layout.addView(prize);

        final EditText tokens = new EditText(SetUpReward.this);
        tokens.setInputType(InputType.TYPE_CLASS_NUMBER);
        tokens.setHint("Enter amount");
        //alert.setView(tokens);
        layout.addView(tokens);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(SetUpReward.this);

                if(prize.getText().toString().isEmpty())
                {
                    alert1.setTitle("Warning");
                    alert1.setMessage("Prize entry can't be empty");
                    alert1.setPositiveButton("OK", null);
                    prize.requestFocus();
                    alert1.create().show();
                }
                else if(tokens.getText().toString().isEmpty())
                {
                    alert1.setTitle("Warning");
                    alert1.setMessage("Amount of tokens entry can't be empty");
                    alert1.setPositiveButton("OK", null);
                    //dfstokens.requestFocus();
                    alert1.create().show();
                }
                else {
                    newPrize = prize.getText().toString();
                    tokenAmount = Integer.parseInt(tokens.getText().toString());
                    int x = (newPrize.length()) - 1;
                    while (newPrize.charAt(x--) == ' ') {
                    }
                    //                        newPrize.ind
                    // if(newPrize.charAt(x) == ' ')
                    newPrize = newPrize.substring(0, x + 2);

                    if (!reward.containsKey(newPrize.toLowerCase())) {
                        reward.remove(key);
                        rewardKeyList.remove(key);
                        mPrizesRef.child(key).removeValue();

                        reward.put(newPrize, tokenAmount);
                        rewardsList.add(newPrize.concat(" ".concat(String.valueOf(tokenAmount).concat(" Tokens"))));

                        alert1.setTitle("Reward added");
                        alert1.setMessage(newPrize + " has been added");
                        alert1.setPositiveButton("OK", null);
                        alert1.create().show();

                        writePrize(newPrize, tokenAmount);
                    } else {

                        alert1.setTitle("Duplicate Reward");
                        alert1.setMessage("Duplicate Reward");
                        alert1.setPositiveButton("OK", null);
                        alert1.create().show();
                    }
                }
            }
        });
        alert.setNegativeButton("Cancel", null);
        alert.setView(layout);
        alert.show();
    }
}
