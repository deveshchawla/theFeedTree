package com.example.newsapp2;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class TopStories extends Fragment {
    String API_KEY="f6a4db699db04005b2c6215915c2c1f6";
    RecyclerView recyclerNews;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

    static final String KEY_AUTHOR = "author";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_URL = "url";
    static final String KEY_URLTOIMAGE = "urlToImage";
    static final String KEY_PUBLISHEDAT = "publishedAt";
    ShimmerFrameLayout shimmerFrameLayout;

    public TopStories() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_stories, container, false);
        if(com.example.newsapp2.Function.isNetworkAvailable(getContext()))
        {
            DownloadNews newsTask = new DownloadNews();
            newsTask.execute();

        }else{
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

        recyclerNews = view.findViewById(R.id.listNews);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerNews.setLayoutManager(layoutManager);
        return view;
    }



    class DownloadNews extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            String xml = "";

            String urlParameters = "";
            xml = com.example.newsapp2.Function.excuteGet("https://newsapi.org/v2/top-headlines?country=in&apiKey="+API_KEY, urlParameters);
            return  xml;
        }

        @Override
        protected void onPostExecute(String xml) {

            if(xml!=null && xml.length()>10){ // Just checking if not empty

                try {
                    JSONObject jsonResponse = new JSONObject(xml);
                    JSONArray jsonArray = jsonResponse.optJSONArray("articles");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_AUTHOR, jsonObject.optString(KEY_AUTHOR).toString());
                        map.put(KEY_TITLE, jsonObject.optString(KEY_TITLE).toString());
                        map.put(KEY_DESCRIPTION, jsonObject.optString(KEY_DESCRIPTION).toString());
                        map.put(KEY_URL, jsonObject.optString(KEY_URL).toString());
                        map.put(KEY_URLTOIMAGE, jsonObject.optString(KEY_URLTOIMAGE).toString());
                        map.put(KEY_PUBLISHEDAT, jsonObject.optString(KEY_PUBLISHEDAT).toString());
                        dataList.add(map);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                }


                com.example.newsapp2.MyAdapter adapter = new com.example.newsapp2.MyAdapter(getActivity(), dataList);
                recyclerNews.setAdapter(adapter);



                recyclerNews.addOnItemTouchListener(new RecyclerTouchListner(getContext(), recyclerNews, new ClickListner() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent i = new Intent(getActivity(), com.example.newsapp2.DetailsActivity.class);
                        i.putExtra("url", dataList.get(+position).get(KEY_URL));
                        i.putExtra("title",dataList.get(+position).get(KEY_TITLE));
                        i.putExtra("flag",0);
                        startActivityForResult(i, 100);
                    }

                    @Override
                    public void onLongClick(View child, int childAdapterPosition) {

                    }
                }));

            }else{
                Toast.makeText(getContext(), "No news found", Toast.LENGTH_SHORT).show();
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
