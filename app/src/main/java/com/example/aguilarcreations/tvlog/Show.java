package com.example.aguilarcreations.tvlog;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Work on 11/12/17.
 */

public class Show extends Item{


    private int tvdb_id;
    private int tvrage;


    private String first_aired;
    private String airs_day;
    private String airs_time;
    private String airs_timezone;
    private String network;
    private String country;
    private String status;
    private int aired_episodes;

    private ArrayList<ArrayList<Episode>> seasons;
    private ArrayList<Integer> seasons_traktids;
    private ArrayList<Integer> seasons_tvdbids;
    private ArrayList<Integer> seasons_tmdbids;
    private ArrayList<Integer> seasons_tvrageids;






    public Show(){
        seasons = new ArrayList<>();
        seasons_traktids = new ArrayList<>();
        seasons_tvdbids = new ArrayList<>();
        seasons_tmdbids = new ArrayList<>();
        seasons_tvrageids = new ArrayList<>();
    }

    public static Show createShowFromJson(JSONObject j, String state) throws JSONException {
        Show show = new Show();
        show.state = state;
        show.setTitle(j.getString("title"));
        show.setYear(j.optInt("year"));
        JSONObject ids = j.optJSONObject("ids");
        show.setTrakt_id(ids.getInt("trakt"));
        show.setSlug_id(ids.optString("slug"));
        show.setImdb_id(ids.optString("imdb"));
        show.setTmdb(ids.optInt("tmdb"));
        show.setTvrage(ids.optInt("tvrage"));
        show.setTvdb_id(ids.optInt("tvdb"));
        return show;
    }

    public static Show createShowFromItem(Item item){
        Show show = new Show();
        show.state = item.state;
        show.setTitle(item.getTitle());
        show.setYear(item.getYear());
        show.setTrakt_id(item.getTrakt_id());
        show.setSlug_id(item.getSlug_id());
        show.setImdb_id(item.getImdb_id());
        show.setTmdb(item.getTmdb());
        return show;
    }

    public void addDetails(JSONArray jsonArray) throws JSONException {
        int curr_season;
        JSONObject j;
        for(int i = 0; i < jsonArray.length(); i++){
            j = jsonArray.getJSONObject(i);
            curr_season = j.getInt("number");
            JSONObject ids = j.getJSONObject("ids");
            seasons_traktids.add(curr_season, ids.getInt("trakt"));
            seasons_tvdbids.add(curr_season, ids.getInt("tvdb"));
            seasons_tmdbids.add(curr_season, ids.getInt("tmdb"));
            seasons_tvrageids.add(curr_season, ids.optInt("tvrage"));
            ArrayList<Episode> episodes = new ArrayList<>();
            JSONArray epi_arr = j.getJSONArray("episodes");
            for (int x = 0; x < epi_arr.length(); x++){
                Episode episode = Episode.createEpisodeFromJson(epi_arr.getJSONObject(x), Item.BROWSE);
                episodes.add(episode);
            }

            seasons.add(curr_season, episodes);
        }

    }



    public void setTvdb_id(int tvdb_id){
        this.tvdb_id = tvdb_id;
    }

    public int getTvdb_id() {
        return tvdb_id;
    }

    public void setTvrage(int tvrage){
        this.tvrage = tvrage;
    }

    public int setTvrage(){
        return tvrage;
    }

    public ArrayList<Episode> getEpisodes(int season){
        return seasons.get(season);
    }

    public int getNumSeasons(){
        return seasons.size();
    }
}
