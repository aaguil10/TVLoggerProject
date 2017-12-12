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

/**
 * Created by Work on 11/13/17.
 */

public class ShowViewModel {
    private static ShowViewModel instance = null;
    public static final String DETAILS_LOADED = "movie_details_loaded";
    public static final String GOT_SEASONS = "got_seasons";
    String TAG = ShowViewModel.class.getName();

    Show show = null;
    Context context;

    public ShowViewModel(){

    }

    public static ShowViewModel getInstance(Context context) {
        if(instance == null){
            instance = new ShowViewModel();
            instance.context = context;
        }
        return instance;
    }

    public void setShow(Show show){
        this.show = show;
    }

    public void loadShowDetails(String id){
        TraktExpert.getMovieDetails(id, false, getDetailsCallback);
    }

    private Handler.Callback getDetailsCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String msg = message.getData().getString(ServerCall.GET_MESSAGE);
            try {
                JSONArray j = new JSONArray(msg);
                show.addDetails(j);
            }catch (JSONException e){
                e.printStackTrace();
                Log.e(TAG, "Error getDetailsCallback: " + e.toString());
            }

            Intent intent = new Intent();
            intent.setAction(DETAILS_LOADED);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            return false;
        }
    };

    public void getSeasons(){
        TraktExpert.getSeasons(show.getImdb_id(), getSeasonsCallback);
    }


    private Handler.Callback getSeasonsCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String msg = message.getData().getString(ServerCall.GET_MESSAGE);

            try {
                JSONArray seasonArr = new JSONArray(msg);
                show.addDetails(seasonArr);

            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG, "Error getSeasonsCallback e: " + e.toString());
            }

            Intent intent = new Intent();
            intent.setAction(GOT_SEASONS);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            return false;
        }
    };

    public Show getShow(){
        return show;
    }

    public ArrayList<Episode> getEpisodes(int season){
        return show.getEpisodes(season);
    }

    public int getNumSeasons(){
        return show.getNumSeasons();
    }

}
