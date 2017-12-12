package com.example.aguilarcreations.tvlog;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import layout.ShowDetailsFragment;
import layout.WatchlistFragment;

/**
 * Created by Work on 11/16/17.
 */

public class EpisodeBaseAdapter extends BaseAdapter {
    private MainActivity activity;
    public ArrayList<Episode> data;
    Resources res;
    private static LayoutInflater inflater = null;

    public EpisodeBaseAdapter(Activity a, ArrayList<Episode> d, Resources resources){
        activity = (MainActivity) a;
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


    public class SeasonHolder{
        public TextView title;
        public ImageButton mark_finshed;
    }

    public class EpisodeHolder{
        public TextView title;
        public ImageButton epi_details_button;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Episode episode = data.get(i);
        //SeasonHolder seasonHolder;
        EpisodeHolder episodeHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.cell_episode, viewGroup, false);
            episodeHolder = new EpisodeHolder();
            episodeHolder.title = view.findViewById(R.id.cell_episode_title);
            episodeHolder.epi_details_button = view.findViewById(R.id.epi_details_button);
            view.setTag(episodeHolder);
        }
        episodeHolder = (EpisodeHolder) view.getTag();
        final Episode epi = data.get(i);
        episodeHolder.title.setText(epi.getEpi_number() + ". " +episode.getTitle());
        episodeHolder.epi_details_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEpisodeDetails(epi);
            }
        });

        return view;
    }

//    private boolean isRecyclable(View v, boolean isSeason){
//        if(isSeason) {
//            if (v.getTag().getClass() == SeasonHolder.class) {
//                return true;
//            }
//        }else{
//            if(v.getTag().getClass() == EpisodeHolder.class){
//                return true;
//            }
//        }
//        return false;
//    }

    private void openEpisodeDetails(Episode episode){
        Log.d("Alejandro", "openEpisodeDetails episode: " + episode.toString());
        activity.onEpisodeSelected(episode);
    }

}
