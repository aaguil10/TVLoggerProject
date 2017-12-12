package layout;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aguilarcreations.tvlog.Episode;
import com.example.aguilarcreations.tvlog.Item;
import com.example.aguilarcreations.tvlog.MainActivity;
import com.example.aguilarcreations.tvlog.Movie;
import com.example.aguilarcreations.tvlog.MovieViewModel;
import com.example.aguilarcreations.tvlog.R;
import com.example.aguilarcreations.tvlog.Show;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailsFragment extends Fragment {

    MainActivity activity;
    MovieDetailsFragment fragment;
    MovieViewModel movieViewModel;
    Movie movie;
    Episode episode;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment MovieDetailsFragment.
     */
    public static MovieDetailsFragment newInstance(Movie movie) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.movie = movie;
        return fragment;
    }

    public static MovieDetailsFragment newInstance(Episode episode) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.episode = episode;
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("Lifecycle", "MovieDetailsFragment onCreate");
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        fragment = this;
        movieViewModel = activity.movieViewModel;
        if (movie != null) {
            Log.d("Alejandro", "Hit movie!");
            movieViewModel.setMovie(movie);
            movieViewModel.loadMovieDetails(Integer.toString(movie.getTrakt_id()));
        }else if(episode != null){
            Log.d("Alejandro", "Hit episode!");
            movieViewModel.setEpisode(episode);
            Log.d("Alejandro", "episode: " + episode.toString());
            movieViewModel.loadEpisodeDetails(Integer.toString(episode.getShow().getTrakt_id()),
                    Integer.toString(episode.getSeason()),
                    Integer.toString(episode.getEpi_number()));
        }else{
            Log.d("Alejandro", "NO BANANAS");
        }
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MovieViewModel.DETAILS_LOADED);
        intentFilter.addAction(MovieViewModel.ADDED_WATCHLIST);
        intentFilter.addAction(MovieViewModel.ADDED_FINISHED);
        intentFilter.addAction(MovieViewModel.REMOVED_WATCHLIST);
        intentFilter.addAction(MovieViewModel.REMOVED_FINISHED);
        LocalBroadcastManager.getInstance(activity.getBaseContext()).registerReceiver(movieReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Lifecycle", "MovieDetailsFragment onCreateView");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_details, container, false);
        setupActionBar();

        setupSpinner(v);


        return v;
    }


    private void setupSpinner(View v){
        Spinner spinner = v.findViewById(R.id.movie_status_spinner);
        int spinner_strings;
        String state = null;
        if(movie != null){
          state = movie.getState();
        }else if(episode != null){
            state = episode.getState();
        }

        switch (state){
            case Item.WATCHLIST:
                spinner_strings = R.array.movie_stat_watchlist;
                break;
            case Item.BROWSE:
                spinner_strings = R.array.movie_stat_browse;
                break;
            case Item.FINISHED:
                spinner_strings = R.array.movie_stat_finished;
                break;
            default:
                spinner_strings = R.array.movie_stat_watchlist;
        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity.getBaseContext(),
                spinner_strings, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = (String) adapterView.getItemAtPosition(i);
                Log.d("Alejandro", "selected: " + s);
                if(getString(R.string.remove_watch).equals(s)){
                    movieViewModel.removeWatchlist();
                }else if(getString(R.string.add_watchlist).equals(s)){
                    movieViewModel.addToWatchlist();
                    if(movie.getState().equals(Item.FINISHED)){
                        movieViewModel.removeFinshed();
                    }
                }else if(getString(R.string.add_finished).equals(s)){
                    movieViewModel.addToFinshed();
                    if(movie.getState().equals(Item.WATCHLIST)){
                        movieViewModel.removeWatchlist();
                    }
                }else if(getString(R.string.remove_finished).equals(s)){
                    movieViewModel.removeFinshed();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupActionBar(){
        ActionBar actionBar = activity.getActionBar();
        if(movie != null) {
            actionBar.setTitle(movie.getTitle());
        }else if(episode != null){
            actionBar.setTitle(episode.getTitle());
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public void onAttach(Context context) {
        Log.d("Lifecycle", "MovieDetailsFragment onAttach");
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        Log.d("Lifecycle", "MovieDetailsFragment onDetach");
        super.onDetach();
        LocalBroadcastManager.getInstance(activity.getBaseContext()).unregisterReceiver(movieReceiver);
    }

    BroadcastReceiver movieReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("Alejandro", "action: " + intent.getAction());
            if(MovieViewModel.DETAILS_LOADED.equals(intent.getAction())){
                if(fragment.getView() != null) {
                    TextView title_tv = fragment.getView().findViewById(R.id.moviedetails_description);
                    title_tv.setText(movie.getOverview());
                }
            }else if(MovieViewModel.ADDED_WATCHLIST.equals(intent.getAction())){
                Toast.makeText(context, "Added to Watchlist", Toast.LENGTH_LONG).show();
            }else if(MovieViewModel.ADDED_FINISHED.equals(intent.getAction())){
                Toast.makeText(context, "Added to Finished", Toast.LENGTH_LONG).show();
            }else if(MovieViewModel.REMOVED_WATCHLIST.equals(intent.getAction())){
                Toast.makeText(context, "Removed from Watchlist", Toast.LENGTH_LONG).show();
                if(Item.WATCHLIST.equals(movie.getState())){
                    movie.setState(Item.BROWSE);
                }
            }else if(MovieViewModel.REMOVED_FINISHED.equals(intent.getAction())){
                Toast.makeText(context, "Removed from Finished", Toast.LENGTH_LONG).show();
                if(Item.FINISHED.equals(movie.getState())){
                    movie.setState(Item.BROWSE);
                }
            }else if(MovieViewModel.GOT_SEASONS.equals(intent.getAction())){
                Toast.makeText(context, "Got Seasons", Toast.LENGTH_LONG).show();

            }
        }
    };




}
