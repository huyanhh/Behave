package com.behave.behave;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Calvin on 2/17/2017.
 */

public class Register extends AppCompatActivity
{
    private static final String TAG = "Register";
    private static String firstName;
    private static String lastName;
    private static String email;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_register);

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

        final EditText etFirstName = (EditText) findViewById(R.id.etFirstName);
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etVerifyPassword = (EditText) findViewById(R.id.etVerifyPassword);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final Button bRegister = (Button) findViewById(R.id.bRegister);

        assert bRegister != null;
        bRegister.setOnClickListener(new View.OnClickListener()
        {
            boolean valid = true;
            @Override
            public void onClick(View v) {
                firstName = etFirstName.getText().toString();
                final String Username = etUsername.getText().toString();
                email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();
                final String verifyPassword = etVerifyPassword.getText().toString();
                valid = true;

                if (!isValidEmail(email) || email.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setMessage("Not a valid Email address").setNegativeButton("Retry", null).create().show();
                    valid = false;
                    etEmail.requestFocus();
                }
                if (!notEmpty(firstName)|| firstName == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setMessage("Name cannot be blank").setNegativeButton("Retry", null).create().show();
                    valid = false;
                    etFirstName.requestFocus();
                }

                if (!notEmpty(Username) || Username == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setMessage("Not a valid Username").setNegativeButton("Retry", null).create().show();
                    valid = false;
                    etUsername.requestFocus();
                }
                if (!notEmpty(password) || password.length() < 8) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setMessage("Password has to be at least 8 characters long").setNegativeButton("Retry", null).create().show();
                    valid = false;
                    etPassword.requestFocus();
                }

                if(!password.equals(verifyPassword))
                {
                    AlertDialog.Builder Alert = new AlertDialog.Builder(Register.this);
                    Alert.setMessage("Password does not match");
                    Alert.setPositiveButton("OK", null);
                    etPassword.setText("");
                    etVerifyPassword.setText("");
                    Alert.create().show();
                    etPassword.requestFocus();
                    valid = false;
                }


                if(valid) {
                    createAccount(email, password);

                    AlertDialog.Builder Alert = new AlertDialog.Builder(Register.this);
                    Alert.setMessage("Do you want to add child now?");
                    Alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent addChildNow = new Intent(Register.this, AddChild.class);
                            Register.this.startActivity(addChildNow);
                        }
                    });
                    Alert.setNeutralButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent addChildLater = new Intent(Register.this, LoginActivity.class);
                            Register.this.startActivity(addChildLater);
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
                            Toast.makeText(Register.this, "firebase auth failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Register.this, "success",
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


    public final static boolean isValidEmail(CharSequence target)
    {
        if (target == null)
            return false;
        else
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean notEmpty(final String string)
    {
        return string != null && !string.isEmpty() && !string.trim().isEmpty();
    }

    public static boolean isDateValid(String date)
    {
        String regEx ="^(0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])[- /.](19|20)\\d{2}$";
        Matcher matcherObj = Pattern.compile(regEx).matcher(date);
        return matcherObj.matches();
    }

    public static String getFirstName()
    {
        return firstName;
    }
    public static String getLastName()
    {
        return lastName;
    }
    public static String getEmail()
    {
        return email;
    }
}
