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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aguilarcreations.tvlog.Episode;
import com.example.aguilarcreations.tvlog.EpisodeBaseAdapter;
import com.example.aguilarcreations.tvlog.Item;
import com.example.aguilarcreations.tvlog.MainActivity;
import com.example.aguilarcreations.tvlog.Movie;
import com.example.aguilarcreations.tvlog.MovieViewModel;
import com.example.aguilarcreations.tvlog.R;
import com.example.aguilarcreations.tvlog.Show;
import com.example.aguilarcreations.tvlog.ShowViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowDetailsFragment extends Fragment {

    ShowDetailsFragment fragment;
    ShowViewModel showViewModel;
    Show show;

    public ShowDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ShowDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowDetailsFragment newInstance(Show show) {
        ShowDetailsFragment fragment = new ShowDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.show = show;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("Lifecycle", "ShowDetailsFragment onCreate");
        super.onCreate(savedInstanceState);
        fragment = this;
        showViewModel = ShowViewModel.getInstance(getActivity());
        if (show != null) {
            showViewModel.setShow(show);
            showViewModel.loadMovieDetails(Integer.toString(show.getTrakt_id()));
        }
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MovieViewModel.DETAILS_LOADED);
        intentFilter.addAction(MovieViewModel.ADDED_WATCHLIST);
        intentFilter.addAction(MovieViewModel.ADDED_FINISHED);
        intentFilter.addAction(MovieViewModel.REMOVED_WATCHLIST);
        intentFilter.addAction(MovieViewModel.REMOVED_FINISHED);
        intentFilter.addAction(MovieViewModel.GOT_SEASONS);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(showReceiver, intentFilter);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Lifecycle", "MovieDetailsFragment onCreateView");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_show_details, container, false);
        setupActionBar();

        setupSpinner(v);
        showViewModel.getSeasons();


        return v;
    }

    private void setupSpinner(View v){
        Spinner spinner = v.findViewById(R.id.movie_status_spinner);
        int spinner_strings;
        switch (show.getState()){
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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                spinner_strings, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = (String) adapterView.getItemAtPosition(i);
                Log.d("Alejandro", "selected: " + s);
                if(getString(R.string.remove_watch).equals(s)){
                    showViewModel.removeWatchlist();
                }else if(getString(R.string.add_watchlist).equals(s)){
                    showViewModel.addToWatchlist();
                    if(show.getState().equals(Item.FINISHED)){
                        showViewModel.removeFinshed();
                    }
                }else if(getString(R.string.add_finished).equals(s)){
                    showViewModel.addToFinshed();
                    if(show.getState().equals(Item.WATCHLIST)){
                        showViewModel.removeWatchlist();
                    }
                }else if(getString(R.string.remove_finished).equals(s)){
                    showViewModel.removeFinshed();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupActionBar(){
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setTitle(show.getTitle());
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public void onAttach(Context context) {
        Log.d("Lifecycle", "ShowDetailsFragment onAttach");
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        Log.d("Lifecycle", "ShowDetailsFragment onDetach");
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(showReceiver);
    }

    BroadcastReceiver showReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("Alejandro", "action: " + intent.getAction());
            if(MovieViewModel.DETAILS_LOADED.equals(intent.getAction())){
                if(fragment.getView() != null) {
                    TextView title_tv = fragment.getView().findViewById(R.id.moviedetails_description);
                    title_tv.setText(show.getTitle());

                }
            }else if(MovieViewModel.ADDED_WATCHLIST.equals(intent.getAction())){
                Toast.makeText(context, "Added to Watchlist", Toast.LENGTH_LONG).show();
            }else if(MovieViewModel.ADDED_FINISHED.equals(intent.getAction())){
                Toast.makeText(context, "Added to Finished", Toast.LENGTH_LONG).show();
            }else if(MovieViewModel.REMOVED_WATCHLIST.equals(intent.getAction())){
                Toast.makeText(context, "Removed from Watchlist", Toast.LENGTH_LONG).show();
                if(Item.WATCHLIST.equals(show.getState())){
                    show.setState(Item.BROWSE);
                }
            }else if(MovieViewModel.REMOVED_FINISHED.equals(intent.getAction())){
                Toast.makeText(context, "Removed from Finished", Toast.LENGTH_LONG).show();
                if(Item.FINISHED.equals(show.getState())){
                    show.setState(Item.BROWSE);
                }
            }else if(MovieViewModel.GOT_SEASONS.equals(intent.getAction())){
                Toast.makeText(context, "Got Seasons", Toast.LENGTH_LONG).show();
                ListView episode_listview = fragment.getView().findViewById(R.id.episode_listview);
                EpisodeBaseAdapter episodeBaseAdapter;
                ArrayList<EpiSlot> list = new ArrayList<>();
                int counter = 0;
                for (int i = 0; i < showViewModel.getNumSeasons(); i++) {
                        EpiSlot epiSlot = new EpiSlot(true);
                        epiSlot.season_num = i;
                        list.add(epiSlot);
                        for (Episode episode : showViewModel.getEpisodes(i)) {
                            EpiSlot epi = new EpiSlot(false);
                            if(i != 0) {
                                epi.epi_num = ++counter;
                            }else{
                                epi.epi_num = 0;
                            }
                            epi.episode = episode;
                            list.add(epi);
                        }
                }
                episodeBaseAdapter = new EpisodeBaseAdapter(getActivity(), list, getResources());
                episode_listview.setAdapter(episodeBaseAdapter);

            }
        }
    };

    public class EpiSlot{
        public Episode episode;
        public int season_num;
        public boolean isSeason;
        public int epi_num;

        public EpiSlot(Boolean isSeason){
            this.isSeason = isSeason;

        }


    }



}
