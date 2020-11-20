package com.example.newsapp2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class DetailsActivity extends AppCompatActivity {
    WebView webView;
    String url = "",title="";
    TextView open_original,save_later;
    DatabaseHelper myDb;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    Switch mode_switch;
    public static BottomSheetBehavior bottomSheetBehavior;


    ImageView bottom_back,bottom_share,bottom_option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        final View bottomSheet = findViewById(R.id.bottom_sheet);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setMax(100);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        sharedPreferences = getSharedPreferences(SettingsActivity.PACKAGE, MODE_PRIVATE);

        bottom_option=findViewById(R.id.bottom_option);
        bottom_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState()==BottomSheetBehavior.STATE_COLLAPSED)
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                else
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        bottom_back= findViewById(R.id.bottom_back);
        bottom_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (getIntent().getIntExtra("flag",0)){
                    case 0:
                        startActivity(new Intent(DetailsActivity.this,HomeActivity.class));
                        finish();
                        break;
                    case 1:
                        startActivity(new Intent(DetailsActivity.this,CategoryActivity.class));
                        finish();
                        break;
                    case 2:
                        startActivity(new Intent(DetailsActivity.this,SearchActivity.class));
                        finish();
                        break;
                }
            }
        });

        bottom_share=findViewById(R.id.bottom_share);
        bottom_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareCompat.IntentBuilder.from(DetailsActivity.this).setType("text/plain").setChooserTitle("Share this Article")
                        .setText(url).startChooser();
            }
        });


        save_later = findViewById(R.id.save_later);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        save_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isInserted = myDb.insertData(title,url);
                if(isInserted)
                    Toast.makeText(DetailsActivity.this,"Page Saved",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(DetailsActivity.this,"Page not Saved",Toast.LENGTH_LONG).show();

            }
        });


        open_original = findViewById(R.id.open_original);
        open_original.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });


        boolean switchPref =sharedPreferences.getBoolean(SettingsActivity.KEY_PREF_SWITCH,false);
        mode_switch =findViewById(R.id.mode_switch);
        mode_switch.setChecked(switchPref);
        if(switchPref) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            bottomSheet.setBackgroundColor(Color.DKGRAY);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            bottomSheet.setBackgroundColor(Color.WHITE);
        }
        mode_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(DetailsActivity.this, "ischecked"+isChecked, Toast.LENGTH_SHORT).show();
                if(isChecked) {
                    sharedPreferences.edit().putBoolean(SettingsActivity.KEY_PREF_SWITCH,true).apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                    bottomSheet.setBackgroundColor(Color.DKGRAY);
                    startActivity(getIntent());
                    finish();
                }
                else{
                    sharedPreferences.edit().putBoolean(SettingsActivity.KEY_PREF_SWITCH,false).apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    bottomSheet.setBackgroundColor(Color.WHITE);
                    startActivity(getIntent());
                    finish();
                }
            }
        });
        myDb = new DatabaseHelper(this);
        webView = findViewById(R.id.webView);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        WebSettings webSettings =webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient());

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }
        });
    }
}