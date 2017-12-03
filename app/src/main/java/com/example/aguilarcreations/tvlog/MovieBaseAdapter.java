package com.example.aguilarcreations.tvlog;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Work on 10/15/17.
 */

public class MovieBaseAdapter extends BaseAdapter {
    private Activity activity;
    public ArrayList<Item> data;
    Resources res;
    private static LayoutInflater inflater = null;


    public MovieBaseAdapter(Activity a, ArrayList<Item> d, Resources resources){
        activity = a;
        data = d;
        res = resources;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(data != null && i < data.size()){
            return data.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.cell_movie, viewGroup, false);
        }

        Item movie = data.get(i);
        TextView title_tv = view.findViewById(R.id.main_txt);
        title_tv.setText(movie.getTitle());
        return view;
    }
}
