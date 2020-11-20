package com.example.newsapp2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SourcesActivity extends AppCompatActivity {

    public static boolean aBoolean;
    private ListView listCheckBox;
    public static final String KEY_PREF_POSITION="position";
    private String[] source={"ABC News","BBC News","BBC Sports","Bild","Bloomberg","Business Insider","Buzzfeed","CNBC","CNN",
            "Engadget","ESPN","Fox News","IGN","Mashable","MTV News","National Geographic","New Scientist","Recode","Redddit",
            "TechCrunch","TechRadar","The Hindu","The Times of India","The Verge","Time","Wired"};
    ArrayList<String> sources = new ArrayList<String>();
    ArrayList<Integer> values = new ArrayList<Integer>();
    private ArrayList<InfoRowData> infodata;
    SharedPreferences sp;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            saveSources();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources);
        sp = getSharedPreferences(SettingsActivity.PACKAGE, MODE_PRIVATE);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton2);

        if(aBoolean==false) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
            floatingActionButton.show();
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveSources();
                    startActivity(new Intent(SourcesActivity.this, HomeActivity.class));
                }
            });
        }


        listCheckBox = findViewById(R.id.listCheckBox);
        infodata = new ArrayList<InfoRowData>();
        for (int i = 0; i < source.length; i++) {
            infodata.add(new InfoRowData(false, i));
        }
        listCheckBox.invalidate();
        listCheckBox.setAdapter(new MyAdapter());
    }


    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return source.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View row;
            row = View.inflate(getApplicationContext(), R.layout.source_row, null);
            TextView tvContent= row.findViewById(R.id.tvContent);
            tvContent.setText(source[position]);
//            String s =sp.getString(KEY_PREF_POSITION,"");
//            s=s.replace("[","").replace("]","");
//            List<Integer> myList = new ArrayList<Integer>(Arrays.asList(s.split(",")));
            final CheckBox cb = row.findViewById(R.id.chbContent);
//            for(int i=0;i<)
            cb.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (infodata.get(position).isclicked) {
                        infodata.get(position).isclicked = false;
                        sources.remove(source[position]);
//                        values.remove(position);
                    } else {
                        infodata.get(position).isclicked = true;
                        sources.add(source[position]);
//                        values.add(position);
                    }

                    for(int i=0;i<infodata.size();i++)
                    {
                        if (infodata.get(i).isclicked)
                        {
                            String jkl=sources.toString()
                                    .replace(" ","-")
                                    .replaceAll("[\\[\\]\\(\\)]", "")
                                    .toLowerCase()
                                    .replace(",-",",");
                            System.out.println("Selectes Are == "+ jkl);
                            sp.edit().putString(SettingsActivity.KEY_PREF_LIST,jkl).apply();
                        }
                        else{
//                            sources.remove(source[i]);
//                            System.out.println("Sources is "+sources);
                        }
                    }
                }
            });

            if (infodata.get(position).isclicked) {

                cb.setChecked(true);
            }
            else {
                cb.setChecked(false);
            }
            return row;
        }
    }

    public void saveSources(){
        String jkl;
        jkl = sources.toString().replace(" ", "-")
                .replaceAll("[\\[\\]\\(\\)]", "")
                .toLowerCase()
                .replace(",-", ",");
        sp.edit().putString(SettingsActivity.KEY_PREF_LIST, jkl).apply();
        Toast.makeText(SourcesActivity.this, "string is " + jkl, Toast.LENGTH_SHORT).show();
        sp.edit().putString(KEY_PREF_POSITION,values.toString()).apply();

    }
}



