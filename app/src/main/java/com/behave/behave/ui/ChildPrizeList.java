package com.behave.behave.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.behave.behave.R;
import com.behave.behave.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.R.id.auto;

public class ChildPrizeList extends Activity implements AdapterView.OnItemClickListener{


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mKidRef = mRootRef.child(Constants.CHILDREN_CHILD); // creates `-/children` in db
    DatabaseReference mParRef = mRootRef.child("parents");  // points to "parents" directory

    String childId;
    String childName;

    ArrayList<Pair<String, Integer>> prizes = new ArrayList<Pair<String, Integer>>();
    private List<String> prizesList = new ArrayList<String>();
    TextView tvTokenAmount;
    private ArrayAdapter<String> adapter;
    String isRedeem = "true";
    String getChildToken;
    Integer childAmountToken=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_prize_list);

        final Button Home = (Button) findViewById(R.id.backHome);
        final ListView lprizeList = (ListView) findViewById(R.id.lsprizeList);
        final Intent childNameIntent = getIntent();
        childId = childNameIntent.getStringExtra("childId");
        childName = childNameIntent.getStringExtra("childName");



        mKidRef.child(childId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> tempList = new ArrayList<String>();
                isRedeem = dataSnapshot.child("isRedeeming").getValue().toString();
                getChildToken= dataSnapshot.child("tokens").getValue().toString();
                childAmountToken= Integer.parseInt(getChildToken);
                for (DataSnapshot prize : dataSnapshot.child(Constants.PRIZES_CHILD).getChildren()) {


                    if (prize.getValue(Integer.class) != null) {
                        tempList.add(prize.getKey() + " for " + prize.getValue().toString().concat(" Tokens"));
                    }
                    prizes.add(new Pair(prize.getKey(), prize.getValue(Integer.class)));


                    prizesList = tempList;
                    if (prizesList.size() != 0) {
                        adapter = new ArrayAdapter<String>(ChildPrizeList.this, android.R.layout.simple_list_item_1, prizesList);
                        lprizeList.setAdapter(adapter);
                        lprizeList.setOnItemClickListener(ChildPrizeList.this);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent backHome = new Intent(ChildPrizeList.this, HomeChildrenPage.class);
                backHome.putExtra("childId", childId);
                startActivity(backHome);
            }
            });
    }





    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        final String item = (String)adapter.getItem(position);
        //Toast.makeText(this, "You selected : "+ item,Toast.LENGTH_SHORT).show();
        Context context = ChildPrizeList.this;
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(ChildPrizeList.this);
        alert.setTitle("Redeem Prize?");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                android.app.AlertDialog.Builder alert1 = new android.app.AlertDialog.Builder(ChildPrizeList.this);
                    if (isRedeem != "true"){
                        String[] items = item.split(" for ");
                        String p = items[0];
                        for (Pair<String, Integer> ptoke: prizes){
//                            Log.d(ptoke.first, "the array thing");
//                            Log.d(p,"The string you get");
//                            Log.d(String.valueOf(ptoke.second),"the number");

                            if ((ptoke.first).equals(p) ){
                                if(ptoke.second < childAmountToken) {
                                    mKidRef.child(childId).child("amount").setValue(ptoke.second);
                                    mKidRef.child(childId).child("prize").setValue(p);
                                    mKidRef.child(childId).child("isRedeeming").setValue(true);
                                }
                                else{ alert1.setTitle("Sorry you don't have enough tokens for this prize");
                                    alert1.setTitle("Sorry you don't have enough tokens for this prize");
                                    alert1.setPositiveButton("OK",null);
                                    alert1.create().show();

                                }
                            }
                        }


//                        Intent userIntent = new Intent(ChildPrizeList.this, ChildRedeemPage.class);
//                        userIntent.putExtra("prizeName", p);
                        //String t =items[1];
                        //int index = t.indexOf(" ");
                       // String tokens = t.substring(0,index);
                      //  mRootRef.child(childId).child("prize").setValue(p);
                       // String to = mRootRef.child(childId).child("prizes").child(p).getKey();
                    //    Log.d(to, "tokens?");
//                        Toast.makeText(this,"hello" +to,Toast.LENGTH_SHORT).show();
//                        Toast.makeText(this,to,Toast.LENGTH_SHORT).show();
                        //int token = Integer.parseInt(to);
                       // mRootRef.child(childId).child("prizeToken").setValue(token);
                      //  mRootRef.child(childId).child("isRedeeming").setValue(true);
                    }
                    else{
                        alert1.setTitle("Sorry");
                        alert1.setMessage("You are already redeeming an item");
                        alert1.setPositiveButton("OK",null);
                        alert1.create().show();
                    }

            }
            });
        alert.setNegativeButton("Cancel", null);
        alert.setView(layout);
        alert.show();
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



