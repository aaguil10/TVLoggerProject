package com.example.aguilarcreations.tvlog;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.net.ssl.SNIHostName;

import layout.WatchlistFragment;

/**
 * Created by Work on 10/18/17.
 */

public class WatchlistViewModel {
    private static WatchlistViewModel instance = null;
    ArrayList<Item> watchlist = null;
    WatchlistFragment fragment;
    Context context;
    public static final String WATCHLIST_UPDATED = "watchlist_updated";

    private WatchlistViewModel() {
    }

    public static WatchlistViewModel getInstance(WatchlistFragment fragment) {
        if(instance == null){
            Log.d("Alejandro", "WatchlistViewModel was created!");
            instance = new WatchlistViewModel();
            instance.fragment = fragment;
            instance.context = fragment.getActivity().getBaseContext();
        }
        return instance;
    }


    public void loadWatchlist(){

        TraktExpert.getWatchlist(getWatchlistCallback);
        TraktExpert.getCurrentShows(getCurrentShowsCallback);
    }

    private Handler.Callback getCurrentShowsCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String msg = message.getData().getString(ServerCall.GET_MESSAGE);
            Log.d("Alejandro", "getCurrentShowsCallback: " + msg);
            try {
                if(watchlist == null) {
                    watchlist = new ArrayList<>();
                }
                JSONArray jsonArray = new JSONArray(msg);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject j = jsonArray.getJSONObject(i);
                    if(j.has("show")){
                        Show show = Show.createShowFromJson(j.getJSONObject("show"), Item.WATCHLIST);
                        if(!fragment.isMovieMode()) watchlist.add(show);
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
                Log.e("Alejandro", "Error getWatchlistCallback: " + e.toString());
            }

            Intent intent = new Intent();
            intent.setAction("watchlist_updated");
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


            return false;
        }
    };

    private Handler.Callback getWatchlistCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String msg = message.getData().getString(ServerCall.GET_MESSAGE);
            Log.d("Alejandro", "In callback: " + msg);
            try {
                watchlist = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(msg);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject j = jsonArray.getJSONObject(i);
                    if(j.has("movie")) {
                        Movie movie = Movie.createMovieFromJson(j.getJSONObject("movie"), Item.WATCHLIST);
                        if(fragment.isMovieMode()) watchlist.add(movie);
                    }else if(j.has("show")){
                        Show show = Show.createShowFromJson(j.getJSONObject("show"), Item.WATCHLIST);
                        if(!fragment.isMovieMode()) watchlist.add(show);
                    }
                }

            }catch (JSONException e){
                e.printStackTrace();
                Log.e("Alejandro", "Error getWatchlistCallback: " + e.toString());
            }

            Intent intent = new Intent();
            intent.setAction("watchlist_updated");
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            return false;
        }
    };


    public ArrayList<Item> getWatchlist(){
        if(watchlist == null){
            loadWatchlist();
        }
        return watchlist;
    }

}
