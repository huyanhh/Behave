package com.behave.behave;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import static java.sql.DriverManager.println;

public class LoginActivity extends AppCompatActivity
{
    private static final String TAG = "Login";
    private static SharedPreferences ps;
    private static SharedPreferences.Editor pe;
    private static String Username;
    private static String Password;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

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

        ps = getPreferences(0);
        pe = ps.edit();

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);

        final Button bLogin = (Button) findViewById(R.id.bLogin);
        final TextView tvRegisterLink = (TextView) findViewById(R.id.tvRegister);
        final TextView tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);

        tvRegisterLink.setPaintFlags(tvRegisterLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvForgotPassword.setPaintFlags(tvForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        String spUsername = ps.getString("Username","");
        String spPassword = ps.getString("password", "");

        //When Register is clicked, send user to Register screen; Register.class
        tvRegisterLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent registerIntent = new Intent(LoginActivity.this, Register.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        //When Forgot password is clicked, send user to Forgot password screen; ForgotPassword.class
        tvForgotPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent ForgotPasswordIntent = new Intent(LoginActivity.this, ForgotPassword.class);
                LoginActivity.this.startActivity(ForgotPasswordIntent);
            }
        });

        //auto-login
        if (spUsername.length() != 0 || spPassword.length() != 0)
        {
            etUsername.setText(spUsername);
            etPassword.setText(spPassword);
            //create an intent to store Username information for UserActivity
            Intent userIntent = new Intent(LoginActivity.this, HomeParentActivity.class);
            userIntent.putExtra("Username", spUsername);
            Username = spUsername;
            userIntent.putExtra("password", spPassword);
            Password = spPassword;
            //start activity to UserActivity.class
            LoginActivity.this.startActivity(userIntent);
        }


        //LoginActivity process
        if (bLogin != null) {
            //when Login button is clicked, store username and password to a String
            bLogin.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final String username = etUsername.getText().toString(); // should be email
                    final String password = etPassword.getText().toString();

                    signIn(username, password);
//                    Response.Listener<String> responseListener = new Response.Listener<String>() {
//                        @Override
                        //Validate Username + Password with database
//                        public void onResponse(String response) {
//                            try {
//                                JSONObject jsonResponse = new JSONObject(response);
//                                boolean success = jsonResponse.getBoolean("success");
//                                if (success) {
//                                    String username = jsonResponse.getString("Username");
//                                    String password = jsonResponse.getString("password");
//                                    pe.putString("Username", username);
//                                    pe.putString("password", password);
//                                    pe.commit();
//
//                                    //create an intent to store Username information for UserActivity
//                                    Intent userIntent = new Intent(LoginActivity.this, HomeParentActivity.class);
//                                    userIntent.putExtra("Username", username);
//                                    userIntent.putExtra("password", password);
//
//                                    //start activity to UserActivity.class
//                                    LoginActivity.this.startActivity(userIntent);
//                                } else {
//                                    AlertDialog.Builder Alert = new AlertDialog.Builder(LoginActivity.this);
//                                    Alert.setMessage("Invalid Username or Password");
//                                    Alert.setPositiveButton("OK", null);
//                                    etPassword.setText("");
//                                    Alert.create().show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    };

                    //HUYANH call LoginRequest
//                    LoginRequest loginRequest = new LoginRequest(Username, password, responseListener);
//                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
//                    queue.add(loginRequest);
                }
            });
        }

    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, "couldn't sign in whoops lol",
                                    Toast.LENGTH_SHORT).show();
                        } else {
//                            create an intent to store Username information for UserActivity
                            Intent userIntent = new Intent(LoginActivity.this, HomeParentActivity.class);
                            LoginActivity.this.startActivity(userIntent);
                        }
                    }
                });
        // [END sign_in_with_email]
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

    public static void clearUsername()
    {
        pe.clear();
        pe.commit();
    }

    public static String getUsername() { return Username;}
    public static void setNewPassword(String x)
    {
        Password = x;
    }
    public static String getPassword() { return Password; }
}