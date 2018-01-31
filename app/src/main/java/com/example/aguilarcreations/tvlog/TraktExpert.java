package com.example.aguilarcreations.tvlog;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Work on 10/16/17.
 * Used for interacting with Trakt API
 * http://docs.trakt.apiary.io/#
 */

public class TraktExpert {

    private static String apiGateWayUrl = "http://ec2-34-234-70-85.compute-1.amazonaws.com:8080/";
    private static String popular_movies_function = "browse/getPopularMovies";
    private static String get_watchlist_function = "watchlist/getWatchList";
    private static String get_current_shows_function = "watchlist/getCurrentShows";
    private static String add_watchlist_function = "watchlist/addToWatchList";
    private static String remove_watchlist_function = "watchlist/removeFromWatchList";
    private static String get_watched_function = "history/getWatched";
    private static String add_history_function = "history/addToHistory";
    private static String remove_history_function = "history/removeFromHistory";
    private static String get_movie_details_function = "details/getMovieDetails/";
    private static String get_episode_details_function = "details/getEpisodeDetails/";
    private static String get_episodes_function = "details/getEpisodes/";
    private static String get_finished_episodes_function = "details/getFinishedEpisodes/";



    public static void getPopularMovies(Handler.Callback callback, boolean getMovies){
        ServerCall serverCall = new ServerCall();
        String opt = "/Shows";
        if(getMovies){
            opt = "/Movies";
        }
        serverCall.execute(apiGateWayUrl+popular_movies_function+opt, callback);
    }


    public static void getWatchlist(Handler.Callback callback){

        ServerCall serverCall = new ServerCall();
        serverCall.execute(apiGateWayUrl+get_watchlist_function, callback);
    }

    public static void getCurrentShows(Handler.Callback callback){
        ServerCall serverCall = new ServerCall();
        serverCall.execute(apiGateWayUrl+get_current_shows_function, callback);
    }

    public static void addToWatchlist(Movie movie, Handler.Callback callback){
        ServerCall serverCall = new ServerCall();
        serverCall.execute(apiGateWayUrl+add_watchlist_function, callback, generateTransportString(movie));
    }

    public static void addToWatchlist(Show show, Handler.Callback callback){
        ServerCall serverCall = new ServerCall();
        serverCall.execute(apiGateWayUrl+add_watchlist_function, callback, generateTransportString(show));
    }

    public static void markFinished(Episode episode, Handler.Callback callback){
        ServerCall serverCall = new ServerCall();
        Log.d("Alejandro", "TraktExp.markFinished called: " + apiGateWayUrl+add_history_function);
        serverCall.execute(apiGateWayUrl+add_history_function, callback, "Fuck");
        Log.d("Alejandro", "Tacos");
    }

    public static void markUnwatched(Episode episode, Handler.Callback callback){
        ServerCall serverCall = new ServerCall();
        //serverCall.execute(apiGateWayUrl+add_watchlist_function, callback, generateTransportString(episode));
    }


    public static void removeWatchlist(Movie movie, Handler.Callback callback){
        ServerCall serverCall = new ServerCall();
        serverCall.execute(apiGateWayUrl+remove_watchlist_function, callback, generateTransportString(movie));
    }

    public static void removeWatchlist(Show show, Handler.Callback callback){
        ServerCall serverCall = new ServerCall();
        serverCall.execute(apiGateWayUrl+remove_watchlist_function, callback, generateTransportString(show));
    }

    public static void getFinished(Handler.Callback callback){
        ServerCall serverCall = new ServerCall();
        serverCall.execute(apiGateWayUrl+get_watched_function, callback);
    }

    public static void addToFinished(Movie movie, Handler.Callback callback){
        ServerCall serverCall = new ServerCall();
        serverCall.execute(apiGateWayUrl+add_history_function, callback, generateTransportString(movie));
    }

    public static void addToFinished(Episode episode, Handler.Callback callback){
        ServerCall serverCall = new ServerCall();
        serverCall.execute(apiGateWayUrl+add_history_function, callback, generateTransportString(episode));
    }

    public static void removedFromFinished(Movie movie, Handler.Callback callback){
        ServerCall serverCall = new ServerCall();
        serverCall.execute(apiGateWayUrl+remove_history_function, callback, generateTransportString(movie));
    }

    public static void removedFromFinished(Show show, Handler.Callback callback){
        ServerCall serverCall = new ServerCall();
        serverCall.execute(apiGateWayUrl+remove_history_function, callback, generateTransportString(show));
    }

    public static void getMovieDetails(String traktid, boolean isShow, Handler.Callback callback){
        ServerCall serverCall = new ServerCall();
        String mode = isShow ? "Shows": "Item";
        serverCall.execute(apiGateWayUrl+get_movie_details_function+mode+"-"+traktid, callback);
    }

    //
    public static void getSeasons(String traktid, Handler.Callback callback){
        ServerCall serverCall = new ServerCall();
        serverCall.execute(apiGateWayUrl+get_episodes_function+traktid, callback);
    }

    public static void getEpisodeDetails(String show_id,String season_num, String epi_num, Handler.Callback callback){
        ServerCall serverCall = new ServerCall();
        serverCall.execute(apiGateWayUrl+get_episode_details_function+show_id+"-"+season_num+"-"+epi_num, callback);
    }


    public static void getFinishedEpisodes(String traktid, Handler.Callback callback){
        ServerCall serverCall = new ServerCall();
        serverCall.execute(apiGateWayUrl+get_finished_episodes_function+traktid, callback);
    }

    private static String generateTransportString(Movie movie){
        String s = "{\"movies\": [{\"ids\": {\"imdb\": \"" + movie.getImdb_id() +"\"}}]}";
        return s;
    }

    private static String generateTransportString(Show show){
        String s = "{\"shows\": [{\"ids\": {\"imdb\": \"" + show.getImdb_id() +"\"}}]}";
        return s;
    }

    private static String generateTransportString(Episode episode){
        String s = "{\"episodes\": [{\"ids\": {\"imdb\": \"" + episode.getImdb_id() +"\"}}]}";
        return s;
    }

}
