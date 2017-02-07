package com.behave.behave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class Register extends AppCompatActivity
{
    private static String firstName;
    private static String lastName;
    private static String email;

    @Override
    protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_register);

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

                if (!isValidEmail(email)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setMessage("Not a valid Email address").setNegativeButton("Retry", null).create().show();
                    valid = false;
                    etEmail.requestFocus();
                }
                if (!notEmpty(firstName) || !notEmpty(lastName)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setMessage("Name cannot be blank").setNegativeButton("Retry", null).create().show();
                    valid = false;
                    etFirstName.requestFocus();
                }
                if (!notEmpty(Username)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setMessage("Not a valid Username").setNegativeButton("Retry", null).create().show();
                    valid = false;
                    etUsername.requestFocus();
                }
                if (!notEmpty(password)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setMessage("Not a valid password").setNegativeButton("Retry", null).create().show();
                    valid = false;
                    etPassword.requestFocus();
                }

                Response.Listener<String> responseListener = new Response.Listener<String>()
                {

                    @Override
                    public void onResponse(String response) {
                        if (valid == true) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    Intent intent = new Intent(Register.this, LoginActivity.class);
                                    Register.this.startActivity(intent);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                    builder.setMessage("Username taken").setNegativeButton("Retry", null).create().show();
                                    etUsername.requestFocus();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };

                if(password.equals(verifyPassword))
                {
                    //HUYANH REGISTER TO DATA BASE
//                    RegisterRequest registerRequest = new RegisterRequest(firstName,lastName,email,dateOfBirth,Username,password, responseListener );
//                    RequestQueue queue = Volley.newRequestQueue(Register.this);
//                    queue.add(registerRequest);
                }
                else
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
            }
        });

    }

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
