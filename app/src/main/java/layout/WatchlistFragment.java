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
import com.example.aguilarcreations.tvlog.WatchlistViewModel;
import com.example.aguilarcreations.tvlog.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class WatchlistFragment extends Fragment {

    private OnItemSelectedListener mListener;
    private WatchlistViewModel watchlistViewModel;
    SwipeRefreshLayout mySwipeRefreshLayout;

    GridView watchlistGV;
    MainActivity activity;

    public WatchlistFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("Lifecycle", "WatchlistFragment Oncreate");
        super.onCreate(savedInstanceState);

        activity = (MainActivity) getActivity();
        watchlistViewModel = WatchlistViewModel.getInstance(this);

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WatchlistViewModel.WATCHLIST_UPDATED);
        LocalBroadcastManager.getInstance(activity.getBaseContext()).registerReceiver(watchlistReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Lifecycle", "WatchlistFragment onCreateView");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_watchlist, container, false);

        watchlistGV = v.findViewById(R.id.watch_gridview);
        watchlistGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item movie = (Item) adapterView.getItemAtPosition(i);
                mListener.onItemSelected(movie);
                Log.d("Alejandro", "Clicked on: " + movie.getTitle());
            }
        });

        watchlistViewModel.loadWatchlist();
        //SetUp loading bar

        setUpActionbar();

        mySwipeRefreshLayout = v.findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setRefreshing(true);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        watchlistViewModel.loadWatchlist();
                    }
                }
        );


        return v;
    }


    public boolean isMovieMode(){
        return MainActivity.MOVIE_MODE.equals(activity.getMedia_mode());
    }

    private void setUpActionbar(){
        ActionBar actionBar;
        actionBar = activity.getActionBar();
        actionBar.setTitle(R.string.title_watch);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);

    }






    @Override
    public void onAttach(Context context) {
        Log.d("Lifecycle", "WatchlistFragment onAttach");
        super.onAttach(context);
        try {
            mListener = (OnItemSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnMovieSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d("Lifecycle", "WatchlistFragment onDetach");
        super.onDetach();
        mListener = null;
        LocalBroadcastManager.getInstance(activity.getBaseContext()).unregisterReceiver(watchlistReceiver);
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

        void onItemSelected(Item item);
    }

    BroadcastReceiver watchlistReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Alejandro", "action: " + intent.getAction());
            if(WatchlistViewModel.WATCHLIST_UPDATED.equals(intent.getAction())){
                ArrayList<Item> movies = watchlistViewModel.getWatchlist();
                MovieBaseAdapter adapter = new MovieBaseAdapter(getActivity(), movies, getResources());
                watchlistGV.setAdapter(adapter);
                mySwipeRefreshLayout.setRefreshing(false);
            }
        }
    };

}
