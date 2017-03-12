package com.behave.behave.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.behave.behave.R;

public class ChildAbout extends AppCompatActivity {
    String childId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_about);

        final Intent childNameIntent = getIntent();
        childId = childNameIntent.getStringExtra("childId");
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


