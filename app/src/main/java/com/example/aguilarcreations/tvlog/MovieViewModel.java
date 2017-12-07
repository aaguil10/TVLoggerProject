package com.example.aguilarcreations.tvlog;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Work on 10/21/17.
 */

public class MovieViewModel {
    private static MovieViewModel instance = null;
    public static final String DETAILS_LOADED = "movie_details_loaded";
    public static final String ADDED_WATCHLIST = "added_watchlist";
    public static final String ADDED_FINISHED = "added_finished";
    public static final String REMOVED_WATCHLIST = "removed_watchlist";
    public static final String REMOVED_FINISHED = "removed_finished";
    public static final String GOT_SEASONS = "got_seasons";

    Movie movie = null;
    Episode episode = null;
    Context context;

    private MovieViewModel() {
    }

    public static MovieViewModel getInstance(Context context) {
        if(instance == null){
            instance = new MovieViewModel();
            instance.context = context;
        }
        return instance;
    }

    public void setMovie(Movie movie){
        this.movie = movie;
    }

    public void setEpisode(Episode episode){
        this.episode = episode;
    }

    public void loadMovieDetails(String id){
        Log.d("Alejandro", "Calling loadMovieDetails");
        TraktExpert.getMovieDetails(id, false, getDetailsCallback);
    }

    public void loadEpisodeDetails(String showid, String season_num, String episode_num){
        Log.d("Alejandro", "Calling loadMovieDetails");
        TraktExpert.getEpisodeDetails(showid, season_num, episode_num,getEpisodesDetailsCallback);
    }


    private Handler.Callback getDetailsCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String msg = message.getData().getString(ServerCall.GET_MESSAGE);
            Log.d("Alejandro", "getDetailsCallback msg: " + msg);
            try {
                JSONObject j = new JSONObject(msg);
                movie.addDetails(j);
                Log.d("Alejandro", "m: " + movie.toString());
            }catch (JSONException e){
                e.printStackTrace();
                Log.e("Alejandro", "Error getDetailsCallback: " + e.toString());
            }

            Intent intent = new Intent();
            intent.setAction(DETAILS_LOADED);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            return false;
        }
    };

    public void addToWatchlist(){
        Log.d("Alejandro", "Calling addToWatchlist");
        TraktExpert.addToWatchlist(movie, addWatchlistCallback);
    }

    public void removeWatchlist(){
        Log.d("Alejandro", "Calling removeWatchlist");
        TraktExpert.removeWatchlist(movie, removeWatchlistCallback);
    }

    public void addToFinshed(){
        Log.d("Alejandro", "Calling addToFinshed");
        TraktExpert.addToFinished(movie, addFinishedCallback);
    }

    public void removeFinshed(){
        Log.d("Alejandro", "Calling removeFinshed");
        TraktExpert.removedFromFinished(movie, removedFinishedCallback);
    }

    public void getSeasons(){
        Log.d("Alejandro", "Calling getSeasons");
        TraktExpert.getSeasons(movie.getImdb_id(), getSeasonsCallback);
    }




    private Handler.Callback addWatchlistCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String msg = message.getData().getString(ServerCall.GET_MESSAGE);
            Log.d("Alejandro", "addWatchlistCallback msg: " + msg);

            Intent intent = new Intent();
            intent.setAction(ADDED_WATCHLIST);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            return false;
        }
    };

    private Handler.Callback removeWatchlistCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String msg = message.getData().getString(ServerCall.GET_MESSAGE);
            Log.d("Alejandro", "removeWatchlistCallback msg: " + msg);

            Intent intent = new Intent();
            intent.setAction(REMOVED_WATCHLIST);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            return false;
        }
    };

    private Handler.Callback addFinishedCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String msg = message.getData().getString(ServerCall.GET_MESSAGE);
            Log.d("Alejandro", "addFinishedCallback msg: " + msg);

            Intent intent = new Intent();
            intent.setAction(ADDED_FINISHED);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            return false;
        }
    };

    private Handler.Callback removedFinishedCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String msg = message.getData().getString(ServerCall.GET_MESSAGE);
            Log.d("Alejandro", "removedFinishedCallback msg: " + msg);

            Intent intent = new Intent();
            intent.setAction(REMOVED_FINISHED);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            return false;
        }
    };

    private Handler.Callback getSeasonsCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String msg = message.getData().getString(ServerCall.GET_MESSAGE);
            Log.d("Alejandro", "getSeasonsCallback msg: " + msg);

            try {


            }catch (Exception e){
                e.printStackTrace();
                Log.e("Alejandro", "Error getSeasonsCallback e: " + e.toString());
            }

            Intent intent = new Intent();
            intent.setAction(GOT_SEASONS);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            return false;
        }
    };

    private Handler.Callback getEpisodesDetailsCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String msg = message.getData().getString(ServerCall.GET_MESSAGE);
            Log.d("Alejandro", "getEpisodesCallback msg: " + msg);

            return false;
        }
    };

    public Movie getMovie(){
        return movie;
    }


}
