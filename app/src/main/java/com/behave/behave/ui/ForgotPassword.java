package com.behave.behave.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.behave.behave.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Calvin on 2/17/2017.
 */


public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Forgot Password");
        setContentView(R.layout.activity_forgot_password);

        final EditText etForgotPasswordEmail = (EditText) findViewById(R.id.etForgotPasswordEmail);
        final Button bForgotPasswordOK = (Button) findViewById(R.id.bForgotPasswordOK);
        final Button bBack = (Button) findViewById(R.id.bBack);

        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ForgotPassword.this, MainActivity.class);
                ForgotPassword.this.startActivity(mainIntent);
            }
        });

        bForgotPasswordOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etForgotPasswordEmail.getText().toString();
                AlertDialog.Builder alert = new AlertDialog.Builder(ForgotPassword.this);
                alert.setTitle("Forgot Password");
                if(email.isEmpty() || !isValidEmail(email)) {
                    alert.setMessage("Invalid entry");
                    alert.setPositiveButton("OK", null);
                }
                else {
                    alert.setMessage("If Email is valid, reset password will be sent to your email at " + email);
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email);
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent mainIntent = new Intent(ForgotPassword.this, MainActivity.class);
                            ForgotPassword.this.startActivity(mainIntent);
                        }
                    });
                }
                alert.create().show();
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
}
