package com.example.newsapp2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.preference.Preference;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    private static int SPLASH_TIME_OUT = 4000;
    public static boolean previouslyStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
                SharedPreferences.Editor edit = prefs.edit();
                System.out.println(previouslyStarted);
                if (!previouslyStarted) {
                    System.out.println("condition");
                    edit.putBoolean(getString(R.string.pref_previously_started), true).apply();
                    SourcesActivity.aBoolean=false;
                    Intent i = new Intent(MainActivity.this, SourcesActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    SourcesActivity.aBoolean=true;
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}