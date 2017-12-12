package com.example.aguilarcreations.tvlog;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Work on 11/16/17.
 */

public class Episode extends Item {

    private Show show;
    private int season;
    private int epi_number;


    public Episode(){

    }

    public static Episode createEpisodeFromJson(JSONObject j, String state) throws JSONException {
        Episode episode = new Episode();
        episode.state = state;
        episode.setSeason(j.getInt("season"));
        episode.setEpiNumber(j.getInt("number"));
        episode.setTitle(j.getString("title"));
        episode.setYear(j.optInt("year"));
        JSONObject ids = j.optJSONObject("ids");
        episode.setTrakt_id(ids.getInt("trakt"));
        episode.setSlug_id(ids.optString("slug"));
        episode.setImdb_id(ids.optString("imdb"));
        episode.setTmdb(ids.optInt("tmdb"));
        return episode;
    }

    public static Episode createEpisodeFromItem(Item item){
        Episode episode = new Episode();
        episode.state = item.state;
        episode.setTitle(item.getTitle());
        episode.setYear(item.getYear());
        episode.setTrakt_id(item.getTrakt_id());
        episode.setSlug_id(item.getSlug_id());
        episode.setImdb_id(item.getImdb_id());
        episode.setTmdb(item.getTmdb());
        return episode;
    }


    public void setShow(Show show){
        this.show = show;
    }

    public Show getShow(){
        return show;
    }

    public void setSeason(int season){
        this.season = season;
    }

    public int getSeason(){
        return season;
    }

    public void setEpiNumber(int epi_number){
        this.epi_number = epi_number;
    }

    public int getEpi_number(){
        return epi_number;
    }

    public String toString(){
        Log.d("Alejandro", "Episode.toString Called!");
        String result = "{";
        result += "\"title\":" + getTitle();
        result += ", \"trackt_id\":" + getTrakt_id();
        if(show != null) {
            result += ", \"show\":" + getShow().toString();
        }else{
            result += ", \"show\": null";
        }
        Log.d("Alejandro", "result: "+ result);
        result += "}";
        Log.d("Alejandro", "--result: "+ result);
        return result;
    }


}
