package com.behave.behave;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Calvin on 2/17/2017.
 */

public class AddChild extends AppCompatActivity {

    private static final String TAG = "Register";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

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
        final EditText etUsername = (EditText) findViewById(R.id.etChildUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etChildPassword);
        final EditText etVerifyPassword = (EditText) findViewById(R.id.etChildVerifyPassword);
        final Button bOk = (Button) findViewById(R.id.bAddChildOk);
        etFirstName.requestFocus();

        assert bOk != null;
        bOk.setOnClickListener(new View.OnClickListener() {

            boolean valid = true;

            @Override
            public void onClick(View view) {
                final String firstName = etFirstName.getText().toString();
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                final String verifyPassword = etVerifyPassword.getText().toString();

                if(firstName.isEmpty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddChild.this);
                    builder.setMessage("Name cannot be blank").setNegativeButton("Retry", null).create().show();
                    etFirstName.requestFocus();
                    valid = false;

                }
                if(username.isEmpty() )
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddChild.this);
                    builder.setMessage("Not a valid Username").setNegativeButton("Retry", null).create().show();
                    etUsername.requestFocus();
                    valid = false;
                }
                if(password.isEmpty() || verifyPassword.isEmpty() || password.length() < 8)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddChild.this);
                    builder.setMessage("Password has to be at least 8 characters long").setNegativeButton("Retry", null).create().show();
                    etPassword.requestFocus();
                    valid = false;
                }

                if(!password.equals(verifyPassword))
                {
                    AlertDialog.Builder Alert = new AlertDialog.Builder(AddChild.this);
                    Alert.setMessage("Password does not match");
                    Alert.setPositiveButton("OK", null);
                    etPassword.setText("");
                    etVerifyPassword.setText("");
                    Alert.create().show();
                    etPassword.requestFocus();
                    valid = false;
                }

                if(valid)
                {
                   // createAccount(username, password);

                    AlertDialog.Builder Alert = new AlertDialog.Builder(AddChild.this);
                    Alert.setMessage(firstName + " has been added");
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

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(AddChild.this, "firebase auth failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddChild.this, "success",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        // [END create_user_with_email]
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
