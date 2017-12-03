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

import com.example.aguilarcreations.tvlog.FinishedMoviesViewModel;
import com.example.aguilarcreations.tvlog.Item;
import com.example.aguilarcreations.tvlog.MainActivity;
import com.example.aguilarcreations.tvlog.Movie;
import com.example.aguilarcreations.tvlog.MovieBaseAdapter;
import com.example.aguilarcreations.tvlog.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FinishedMoviesFragment extends Fragment {

    private OnItemSelectedListener mListener;
    private FinishedMoviesViewModel finishedMoviesViewModel;
    SwipeRefreshLayout mySwipeRefreshLayout;

    MainActivity activity;
    GridView movieGV;

    public FinishedMoviesFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("Lifecycle", "FinshedMoviesFragment onCreate");
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

        finishedMoviesViewModel = FinishedMoviesViewModel.getInstance(this);

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FinishedMoviesViewModel.FINISHED_MOVIES_UPDATED);
        LocalBroadcastManager.getInstance(activity.getBaseContext()).registerReceiver(movieReceiver, intentFilter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Lifecycle", "FinshedMoviesFragment onCreateView");
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_finished_movies, container, false);

        movieGV = v.findViewById(R.id.finshed_gridview);
        movieGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item movie = (Item) adapterView.getItemAtPosition(i);
                mListener.onItemSelected(movie);
                Log.d("Alejandro", "Clicked on: " + movie.getTitle());
            }
        });
        finishedMoviesViewModel.loadFinishedMovies();
        //SetUp loading bar

        setUpActionbar();

        mySwipeRefreshLayout = v.findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setRefreshing(true);

        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        finishedMoviesViewModel.loadFinishedMovies();
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
        actionBar.setTitle(R.string.title_finished);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
    }

    @Override
    public void onAttach(Context context) {
        Log.d("Lifecycle", "FinshedMoviesFragment onAttach");
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener) {
            mListener = (OnItemSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d("Lifecycle", "FinshedMoviesFragment onDetach");
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
            if(FinishedMoviesViewModel.FINISHED_MOVIES_UPDATED.equals(intent.getAction())){
                ArrayList<Item> movies = finishedMoviesViewModel.getFinishedrMovies();
                MovieBaseAdapter adapter = new MovieBaseAdapter(getActivity(), movies, getResources());
                movieGV.setAdapter(adapter);
                mySwipeRefreshLayout.setRefreshing(false);
            }
        }
    };
}
