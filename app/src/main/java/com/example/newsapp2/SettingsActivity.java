package com.example.newsapp2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

public class SettingsActivity extends AppCompatActivity {
    public static final String KEY_PREF_SWITCH="theme_mode";
    public static final String PACKAGE="com.example.newsapp2";
    public static final String KEY_PREF_LIST="sources";
    public static final String KEY_PREF_CACHE="cache_clear";
    public static boolean switchPref;
    public static SharedPreferences sharedPreferences;
    TextView selectSources,clearCache;
    Switch mode;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,HomeActivity.class));
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        clearCache= findViewById(R.id.clear_cache);
        selectSources = findViewById(R.id.select_source);
        mode = findViewById(R.id.mode_switch);
        sharedPreferences = getSharedPreferences(SettingsActivity.PACKAGE, MODE_PRIVATE);
        switchPref =sharedPreferences.getBoolean(KEY_PREF_SWITCH,false);
        mode.setChecked(switchPref);
        if(switchPref)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        clearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCache(getApplicationContext());
                Snackbar.make(v,"Cache Cleared",Snackbar.LENGTH_SHORT).show();
            }
        });

        selectSources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,SourcesActivity.class));
            }
        });

        mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(SettingsActivity.this, "ischecked"+isChecked, Toast.LENGTH_SHORT).show();
                if(isChecked) {
                    sharedPreferences.edit().putBoolean(KEY_PREF_SWITCH,true).apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                    finish();
                }
                else{
                    sharedPreferences.edit().putBoolean(KEY_PREF_SWITCH,false).apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            startActivity(new Intent(this,HomeActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public static void deleteCache(Context context){
        try{
            File dir = context.getCacheDir();
            deleteDir(dir);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir){
        if(dir!=null && dir.isDirectory()){
            String[] children= dir.list();
            for (int i=0;i<children.length;i++){
                boolean success = deleteDir(new File(dir,children[i]));
                if(!success){
                    return false;
                }
            }
            return dir.delete();
        }else {
            return false;
        }
    }

}
