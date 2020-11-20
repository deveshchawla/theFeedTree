package com.example.newsapp2;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;


class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;

    public MyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);
        MyHolder myHolder = new MyHolder(v);
        return myHolder;
    }


    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        holder.galleryImage.setId(position);
        holder.author.setId(position);
        holder.title.setId(position);
        holder.sdetails.setId(position);
        holder.time.setId(position);

        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);

        try {
            if(song.get(HomeActivity.KEY_AUTHOR)=="null" || song.get(HomeActivity.KEY_AUTHOR).length()>15){
                holder.author.setText("");
            }else {
                holder.author.setText(song.get(HomeActivity.KEY_AUTHOR));
            }
            holder.title.setText(song.get(HomeActivity.KEY_TITLE));
            holder.time.setText(convertTime(song.get(HomeActivity.KEY_PUBLISHEDAT)));
//            holder.time.setText(song.get(HomeActivity.KEY_PUBLISHEDAT));
            if(song.get(HomeActivity.KEY_DESCRIPTION)=="null")
                holder.sdetails.setVisibility(View.GONE);
            else
                holder.sdetails.setText(song.get(HomeActivity.KEY_DESCRIPTION));

            if (song.get(HomeActivity.KEY_URLTOIMAGE).toString().length() < 5) {
                holder.galleryImage.setVisibility(View.GONE);
            } else {
                Picasso.get()
                        .load(song.get(HomeActivity.KEY_URLTOIMAGE).toString())
                        .resize(300, 200)
                        .into(holder.galleryImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }


    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView title, sdetails, author, time;
        ImageView galleryImage;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            sdetails = itemView.findViewById(R.id.sdetails);
            author = itemView.findViewById(R.id.author);
            time =  itemView.findViewById(R.id.time);
            galleryImage =  itemView.findViewById(R.id.galleryImage);
        }
    }
    private String convertTime(String time){
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd-MMM hh:mm a");
        java.util.Date date =null;
        try{
            date = input.parse(time);

        }catch(ParseException e){
            e.printStackTrace();
        }
        String convertedDate = output.format(date);
        return convertedDate;
    }
}


