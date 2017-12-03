package com.example.aguilarcreations.tvlog;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import layout.MovieBrowserFragment;

/**
 * Created by Work on 10/16/17.
 */

public class MovieBrowserViewModel {
    private static MovieBrowserViewModel instance = null;
    private ArrayList<Item> popularMovies = null;
    private MovieBrowserFragment fragment;
    private Context context;

    public static final String POPULAR_UPDATED = "popular_movies_updated";

    public MovieBrowserViewModel(){
    }

    public static MovieBrowserViewModel getInstance(MovieBrowserFragment fragment){
        if(instance == null){
            instance = new MovieBrowserViewModel();
            instance.fragment = fragment;
            instance.context = fragment.getActivity().getBaseContext();
        }
        return instance;
    }



    public void loadPopularMovies(boolean media_mode){
        TraktExpert.getPopularMovies(getPopularMoviesCallback, media_mode);
    }

    private Handler.Callback getPopularMoviesCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String msg = message.getData().getString(ServerCall.GET_MESSAGE);
            Log.d("Alejandro", "In callback: " + msg);
            try {
                popularMovies = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(msg);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject j = jsonArray.getJSONObject(i);
                    if(fragment.isMovieMode()) {
                        Movie movie = Movie.createMovieFromJson(jsonArray.getJSONObject(i), Item.BROWSE);
                        popularMovies.add(movie);
                    }else {
                        Show show = Show.createShowFromJson(j, Item.BROWSE);
                        popularMovies.add(show);
                    }
                }

            }catch (JSONException e){
                e.printStackTrace();
                Log.e("Alejandro", "Error getPopularMoviesCallback: " + e.toString());
            }

            Intent intent = new Intent();
            intent.setAction(POPULAR_UPDATED);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            return false;
        }
    };

    public ArrayList<Item> getPopularMovies(){
        if(popularMovies == null){
            loadPopularMovies(true);
        }
        return popularMovies;
    }





}
