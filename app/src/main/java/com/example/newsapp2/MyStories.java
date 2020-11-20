package com.example.newsapp2;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class MyStories extends Fragment {
    String API_KEY="f6a4db699db04005b2c6215915c2c1f6";
    RecyclerView recyclerNews;
    public static String NEWS_SOURCE;
    ShimmerFrameLayout shimmerFrameLayout;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

    static final String KEY_AUTHOR = "author";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_URL = "url";
    static final String KEY_URLTOIMAGE = "urlToImage";
    static final String KEY_PUBLISHEDAT = "publishedAt";


    public MyStories() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_stories, container, false);
        SharedPreferences sp = getActivity().getSharedPreferences(SettingsActivity.PACKAGE,Context.MODE_PRIVATE);
        NEWS_SOURCE = sp.getString(SettingsActivity.KEY_PREF_LIST,"the-verge,time");
//        Toast.makeText(getActivity(),NEWS_SOURCE,Toast.LENGTH_SHORT).show();
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
            if(NEWS_SOURCE==null){
                NEWS_SOURCE="CNN";
            }
            xml = com.example.newsapp2.Function.excuteGet("https://newsapi.org/v2/everything?sources="+NEWS_SOURCE+"&language=en&apiKey="+API_KEY, urlParameters);
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
                recyclerNews.addOnItemTouchListener(new TopStories.RecyclerTouchListner(getContext(), recyclerNews, new TopStories.ClickListner() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent i = new Intent(getActivity(), com.example.newsapp2.DetailsActivity.class);
                        i.putExtra("url", dataList.get(+position).get(KEY_URL));
                        i.putExtra("title",dataList.get(+position).get(KEY_TITLE));
                        i.putExtra("flag",0);
                        startActivityForResult(i,100);
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
}