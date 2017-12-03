package com.example.aguilarcreations.tvlog;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import layout.FinishedMoviesFragment;
import layout.MovieBrowserFragment;
import layout.MovieDetailsFragment;
import layout.ShowDetailsFragment;
import layout.WatchlistFragment;

public class MainActivity extends Activity implements
        MovieBrowserFragment.OnItemSelectedListener,
        WatchlistFragment.OnItemSelectedListener,
        FinishedMoviesFragment.OnItemSelectedListener{

    public MovieViewModel movieViewModel;
    public ShowViewModel showViewModel;
    public static String TV_MODE = "TV", MOVIE_MODE = "Item";
    private String media_mode = TV_MODE;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_watch:
                    fragment = new WatchlistFragment();
                    Log.d("Alejandro", "navigation_watch");
                    break;
                case R.id.navigation_browse:
                    fragment = new MovieBrowserFragment();
                    Log.d("Alejandro", "navigation_browse");
                    break;
                case R.id.navigation_finished:
                    fragment = new FinishedMoviesFragment();
                    Log.d("Alejandro", "navigation_finished");
                    break;
                default:
                    Log.d("Alejandro", "default");
                    fragment = new WatchlistFragment();
            }
            fragmentTransaction.replace(R.id.content, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return false;
        }

    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("Lifecycle", "MainActivity onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_action_buttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("Lifecycle", "MainActivity onOptionsItemSelected");

        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
                return true;
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Log.d("Alejandro", "Clicked Settings Button");
                return true;
            case R.id.action_tv_toggle:
                // User chose the "Settings" item, show the app settings UI...
                Log.d("Alejandro", "Clicked TV Toggle Button");
                if(media_mode.equals(MOVIE_MODE)) {
                    item.setTitle("Item");
                    media_mode = TV_MODE;
                }else{
                    item.setTitle("TV");
                    media_mode = MOVIE_MODE;
                }
                SharedPreferences settings = getSharedPreferences("MainActivity", 0);
                SharedPreferences.Editor editor = settings.edit();
                Log.d("Alejandro", "Saving:  " + media_mode);
                editor.putString("media_mode", media_mode);
                editor.commit();

                recreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Lifecycle", "MainActivity onCreate");
        SharedPreferences settings = getSharedPreferences("MainActivity", 0);
        media_mode = settings.getString("media_mode", MOVIE_MODE);
        Log.d("Alejandro", "--media_mode: " + media_mode);

        if(media_mode.equals(TV_MODE)){
            setTheme(R.style.TVTheme);
        }else {
            setTheme(R.style.MovieTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieViewModel = MovieViewModel.getInstance(getBaseContext());
        showViewModel = ShowViewModel.getInstance(getBaseContext());
        Log.d("Alejandro", "activity.movieViewModel: " + movieViewModel);
        Log.d("Alejandro", "activity.showViewModel: " + showViewModel);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        WatchlistFragment fragment = new WatchlistFragment();
        fragmentTransaction.add(R.id.content, fragment);
        fragmentTransaction.commit();


    }


    @Override
    public void onBackPressed() {
        Log.d("Lifecycle", "MainActivity onBackPressed");
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void onItemSelected(Item item){
        if(media_mode.equals(MOVIE_MODE)){
            onMovieSelected(Movie.createMovieFromItem(item));
        }else {
            onShowSelected(Show.createShowFromItem(item));
        }
    }


    public void onMovieSelected(Movie movie){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MovieDetailsFragment fragment = MovieDetailsFragment.newInstance(movie);
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void onShowSelected(Show show){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ShowDetailsFragment fragment = ShowDetailsFragment.newInstance(show);
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void onEpisodeSelected(Episode episode){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MovieDetailsFragment fragment = MovieDetailsFragment.newInstance(episode);
        fragmentTransaction.replace(R.id.content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    protected void onStop() {
        Log.d("Lifecycle", "MainActivity onStop");
        super.onStop();
    }


    public String getMedia_mode(){
        return media_mode;
    }

}
