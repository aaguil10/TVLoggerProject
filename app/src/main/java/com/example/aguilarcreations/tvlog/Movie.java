package com.example.aguilarcreations.tvlog;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Work on 11/12/17.
 */

public class Movie extends Item{

    private String tagline;
    private String released;

    //Details
    private String overview;
    private int runtime;
    private String trailer;
    private String homepage;
    private long rating;
    private int votes;
    private String updated_at;
    private String language;
    private String[] available_translations;
    private String[] genres;
    private String certification;



    public Movie(){

    }

    public static Movie createMovieFromJson(JSONObject j, String state) throws JSONException {
        Movie movie = new Movie();
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

    public static Movie createMovieFromItem(Item item){
        Movie movie = new Movie();
        movie.state = item.state;
        movie.setTitle(item.getTitle());
        movie.setYear(item.getYear());
        movie.setTrakt_id(item.getTrakt_id());
        movie.setSlug_id(item.getSlug_id());
        movie.setImdb_id(item.getImdb_id());
        movie.setTmdb(item.getTmdb());

        return movie;
    }

    public void addDetails(JSONObject j) throws JSONException {
        this.overview = j.getString("overview");
        this.runtime = j.getInt("runtime");
        this.trailer = j.optString("trailer");
        this.homepage = j.optString("homepage");
        this.rating = j.optLong("rating");
        this.votes = j.optInt("votes");
        this.updated_at = j.getString("updated_at");
        this.language = j.optString("language");
        this.certification = j.getString("certification");
        JSONArray translations = j.getJSONArray("available_translations");
        this.available_translations = new String[translations.length()];
        for (int i = 0; i < translations.length(); i++)
            this.available_translations[i] = translations.getString(i);
        JSONArray genres = j.getJSONArray("genres");
        this.genres = new String[genres.length()];
        for (int i = 0; i < genres.length(); i++)
            this.genres[i] = genres.getString(i);
        this.tagline = j.optString("tagline");
        this.released = j.getString("released");
    }


    public String getTagline(){
        return tagline;
    }

    public String getOverview(){
        return overview;
    }

    public String getReleased(){
        return released;
    }
    public int getRuntime(){
        return runtime;
    }
    public String getTrailer(){
        return trailer;
    }
    public String getHomepage(){
        return homepage;
    }
    public long getRating(){
        return rating;
    }
    public int getVotes(){
        return votes;
    }
    public String getUpdatedAt(){
        return updated_at;
    }
    public String getLanguage(){
        return language;
    }
    public String[] getAvailableTranslations(){
        return available_translations;
    }
    public String[] getGenres(){
        return genres;
    }
    public String getCertification(){
        return certification;
    }


}
