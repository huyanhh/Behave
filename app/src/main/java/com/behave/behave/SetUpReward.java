package com.behave.behave;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Calvin on 2/17/2017.
 */

public class SetUpReward extends AppCompatActivity {

    private ListView rewards;
    private ArrayAdapter<String> adapter;
    private String newPrize;
    private int tokenAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_reward);

        final Button bAddReward = (Button) findViewById(R.id.bSetUpAdd);
        final Button bOk = (Button) findViewById(R.id.bSetUpRewards);
        rewards = (ListView) findViewById(R.id.lRewardList);
        final List<String> rewardsList = new ArrayList<String>();
        final Map<String, Integer> reward = new HashMap<String, Integer>();

        adapter = new ArrayAdapter<String>(SetUpReward.this, android.R.layout.simple_list_item_1, rewardsList);
        rewards.setAdapter(adapter);      // arrayadapter filled with friends' name

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

                                adapter.notifyDataSetChanged();
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
                        Intent addChildLater = new Intent(SetUpReward.this, MainActivity.class);
                        SetUpReward.this.startActivity(addChildLater);
                    }
                });
                Alert.create().show();
            }
        });
    }


}
