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

import layout.FinishedMoviesFragment;

/**
 * Created by Work on 10/20/17.
 */

public class FinishedMoviesViewModel {
    private static FinishedMoviesViewModel instance = null;
    ArrayList<Item> finishedrMovies = null;
    FinishedMoviesFragment fragment;
    Context context;

    public static final String FINISHED_MOVIES_UPDATED = "finishe_movies_updated";

    public FinishedMoviesViewModel(){
    }

    public static FinishedMoviesViewModel getInstance(FinishedMoviesFragment fragment){
        if(instance == null){
            instance = new FinishedMoviesViewModel();
            instance.fragment = fragment;
            instance.context = fragment.getActivity().getBaseContext();
        }
        return instance;
    }



    public void loadFinishedMovies(){
        TraktExpert.getFinished(getFishedMoviesCallback);
    }

    private Handler.Callback getFishedMoviesCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String msg = message.getData().getString(ServerCall.GET_MESSAGE);
            Log.d("Alejandro", "In callback: " + msg);
            try {
                finishedrMovies = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(msg);
                for (int i = 0; i < jsonArray.length(); i++){
                    try {
                        JSONObject j = jsonArray.getJSONObject(i);
                        if(j.has("movie")) {
                            Movie movie = Movie.createMovieFromJson(j.getJSONObject("movie"), Item.FINISHED);
                            movie.setHistory_id(j.getInt("id"));
                            movie.setWatched_at(j.getString("watched_at"));
                            movie.setAction(j.getString("action"));
                            movie.setType(j.getString("type"));
                            if(fragment.isMovieMode()) finishedrMovies.add(movie);
                        }else if(j.has("show")){
                            Show show = Show.createShowFromJson(j.getJSONObject("show"), Item.FINISHED);
                            if(!fragment.isMovieMode())finishedrMovies.add(show);
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.d("Alejandro", "getFishedMoviesCallback e: " + e.toString());
                    }
                }

            }catch (JSONException e){
                e.printStackTrace();
                Log.e("Alejandro", "Error getPopularMoviesCallback: " + e.toString());
            }

            Intent intent = new Intent();
            intent.setAction(FINISHED_MOVIES_UPDATED);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            return false;
        }
    };

    public ArrayList<Item> getFinishedrMovies(){
        if(finishedrMovies == null){
            loadFinishedMovies();
        }
        return finishedrMovies;
    }

}
