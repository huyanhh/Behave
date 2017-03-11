package com.behave.behave.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.behave.behave.R;


/* Created by Irish Marquez */

public class SplashScreen extends Activity {

    ImageView iv_teamLogo;
    private static int SPLASH_TIME_OUT = 2000;  // length of time splash screen is displayed
    ProgressBar progressBar;
    CountDownTimer countDownTimer;
    int progress = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);       // hides the action bar
        setContentView(R.layout.activity_splash_screen);

        addTeamLogo();
        createProgressBar();
    }   // end onCreate


    /* ADDS THE TEAM LOGO TO THE SCREEN */
    public void addTeamLogo() {
        iv_teamLogo = (ImageView) findViewById(R.id.tv_teamlogo);
        iv_teamLogo.setImageResource(R.drawable.teamlogo);
    }


    /* CREATES THE PROGRESS BAR AND
        LAUNCHES THE WELCOME SCREEN FOR 2 SECS BEFORE GOING TO SECOND SCREEN */
    public void createProgressBar() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        countDownTimer = new CountDownTimer(/*SPLASH_TIME_OUT*/2000, 1000) {
            @Override
            /* WHAT HAPPENS EVERY SECOND */
            public void onTick(long l) {
                progress += 50;
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                Intent homeIntent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(homeIntent);
            }
        };
        countDownTimer.start();
    }

}   // end SplashScreen
