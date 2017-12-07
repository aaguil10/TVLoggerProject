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
    public ArrayList<ShowDetailsFragment.EpiSlot> data;
    Resources res;
    private static LayoutInflater inflater = null;

    public EpisodeBaseAdapter(Activity a, ArrayList<ShowDetailsFragment.EpiSlot> d, Resources resources){
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

        ShowDetailsFragment.EpiSlot epiSlot = data.get(i);
        SeasonHolder seasonHolder;
        EpisodeHolder episodeHolder;

        if (view == null || !isRecyclable(view, epiSlot.isSeason)) {
            if(epiSlot.isSeason){
                view = inflater.inflate(R.layout.cell_season, viewGroup, false);
                seasonHolder = new SeasonHolder();
                seasonHolder.title = view.findViewById(R.id.cell_season_title);
                seasonHolder.mark_finshed = view.findViewById(R.id.mark_finshed);
                view.setTag(seasonHolder);
            }else {
                view = inflater.inflate(R.layout.cell_episode, viewGroup, false);
                episodeHolder = new EpisodeHolder();
                episodeHolder.title = view.findViewById(R.id.cell_episode_title);
                episodeHolder.epi_details_button = view.findViewById(R.id.epi_details_button);
                view.setTag(episodeHolder);
            }
        }


        if(epiSlot.isSeason){
            seasonHolder = (SeasonHolder) view.getTag();
            seasonHolder.title.setText("Season " + epiSlot.season_num);
        }else {
            episodeHolder = (EpisodeHolder) view.getTag();
            final Episode episode = data.get(i).episode;
            episodeHolder.title.setText(epiSlot.epi_num + ". " +episode.getTitle());
            episodeHolder.epi_details_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openEpisodeDetails(episode);
                }
            });
        }
        return view;
    }

    private boolean isRecyclable(View v, boolean isSeason){
        if(isSeason) {
            if (v.getTag().getClass() == SeasonHolder.class) {
                return true;
            }
        }else{
            if(v.getTag().getClass() == EpisodeHolder.class){
                return true;
            }
        }
        return false;
    }

    private void openEpisodeDetails(Episode episode){
        activity.onEpisodeSelected(episode);
    }

}
