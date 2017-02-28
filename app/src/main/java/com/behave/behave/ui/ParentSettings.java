package com.behave.behave.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.behave.behave.R;

/**
 * Created by Calvin on 2/20/2017.
 */

public class ParentSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_settings);

        final Button bEditChild = (Button) findViewById(R.id.bEditChild);
        final Button bChangeEmail = (Button) findViewById(R.id.bChangeEmail);
        final Button bChangePassword = (Button) findViewById(R.id.bChangePassword);
        final Button bDeleteAccount = (Button) findViewById(R.id.bDeleteAccount);

        //edit child is clicked
        bEditChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //change email clicked
        bChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = ParentSettings.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                android.app.AlertDialog.Builder changeEmail = new android.app.AlertDialog.Builder(ParentSettings.this);
                changeEmail.setTitle("Change Email");

                final EditText newEmail = new EditText(ParentSettings.this);
                newEmail.setInputType(InputType.TYPE_CLASS_TEXT);
                newEmail.setHint("Enter New Email");

                layout.addView(newEmail);

                final EditText verifyEmail = new EditText(ParentSettings.this);
                verifyEmail.setInputType(InputType.TYPE_CLASS_TEXT);
                verifyEmail.setHint("Re-enter New Email");

                layout.addView(verifyEmail);

                changeEmail.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(ParentSettings.this);
                        alert.setTitle("Change Password");
                        String email = newEmail.getText().toString();
                        String vEmail = verifyEmail.getText().toString();

                        //CHANGE EMAIL IN FIREBASE
                        if(email.equals(vEmail) && isValidEmail(email))
                        {
                            //change EMAIL here in FIREHERE
                            alert.setMessage("Email changed");
                        }
                        else
                        {
                            alert.setMessage("Email do not match or incorrect format");
                        }
                        alert.setPositiveButton("Ok", null);
                        alert.create().show();
                    }
                });
                changeEmail.setNegativeButton("Cancel", null);
                changeEmail.setView(layout);
                changeEmail.show();

            }
        });

        //when change password is clicked
        bChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = ParentSettings.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                android.app.AlertDialog.Builder changePW = new android.app.AlertDialog.Builder(ParentSettings.this);
                changePW.setTitle("Change Password");

                final EditText oldPassword = new EditText(ParentSettings.this); //<-------
                oldPassword.setHint("Old Password");
                oldPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                final String oPassword = oldPassword.getText().toString();
                layout.addView(oldPassword);

                final EditText newPassword = new EditText(ParentSettings.this);
                newPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                newPassword.setHint("Enter New Password");
                final String nPassword = newPassword.getText().toString();
                layout.addView(newPassword);

                final EditText verifyPassword = new EditText(ParentSettings.this);
                verifyPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                verifyPassword.setHint("Re-enter New Password");
                final String vPassword = verifyPassword.getText().toString();
                layout.addView(verifyPassword);

                changePW.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(ParentSettings.this);
                        alert.setTitle("Change Password");

                        //CHECK PASSWORD FROM FIREBASE HERE
                        if(nPassword.equals(vPassword))  //INCLUDE CHECK OLD PASSWORD WITH DB
                        {
                            //change password here in FIREHERE
                            alert.setMessage("Password changed");
                        }
                        else
                        {
                            alert.setMessage("Wrong Password");
                        }
                        alert.setPositiveButton("Ok", null);
                        alert.create().show();
                    }
                });
                changePW.setNegativeButton("Cancel", null);
                changePW.setView(layout);
                changePW.show();
            }
        });

        //delete account
        bDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ParentSettings.this);
                alert.setTitle("DELETE ACCOUNT");
                alert.setMessage("Do you want to Delete Account?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder alertConfirm = new AlertDialog.Builder(ParentSettings.this);
                        alertConfirm.setMessage("Account has been deleted!");
                        alertConfirm.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //INSERT DELETE FROM FIREBASE HERE

                                Intent addChildNow = new Intent(ParentSettings.this, MainActivity.class);
                                ParentSettings.this.startActivity(addChildNow);
                            }
                        });
                        alertConfirm.create().show();
                    }
                });
                alert.setNeutralButton("No", null);
                alert.create().show();
            }
        });
    }


    //Menu option
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.parent_menu, menu);
        return super.onCreateOptionsMenu(menu);
        //  return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mHome:
                Intent homeIntent = new Intent(this, HomeParentActivity.class);
                this.startActivity(homeIntent);
                break;
//            case R.id.mChild:
//                break;
            case R.id.mRedeemNotification:
                Intent redeemIntent = new Intent(this, ParentRedeemPageListChild.class);
                this.startActivity(redeemIntent);
                break;
//            case R.id.mPrizeList:
//                break;
            case R.id.mSetting:
                break;
            case R.id.mAbout:
                Intent aboutIntent = new Intent(this, MenuAbout.class);
                this.startActivity(aboutIntent);
                break;
            case R.id.mLogOut:
                LoginActivity.clearUsername();
                Intent logoutIntent = new Intent(this, MainActivity.class);
                this.startActivity(logoutIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //Menu option end

    //Email checker
    public final static boolean isValidEmail(CharSequence target)
    {
        if (target == null)
            return false;
        else
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
