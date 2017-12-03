package layout;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.aguilarcreations.tvlog.Item;
import com.example.aguilarcreations.tvlog.MainActivity;
import com.example.aguilarcreations.tvlog.Movie;
import com.example.aguilarcreations.tvlog.MovieBaseAdapter;
import com.example.aguilarcreations.tvlog.MovieBrowserViewModel;
import com.example.aguilarcreations.tvlog.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieBrowserFragment.OnItemSelectedListener} interface
 * to handle interaction events.
 */
public class MovieBrowserFragment extends Fragment {

    private OnItemSelectedListener mListener;
    private MovieBrowserViewModel browserViewModel;

    SwipeRefreshLayout mySwipeRefreshLayout;

    MainActivity activity;

    GridView movieGV;

    public MovieBrowserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("Lifecycle", "MovieBrowserFragment onCreate");
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

        browserViewModel = MovieBrowserViewModel.getInstance(this);

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MovieBrowserViewModel.POPULAR_UPDATED);
        LocalBroadcastManager.getInstance(activity.getBaseContext()).registerReceiver(movieReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Lifecycle", "MovieBrowserFragment onCreateView");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_browser, container, false);

        movieGV = v.findViewById(R.id.moviebrowser_gridview);
        movieGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item movie = (Item) adapterView.getItemAtPosition(i);
                mListener.onItemSelected(movie);
                Log.d("Alejandro", "Clicked on: " + movie.getTitle());
            }
        });
        if(isMovieMode()) {
            browserViewModel.loadPopularMovies(true);
        }else{
            browserViewModel.loadPopularMovies(false);
        }
        //SetUp loading bar

        setUpActionbar();
        mySwipeRefreshLayout = v.findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setRefreshing(true);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if(isMovieMode()) {
                            browserViewModel.loadPopularMovies(true);
                        }else{
                            browserViewModel.loadPopularMovies(false);
                        }
                    }
                }
        );

        return v;
    }

    public boolean isMovieMode(){
        return MainActivity.MOVIE_MODE.equals(activity.getMedia_mode());
    }


    private void setUpActionbar(){
        ActionBar actionBar = activity.getActionBar();
        actionBar.setTitle(R.string.title_browse);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
    }


    @Override
    public void onAttach(Context context) {
        Log.d("Lifecycle", "MovieBrowserFragment onAttach");
        super.onAttach(context);
        try {
            mListener = (OnItemSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnMovieSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d("Lifecycle", "MovieBrowserFragment onDetach");
        super.onDetach();
        mListener = null;
        LocalBroadcastManager.getInstance(activity.getBaseContext()).unregisterReceiver(movieReceiver);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnItemSelectedListener {
        void onItemSelected(Item movie);
    }


    BroadcastReceiver movieReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("Alejandro", "action: " + intent.getAction());
            if(MovieBrowserViewModel.POPULAR_UPDATED.equals(intent.getAction())){
                ArrayList<Item> movies = browserViewModel.getPopularMovies();
                MovieBaseAdapter adapter = new MovieBaseAdapter(getActivity(), movies, getResources());
                movieGV.setAdapter(adapter);
                mySwipeRefreshLayout.setRefreshing(false);
            }
        }
    };


}
