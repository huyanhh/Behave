package com.behave.behave.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
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

import com.behave.behave.models.Child;
import com.behave.behave.ui.ChildRedeemPage;
import com.behave.behave.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.behave.behave.utils.Constants;
import com.google.firebase.database.ValueEventListener;

import com.behave.behave.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.key;
import static android.R.attr.value;


public class HomeChildrenPage extends AppCompatActivity  {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mKidRef = mRootRef.child(Constants.CHILDREN_CHILD); // creates `-/children` in db
    String childId;
    ArrayList<Pair<String, Integer>> prizes = new ArrayList<Pair<String, Integer>>();
    private List<String> prizesList;
    TextView tvTokenAmount;
    TextView tvGreetings;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_children_page);

        final Button rp = (Button) findViewById(R.id.button_redeem);

        childId = "a6a3-60611b13ed9c";
        Intent childNameIntent = getIntent();
        childId = childNameIntent.getStringExtra("childId");
        // Example of a call to a native method
        //  TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());
        tvTokenAmount = (TextView) findViewById(R.id.tokenAmount);
        tvGreetings = (TextView) findViewById(R.id.textView3);
        final ListView lvPrizeList = (ListView) findViewById(R.id.lvPrizeList);
        // TODO:- Calvin pass in the data from the first view and put it here
        mKidRef.child(childId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> tempList = new ArrayList<String>();
                tvTokenAmount.setText(dataSnapshot.child("tokens").getValue().toString());
                tvGreetings.setText("Hi " + dataSnapshot.child("name").getValue(String.class));
                for (DataSnapshot prize : dataSnapshot.child(Constants.PRIZES_CHILD).getChildren()) {
//                    Integer prizeDes = prize.getValue(Integer.class);
//                    String prizeKey = prize.getKey();

                    if (prize.getValue(Integer.class) != null) {
                        tempList.add(prize.getKey() + " for " + prize.getValue().toString().concat(" Tokens"));
                    }
//                    for (Pair<String, Integer> p : prizes) {
//                        if (p.first != prize.getKey()) {
                    prizes.add(new Pair(prize.getKey(), prize.getValue(Integer.class)));


                    prizesList = tempList;
                    if (prizesList.size() != 0) {
                        adapter = new ArrayAdapter<String>(HomeChildrenPage.this, android.R.layout.simple_list_item_1, prizesList);
                        lvPrizeList.setAdapter(adapter);
                        //lvPrizeList.setOnClickListener(HomeChildrenPage.this);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //final ListView lvPrizeList = (ListView) findViewById(R.id.lvPrizeList);
        //   if (prizes.size()!=0){
        //  adapter = new ArrayAdapter<String>(HomeChildrenPage.this,android.R.layout.simple_list_item_1, prizes);
        //   }

//        rp.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Context context = HomeChildrenPage.this;
//                LinearLayout layout = new LinearLayout(context);
//                layout.setOrientation(LinearLayout.VERTICAL);
//
//                AlertDialog.Builder alert = new AlertDialog.Builder(HomeChildrenPage.this);
//                alert.setTitle("Redeem this Prize?");


//            }
//        });
    }
    //@Override
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mKidRef.child(childId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot prize : dataSnapshot.child(Constants.PRIZES_CHILD).getChildren()) {
//                    prizes.add(new Pair(prize.getKey(), prize.getValue(Integer.class)));
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
//    }

    public void redeemPrize(View v) {
//        mRootRef.child(Constants.REDEEMING_CHILD).child(childId).
        // pick whichever prize is in the prize list then pass it to the parent
        // first check if prize and token count are valid
        // prizes

        Context context = HomeChildrenPage.this;
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(HomeChildrenPage.this);
        alert.setTitle("Redeem Prize");

        final EditText pr = new EditText(HomeChildrenPage.this);
        pr.setHint("Which prize?");
        pr.setInputType(InputType.TYPE_CLASS_TEXT);
        //alert.setView(prize);
        layout.addView(pr);

        final EditText tokens = new EditText(HomeChildrenPage.this);
        tokens.setInputType(InputType.TYPE_CLASS_NUMBER);
        tokens.setHint("How many tokens?");
        //alert.setView(tokens);
        layout.addView(tokens);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                android.app.AlertDialog.Builder alert1 = new android.app.AlertDialog.Builder(HomeChildrenPage.this);

                if (pr.getText().toString().isEmpty()) {
                    alert1.setTitle("Warning");
                    alert1.setMessage("Prize entry can't be empty");
                    alert1.setPositiveButton("OK", null);
                    pr.requestFocus();
                    alert1.create().show();
                } else if (tokens.getText().toString().isEmpty()) {
                    alert1.setTitle("Warning");
                    alert1.setMessage("Amount of tokens cannot be empty");
                    alert1.setPositiveButton("OK", null);
                    alert1.create().show();
                } else {
                    String newPrize = pr.getText().toString();
                    //System.out.print(newPrize);
                    String ta = tvTokenAmount.getText().toString();
                    String temp= "";
                    for (Pair<String, Integer> p : prizes) {
                        if ((p.first).equals(newPrize) ) {
                            temp = p.first;
                        }}
                    if (!ta.equals("") && !ta.equals(null)) {
                        int numToken = Integer.parseInt(ta);
                        Integer Tks = Integer.parseInt(tokens.getText().toString());

                                if (temp.equals(newPrize)){
                                if (Tks <= numToken) {
                                    String redeemable = newPrize;
                                    Intent intent = new Intent(HomeChildrenPage.this, ChildRedeemPage.class);
                                    startActivity(intent);
                                    mRootRef.child(Constants.REDEEMING_CHILD).child(childId).child(redeemable).setValue(true);
                                } else {
                                    alert1.setTitle("Not enough tokens");
                                    alert1.setMessage("Not enough tokens");
                                    alert1.setPositiveButton("OK", null);
                                    alert1.create().show();
                                }
                            } else {
                                alert1.setTitle("Invalid prize");
                                alert1.setMessage("Invalid prize");
                                alert1.setPositiveButton("OK", null);
                                alert1.create().show();
                            }
                        }else{
                            alert1.setTitle("Token error");
                            alert1.setMessage("Token Error");
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





//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        final String key = prizesList.get(position).substring(0, prizesList.get(position).indexOf("\t"));
//
//        Context context = HomeChildrenPage.this;
//        LinearLayout layout = new LinearLayout(context);
//        layout.setOrientation(LinearLayout.VERTICAL);
//
//        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(HomeChildrenPage.this);
//        alert.setTitle("Redeem this reward?");
//
//
//        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                android.app.AlertDialog.Builder alert1 = new android.app.AlertDialog.Builder(HomeChildrenPage.this);
//
//
//            }
//        });
//
//
//        alert.setNegativeButton("Cancel", null);
//        alert.setView(layout);
//        alert.show();
//    }

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
                Intent settingsIntent = new Intent(this, ChildAbout.class);
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
