package com.example.aguilarcreations.tvlog;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Work on 10/15/17.
 */

public class Item {

    public static final String WATCHLIST = "watchlist", BROWSE = "browse", FINISHED = "finshed";

    private String title;
    private int year;

    private int trakt_id;
    private String slug_id;
    private String imdb_id;
    private int tmdb;


    //added in history
    private int history_id;
    private String watched_at;
    private String action;
    private String type;

    protected String state; //In watchlist/Finished/Not Tracked

    public Item(){

    }

    public static Item createMovieFromJson(JSONObject j, String state) throws JSONException{
        Item movie = new Item();
        movie.state = state;
        movie.setTitle(j.getString("title"));
        movie.setYear(j.optInt("year"));
        JSONObject ids = j.optJSONObject("ids");
        movie.setTrakt_id(ids.getInt("trakt"));
        movie.setSlug_id(ids.optString("slug"));
        movie.setImdb_id(ids.optString("imdb"));
        movie.setTmdb(ids.optInt("tmdb"));

        return movie;
    }

    public void setState(String state){
        if(Item.BROWSE.equals(state) ||
                Item.FINISHED.equals(state) ||
                Item.WATCHLIST.equals(state)) {
            this.state = state;
        }else{
            throw new IllegalStateException();
        }
    }

    public String getState(){
        return state;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public void setYear(int year){
        this.year = year;
    }

    public int getYear(){
        return year;
    }

    public void setTrakt_id(int id){
        trakt_id = id;
    }

    public int getTrakt_id(){
        return trakt_id;
    }

    public void setSlug_id(String slug_id){
        this.slug_id = slug_id;
    }

    public String getSlug_id(){
        return slug_id;
    }

    public void setImdb_id(String imdb_id){
        this.imdb_id = imdb_id;
    }

    public String getImdb_id(){
        return imdb_id;
    }

    public void setTmdb(int tmdb){
        this.tmdb = tmdb;
    }

    public int getTmdb(){
        return tmdb;
    }

    public void setHistory_id(int history_id){
        this.history_id = history_id;
    }

    public void setWatched_at(String watched_at){
        this.watched_at = watched_at;
    }

    public void setAction(String action){
        this.action = action;
    }

    public void setType(String type){
        this.type = type;
    }


    public String toString(){
        String s = "{";
        s += "title:" + "\"" + title +"\",";
        s += "imdb_id: "+ "\"" + imdb_id +"\",";
        s += "action: "+ "\"" + action +"\",";
        s += "type: "+ "\"" + type +"\",";

        return s + "}";
    }

}
