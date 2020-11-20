package com.example.newsapp2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuInflater;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.SearchView;
import com.example.newsapp2.main.SectionsPagerAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;


import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Intent settingsIntent;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    static final String KEY_AUTHOR = "author";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_URL = "url";
    static final String KEY_URLTOIMAGE = "urlToImage";
    static final String KEY_PUBLISHEDAT = "publishedAt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(SettingsActivity.PACKAGE, MODE_PRIVATE);
        boolean switchPref =sharedPreferences.getBoolean(SettingsActivity.KEY_PREF_SWITCH,false);
        if(switchPref)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ShimmerFrameLayout shimmerFrameLayout= findViewById(R.id.shimmer_view);
        shimmerFrameLayout.setVisibility(View.GONE);
        PreferenceManager.setDefaultValues(this,R.xml.preference,false);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tablayout);
        tabs.setupWithViewPager(viewPager);
//        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode ==100 && null != data) {
//            boolean b = data.getBooleanExtra("change", false);
//            if(b) {
//                startActivity(new Intent(HomeActivity.this, HomeActivity.class));
//                finish();
//            }
//        }
//    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.home, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                settingsIntent = new Intent(HomeActivity.this, SearchActivity.class);
                settingsIntent.putExtra("query",searchView.getQuery().toString());
                startActivity(settingsIntent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent = new Intent(this, HomeActivity.class);
        Intent i = new Intent(this, CategoryActivity.class);
        if (id == R.id.nav_home) {

            startActivity(intent);
        } else if (id == R.id.nav_business) {

            i.putExtra("source", "financial-post");
            i.putExtra("title", "Business");
            startActivity(i);
        } else if (id == R.id.nav_entertainment) {
            i.putExtra("title", "Entertainment");
            i.putExtra("source", "entertainment-weekly");
            startActivity(i);
        } else if (id == R.id.nav_health) {
            i.putExtra("title", "Health");
            i.putExtra("source", "medical-news-today");
            startActivity(i);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_ratings) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle("Rate the App");
            View customView = getLayoutInflater().inflate(R.layout.ratingbar, null);
            builder.setView(customView);
            builder.setCancelable(true);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (id == R.id.nav_science) {
            i.putExtra("source", "new-scientist");
            i.putExtra("title", "Science");
            startActivity(i);
        } else if (id == R.id.nav_sports) {
            i.putExtra("title", "Sports");
            i.putExtra("source", "bbc-sport,fox-sports");
            startActivity(i);
        } else if (id == R.id.nav_technology) {
            i.putExtra("title", "Technolgy");
            i.putExtra("source", "engadget,techradar,the-verge,ign");
            startActivity(i);
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
        } else if (id == R.id.saved_pages) {
            startActivity(new Intent(this, SavedPagesActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}