package com.behave.behave.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.behave.behave.R;
import com.behave.behave.models.Child;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Calvin on 2/17/2017.
 */

public class AddChild extends AppCompatActivity {

    private static final String TAG = "AddChild";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mKidRef = mRootRef.child("children");
    DatabaseReference mParRef = mRootRef.child("parents");
    String parentId;
    String childId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    mParRef = mParRef.child(user.getUid());
                    parentId = user.getUid();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        final EditText etFirstName = (EditText) findViewById(R.id.etChildFirstName);

        final Button bOk = (Button) findViewById(R.id.bAddChildOk);
        etFirstName.requestFocus();

        assert bOk != null;
        bOk.setOnClickListener(new View.OnClickListener() {

            boolean valid = true;

            @Override
            public void onClick(View view) {
                final String firstName = etFirstName.getText().toString();

                if(firstName.isEmpty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddChild.this);
                    builder.setMessage("Name cannot be blank").setNegativeButton("Retry", null).create().show();
                    etFirstName.requestFocus();
                    valid = false;
                }

                if(valid)
                {
                    showInputDialog(firstName);
                }
            }
        });
    }

    private void createNewChild(String childId, String name) {
        // since we have a Child class we don't need to manually
        // set all fields, so we can just call child
        Child child = new Child(childId, parentId, name);
        mKidRef.child(childId).setValue(child);
        mParRef.child("children").child(childId).setValue(child);
    }

    private void showInputDialog(String firstName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Child's Username");

        final EditText input = new EditText(this);
        final String name = firstName;

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                childId = input.getText().toString();
                createNewChild(childId, name);

                AlertDialog.Builder Alert = new AlertDialog.Builder(AddChild.this);
                Alert.setMessage(name + " has been added. Please have your child use " +
                        childId + " to log in.");
                Alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent rewardIntent = new Intent(AddChild.this, SetUpReward.class);
                        AddChild.this.startActivity(rewardIntent);
                    }
                });
                Alert.create().show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]

}
