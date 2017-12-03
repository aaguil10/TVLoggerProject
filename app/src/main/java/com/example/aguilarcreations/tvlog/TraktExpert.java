package com.example.aguilarcreations.tvlog;

import android.os.Handler;

/**
 * Created by Work on 10/16/17.
 * Used for interacting with Trakt API
 * http://docs.trakt.apiary.io/#
 */

public class TraktExpert {

    private static String apiGateWayUrl = "http://ec2-34-234-70-85.compute-1.amazonaws.com:3000/";
    private static String popular_movies_function = "getPopularMovies";
    private static String get_watchlist_function = "getWatchList";
    private static String add_watchlist_function = "addToWatchList";
    private static String remove_watchlist_function = "removeFromWatchList";
    private static String get_watched_function = "getWatched";
    private static String add_history_function = "addToHistory";
    private static String remove_history_function = "removeFromHistory";
    private static String get_movie_details_function = "getMovieDetails/";
    private static String get_season_function = "getSeasons/";
    private static String get_episodes_function = "getEpisodes/";



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

    public static void addToWatchlist(Movie movie, Handler.Callback callback){
        ServerCall serverCall = new ServerCall();
        serverCall.execute(apiGateWayUrl+add_watchlist_function, callback, generateTransportString(movie));
    }

    public static void addToWatchlist(Show show, Handler.Callback callback){
        ServerCall serverCall = new ServerCall();
        serverCall.execute(apiGateWayUrl+add_watchlist_function, callback, generateTransportString(show));
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

    public static void addToFinished(Show show, Handler.Callback callback){
        ServerCall serverCall = new ServerCall();
        serverCall.execute(apiGateWayUrl+add_history_function, callback, generateTransportString(show));
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

    public static void getSeasons(String traktid, Handler.Callback callback){
        ServerCall serverCall = new ServerCall();
        serverCall.execute(apiGateWayUrl+get_episodes_function+traktid, callback);
    }

    public static void getEpisodes(String showid,String seasonid, Handler.Callback callback){
        ServerCall serverCall = new ServerCall();
        serverCall.execute(apiGateWayUrl+get_episodes_function+showid+"-"+seasonid, callback);
    }


    private static String generateTransportString(Movie movie){
        String s = "{\"movies\": [{\"ids\": {\"imdb\": \"" + movie.getImdb_id() +"\"}}]}";
        return s;
    }

    private static String generateTransportString(Show show){
        String s = "{\"shows\": [{\"ids\": {\"imdb\": \"" + show.getImdb_id() +"\"}}]}";
        return s;
    }

}
