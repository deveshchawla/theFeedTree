package com.example.newsapp2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyDbAdapter extends RecyclerView.Adapter<MyDbAdapter.MyHolder> {

    private ArrayList<SaveClass> data;
    private Context context;

    public MyDbAdapter(Context context, ArrayList<SaveClass> data) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = (View) LayoutInflater.from(context).inflate(R.layout.item_names, viewGroup, false);
        MyHolder myHolder = new MyHolder(v);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder viewHolder, int position) {
        Log.d("abcdef", String.valueOf(data.get(position).getTitle()));
        SaveClass saveClass = data.get(position);
        viewHolder.title.setText(String.valueOf(saveClass.getTitle()));
        viewHolder.url.setText(saveClass.getUrl());
    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public CheckedTextView title;
        public TextView url;

        public MyHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_content);
            url = itemView.findViewById(R.id.url_content);
        }
    }
}
