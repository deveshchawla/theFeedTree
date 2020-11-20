package com.example.newsapp2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {
    String API_KEY = "f6a4db699db04005b2c6215915c2c1f6";
    RecyclerView recyclerNews;
    String query;
    Intent settingsIntent;

    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

    static final String KEY_AUTHOR = "author";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_URL = "url";
    static final String KEY_URLTOIMAGE = "urlToImage";
    static final String KEY_PUBLISHEDAT = "publishedAt";
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.home, menu);
        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                settingsIntent = new Intent(SearchActivity.this, SearchActivity.class);

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
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences(SettingsActivity.PACKAGE, MODE_PRIVATE);
        boolean switchPref =sharedPreferences.getBoolean(SettingsActivity.KEY_PREF_SWITCH,false);
        if(switchPref)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        query = getIntent().getStringExtra("query");
        getSupportActionBar().setTitle("Result: "+query);
        query= query.replace(" ", "+").toLowerCase();
        recyclerNews = findViewById(R.id.recycler2);
        shimmerFrameLayout= findViewById(R.id.shimmer_view);
        shimmerFrameLayout.startShimmer();



        if (com.example.newsapp2.Function.isNetworkAvailable(this)) {
            DownloadNews newsTask = new DownloadNews();
            newsTask.execute();

        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }


    }
    class DownloadNews extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            String xml = "";

            String urlParameters = "";
            xml = com.example.newsapp2.Function.excuteGet("https://newsapi.org/v2/everything?q="+query+"&sortBy=publishedAt&language=en&apiKey="+API_KEY, urlParameters);


            return  xml;
        }

        @Override
        protected void onPostExecute(String xml) {

            if(xml.length()>10){

                try {
                    JSONObject jsonResponse = new JSONObject(xml);
                    JSONArray jsonArray = jsonResponse.optJSONArray("articles");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<>();
                        map.put(KEY_AUTHOR, jsonObject.optString(KEY_AUTHOR));
                        map.put(KEY_TITLE, jsonObject.optString(KEY_TITLE));
                        map.put(KEY_DESCRIPTION, jsonObject.optString(KEY_DESCRIPTION));
                        map.put(KEY_URL, jsonObject.optString(KEY_URL));
                        map.put(KEY_URLTOIMAGE, jsonObject.optString(KEY_URLTOIMAGE));
                        map.put(KEY_PUBLISHEDAT, jsonObject.optString(KEY_PUBLISHEDAT));
                        dataList.add(map);
                    }
                } catch (JSONException e) {
                    Toast.makeText(SearchActivity.this, "Unexpected error", Toast.LENGTH_SHORT).show();
                }
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerNews.setLayoutManager(layoutManager);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                MyAdapter adapter = new MyAdapter(SearchActivity.this, dataList);
                recyclerNews.setAdapter(adapter);

                recyclerNews.addOnItemTouchListener(new RecyclerTouchListner(SearchActivity.this, recyclerNews, new ClickListner() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent i = new Intent(SearchActivity.this, com.example.newsapp2.DetailsActivity.class);
                        i.putExtra("url", dataList.get(+position).get(KEY_URL));
                        i.putExtra("title",dataList.get(+position).get(KEY_TITLE));
                        i.putExtra("flag",2);
                        startActivity(i);
                    }

                    @Override
                    public void onLongClick(View child, int childAdapterPosition) {

                    }
                }));



            }else{
                Toast.makeText(SearchActivity.this, "No news found", Toast.LENGTH_SHORT).show();
            }
        }



    }




    static class RecyclerTouchListner implements RecyclerView.OnItemTouchListener{

        private ClickListner clickListner;
        private GestureDetector gestureDetector;

        public RecyclerTouchListner(Context context, final RecyclerView recyclerView, final ClickListner clickListner) {
            this.clickListner = clickListner;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child= recyclerView.findChildViewUnder(e.getX(),e.getY());
                    if (child!=null && clickListner!=null){
                        clickListner.onLongClick(child, recyclerView.getChildAdapterPosition(child)
                        );
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
            View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
            if (child!=null && clickListner!=null && gestureDetector.onTouchEvent(motionEvent)){
                clickListner.onClick(child,recyclerView.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {

        }
    }

    public static interface ClickListner {
        public void onClick(View view, int position);

        void onLongClick(View child, int childAdapterPosition);
    }
    }



