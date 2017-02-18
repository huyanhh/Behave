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

import java.util.ArrayList;
import java.util.List;

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
                        newPrize = prize.getText().toString();
                        tokenAmount = Integer.parseInt(tokens.getText().toString());
                        rewardsList.add(newPrize);
                        adapter.notifyDataSetChanged();
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
                Intent intent = new Intent(SetUpReward.this, MainActivity.class);
                SetUpReward.this.startActivity(intent);
            }
        });
    }


}
