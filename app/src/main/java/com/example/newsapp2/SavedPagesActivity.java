package com.example.newsapp2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class SavedPagesActivity extends AppCompatActivity {
    public static final String DATABASE_NAME = "Savepage.db";
    public static final String TABLE_NAME="pages_table";
    static final String KEY_TITLE = "title";
    static final String KEY_URL = "url";

    private RecyclerView recyclerView;
    private ArrayList<InfoRowData> infodata;
    ArrayList<SaveClass> initNames = new ArrayList<>();
    SQLiteDatabase mDatabase;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_pages);
        getSupportActionBar().setTitle("Saved Pages");
        try {
            mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            db = new DatabaseHelper(this);
            //listView = findViewById(R.id.list);


            showSaved();
        } catch (Exception e) {
        e.printStackTrace();
        }

        recyclerView.addOnItemTouchListener(new TopStories.RecyclerTouchListner(getApplicationContext(), recyclerView, new TopStories.ClickListner() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(getApplicationContext(), com.example.newsapp2.DetailsActivity.class);
                i.putExtra("url", initNames.get(+position).getUrl());
                i.putExtra("title",initNames.get(+position).getTitle());
                startActivity(i);
            }

            @Override
            public void onLongClick(View child, int childAdapterPosition) {

            }
        }));
    }
    private void showSaved(){
        Cursor cursorTitle = mDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+";", null);
        if(cursorTitle.moveToFirst()) {
            do {
                initNames.add(new SaveClass(cursorTitle.getString(0),
                        cursorTitle.getString(1)));
            } while (cursorTitle.moveToNext());
        }
        cursorTitle.close();
        recyclerView = findViewById(R.id.title_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MyDbAdapter adapter = new MyDbAdapter(this,initNames);
        infodata = new ArrayList<InfoRowData>();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            infodata.add(new InfoRowData(false, i));
        }
        recyclerView.setAdapter(adapter);
    }


}
