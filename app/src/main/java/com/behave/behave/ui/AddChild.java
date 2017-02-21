package com.behave.behave.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.behave.behave.R;

import com.behave.behave.models.Child;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

/**
 * Created by Calvin on 2/17/2017.
 */

public class AddChild extends AppCompatActivity {

    private static final String TAG = "Register";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mKidRef = mRootRef.child("children");
    DatabaseReference mParRef = mRootRef.child("parents").child("parentid1");

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
                   // createAccount(username, password);
                    String uniqueID = UUID.randomUUID().toString();
                    uniqueID = uniqueID.substring(uniqueID.length() / 2 + 1); // remove hyphen
                    createNewChild(uniqueID, firstName);
                    AlertDialog.Builder Alert = new AlertDialog.Builder(AddChild.this);
                    Alert.setMessage(firstName + " has been added, please use the code " +
                            uniqueID + " to log in");
                    Alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent tokenClass = new Intent(AddChild.this, ChildTokenSchedule.class);
                            AddChild.this.startActivity(tokenClass);
                        }
                    });
                    Alert.create().show();
                }
            }
        });
    }

    private void createNewChild(String childId, String name) {
        // since we have a Child class we don't need to manually
        // set all fields, so we can just call child
//        mParRef.child("children").setValue(childId);
        Child child = new Child(childId, "parent1", name);
        mKidRef.child(childId).setValue(child);
        mParRef.child("children").child(childId).setValue(child);
    }

    // [START on_start_add_listener]sdaf
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
