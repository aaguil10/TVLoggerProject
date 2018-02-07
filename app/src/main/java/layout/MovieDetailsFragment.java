package layout;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aguilarcreations.tvlog.Episode;
import com.example.aguilarcreations.tvlog.FloatingActionMenuAnimator;
import com.example.aguilarcreations.tvlog.ImageHelper;
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
    SwipeRefreshLayout mySwipeRefreshLayout;

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
            movieViewModel.setMovie(movie);
            movieViewModel.loadMovieDetails(Integer.toString(movie.getTrakt_id()));
        }else if(episode != null){
            movieViewModel.setEpisode(episode);
            movieViewModel.loadEpisodeDetails(Integer.toString(episode.getShow().getTrakt_id()),
                    Integer.toString(episode.getSeason()),
                    Integer.toString(episode.getEpi_number()));
        }
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MovieViewModel.DETAILS_LOADED);
        intentFilter.addAction(MovieViewModel.EPISODE_LOADED);
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
        setupActionBar(v);
        setupActionMenu(v);
        setupCoverImage(v);

        mySwipeRefreshLayout = v.findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setRefreshing(true);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (movie != null) {
                            movieViewModel.loadMovieDetails(Integer.toString(movie.getTrakt_id()));
                        }else if(episode != null){
                            movieViewModel.loadEpisodeDetails(Integer.toString(episode.getShow().getTrakt_id()),
                                    Integer.toString(episode.getSeason()),
                                    Integer.toString(episode.getEpi_number()));
                        }
                    }
                }
        );

        return v;
    }


    private void setupActionBar(View v){
        ActionBar actionBar = activity.getActionBar();
        if(actionBar != null) {
            if (movie != null) {
                actionBar.setTitle(movie.getTitle());
            } else if (episode != null) {
                actionBar.setTitle(episode.getTitle());
            }
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    private void setupActionMenu(View v){
        if(movie != null) {
            FloatingActionMenuAnimator.BtnData[] btnData = new FloatingActionMenuAnimator.BtnData[3];
            btnData[0] = new FloatingActionMenuAnimator.BtnData("Stop Tracking", R.drawable.ic_remove_black_24dp);
            btnData[1] = new FloatingActionMenuAnimator.BtnData("Add to Finshed", R.drawable.ic_playlist_add_check_black_24dp);
            btnData[2] = new FloatingActionMenuAnimator.BtnData("Add to Watchlist", R.drawable.ic_watchlist_black_24dp);
            FloatingActionMenuAnimator floatingActionMenuAnimator = FloatingActionMenuAnimator.build(v, btnData);
            floatingActionMenuAnimator.getActionButton(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieViewModel.removeWatchlist();
                    movieViewModel.removeFinshed();
                }
            });
            floatingActionMenuAnimator.getActionButton(1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieViewModel.addToFinshed();
                }
            });
            floatingActionMenuAnimator.getActionButton(2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieViewModel.addToWatchlist();
                }
            });
        }else if(episode != null){
            FloatingActionMenuAnimator.BtnData[] btnData = new FloatingActionMenuAnimator.BtnData[2];
            btnData[0] = new FloatingActionMenuAnimator.BtnData("Mark Unwatched", R.drawable.ic_add_circle_black_24dp);
            btnData[1] = new FloatingActionMenuAnimator.BtnData("---Mark Finished", R.drawable.ic_check_circle_black_24dp);
            FloatingActionMenuAnimator floatingActionMenuAnimator = FloatingActionMenuAnimator.build(v, btnData);
            floatingActionMenuAnimator.getActionButton(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieViewModel.markUnwatched(episode);
                }
            });
            floatingActionMenuAnimator.getActionButton(1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    movieViewModel.addToFinshed();
                }
            });
        }
    }


    private void setupCoverImage(View v){
        ImageView cover = v.findViewById(R.id.moviedetails_main_image);
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int w = size.x;
        int h = (int) (((float)size.x/1920f)*1080f); //get height based on this ratio
        cover.getLayoutParams().width = w;
        cover.getLayoutParams().height = h;

        Bitmap bitmap = ImageHelper.decodeSampledBitmapFromResource(v.getResources(),
                R.mipmap.minions_backdrop, w, h);
        cover.setImageBitmap(bitmap);
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
            if(MovieViewModel.DETAILS_LOADED.equals(intent.getAction())){
                if(fragment.getView() != null) {
                    TextView title_tv = fragment.getView().findViewById(R.id.moviedetails_description);
                    title_tv.setText(movie.getOverview());
                }
                mySwipeRefreshLayout.setRefreshing(false);
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
                Toast.makeText(context, "--Got Seasons", Toast.LENGTH_LONG).show();
                mySwipeRefreshLayout.setRefreshing(false);
            }else if(MovieViewModel.EPISODE_LOADED.equals(intent.getAction())){
                Toast.makeText(context, "Episode Loaded!", Toast.LENGTH_LONG).show();
                mySwipeRefreshLayout.setRefreshing(false);
                if(fragment.getView() != null) {
                    ActionBar actionBar = activity.getActionBar();
                    actionBar.setTitle(episode.getTitle());
                    TextView dis_tv = fragment.getView().findViewById(R.id.moviedetails_description);
                    String dis = "Season " + episode.getSeason() + " Episode: " + episode.getEpi_number();
                    dis_tv.setText(dis);
                }

            }
        }
    };

}
