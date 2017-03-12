package com.behave.behave.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.behave.behave.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.behave.behave.utils.Constants;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import com.behave.behave.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.button;
import static android.R.attr.key;
import static android.R.attr.value;


public class HomeChildrenPage extends AppCompatActivity {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mKidRef = mRootRef.child(Constants.CHILDREN_CHILD); // creates `-/children` in db
    String childId;

    ArrayList<Pair<String, Integer>> prizes = new ArrayList<Pair<String, Integer>>();
    private List<String> prizesList;
    TextView tvTokenAmount;
    TextView tvGreetings;
    private ArrayAdapter<String> adapter;
    String isRedeem = "true";

//    Button bRedeem = (Button) findViewById(R.id.button_redeem);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_children_page);

        final Button lrb = (Button) findViewById(R.id.listRewardButton);
        final Button curredeem = (Button) findViewById(R.id.credeeming);

        childId = "a6a3-60611b13ed9c";
        Intent childNameIntent = getIntent();
        childId = childNameIntent.getStringExtra("childId");

        tvTokenAmount = (TextView) findViewById(R.id.tokenAmount);
        tvGreetings = (TextView) findViewById(R.id.textView3);

        mKidRef.child(childId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> tempList = new ArrayList<String>();
                tvTokenAmount.setText(dataSnapshot.child("tokens").getValue().toString());
                tvGreetings.setText("Hi " + dataSnapshot.child("name").getValue(String.class));
                isRedeem = dataSnapshot.child("isRedeeming").getValue().toString();


                for (DataSnapshot prize : dataSnapshot.child(Constants.PRIZES_CHILD).getChildren()) {


                    if (prize.getValue(Integer.class) != null) {
                        tempList.add(prize.getKey() + " for " + prize.getValue().toString().concat(" Tokens"));
                    }
//                    for (Pair<String, Integer> p : prizes) {
//                        if (p.first != prize.getKey()) {
                    prizes.add(new Pair(prize.getKey(), prize.getValue(Integer.class)));


                    prizesList = tempList;
                    if (prizesList.size() != 0) {
                        adapter = new ArrayAdapter<String>(HomeChildrenPage.this, android.R.layout.simple_list_item_1, prizesList);
                        //   lvPrizeList.setAdapter(adapter);
                        //lvPrizeList.setOnClickListener(HomeChildrenPage.this);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        lrb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent prizeIntent = new Intent(HomeChildrenPage.this, ChildPrizeList.class);
                prizeIntent.putExtra("childId", childId);
                HomeChildrenPage.this.startActivity(prizeIntent);

            }
        });
        curredeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRedeem == "true") {
                    Intent redeemIntent = new Intent(HomeChildrenPage.this, ChildRedeemPage.class);
                    redeemIntent.putExtra("childId", childId);
                    HomeChildrenPage.this.startActivity(redeemIntent);
                } else {
                    android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(HomeChildrenPage.this);
                    alert.setTitle("Sorry you have no prizes redeeming right now?");
                    alert.setPositiveButton("Ok", null);
                }


            }
        });
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
