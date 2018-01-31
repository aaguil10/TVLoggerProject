package com.example.aguilarcreations.tvlog;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
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

    public class EpisodeHolder{
        public TextView title;
        public ImageButton epi_details_button;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final Episode episode = data.get(i);
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
        String entry = String.format("%s. %s", epi.getEpi_number(), episode.getTitle());
        episodeHolder.title.setText(entry);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEpisodeDetails(epi);
            }
        });
        episodeHolder.epi_details_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TraktExpert.addToFinished(episode, addToFinishedCallback);
            }
        });
        Log.d("Alejandro", episode.getEpi_number()+ ": "+ episode.getCompleted());
        if(episode.getCompleted()){
            episodeHolder.epi_details_button.setImageDrawable(
                    view.getResources().getDrawable(R.drawable.ic_check_circle_black_24dp) );
        }

        return view;
    }

    private void openEpisodeDetails(Episode episode){
        activity.onEpisodeSelected(episode);
    }

    private Handler.Callback addToFinishedCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String msg = message.getData().getString(ServerCall.GET_MESSAGE);
            Log.d("Alejandro", "addToFinishedCallback msg: " + msg);
            return false;
        }
    };

}
