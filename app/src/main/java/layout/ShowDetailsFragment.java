package layout;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.example.aguilarcreations.tvlog.FloatingActionMenuAnimator;
import com.example.aguilarcreations.tvlog.Item;
import com.example.aguilarcreations.tvlog.MovieViewModel;
import com.example.aguilarcreations.tvlog.R;
import com.example.aguilarcreations.tvlog.Show;
import com.example.aguilarcreations.tvlog.ShowViewModel;

import java.util.ArrayList;

/**
 * Use the {@link ShowDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowDetailsFragment extends Fragment {

    ShowDetailsFragment fragment;
    ShowViewModel showViewModel;
    Show show;
    EpisodeBaseAdapter episodeBaseAdapter;

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
            showViewModel.loadShowDetails(Integer.toString(show.getTrakt_id()));
        }
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MovieViewModel.DETAILS_LOADED);
        intentFilter.addAction(MovieViewModel.ADDED_WATCHLIST);
        intentFilter.addAction(MovieViewModel.ADDED_FINISHED);
        intentFilter.addAction(MovieViewModel.REMOVED_WATCHLIST);
        intentFilter.addAction(MovieViewModel.REMOVED_FINISHED);
        intentFilter.addAction(MovieViewModel.GOT_SEASONS);
        intentFilter.addAction(ShowViewModel.GOT_EPISODE_DATA);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(showReceiver, intentFilter);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Lifecycle", "MovieDetailsFragment onCreateView");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_show_details, container, false);
        setupActionBar();
        setupActionMenu(v);
        showViewModel.getSeasons();


        return v;
    }




    private void setupSeasonSpinner(View v, int current_season, String[] seasons){
        Spinner spinner = v.findViewById(R.id.season_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                 android.R.layout.simple_spinner_item, seasons);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(current_season);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Update episode listview
                ArrayList<Episode> list = new ArrayList<>();
                for (Episode episode : showViewModel.getEpisodes(i)) {
                    list.add(episode);
                }
                ListView episode_listview = fragment.getView().findViewById(R.id.episode_listview);
                EpisodeBaseAdapter episodeBaseAdapter;
                episodeBaseAdapter = new EpisodeBaseAdapter(getActivity(), list, getResources());
                episode_listview.setAdapter(episodeBaseAdapter);

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

    private void setupActionMenu(View v){
        FloatingActionMenuAnimator.BtnData[] btnData = new FloatingActionMenuAnimator.BtnData[2];
        btnData[0] = new FloatingActionMenuAnimator.BtnData("Stop Tracking",R.drawable.ic_remove_black_24dp);
        btnData[1] = new FloatingActionMenuAnimator.BtnData("Start Tracking",R.drawable.ic_watchlist_black_24dp);
        FloatingActionMenuAnimator floatingActionMenuAnimator = FloatingActionMenuAnimator.build(v, btnData);
        floatingActionMenuAnimator.getActionButton(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showViewModel.stopTracking();
            }
        });
        floatingActionMenuAnimator.getActionButton(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showViewModel.startTracking();
            }
        });
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
            Log.e("Alejandro", "-----action: " + intent.getAction());
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
                if(getView() != null) {
                    Toast.makeText(context, "Got Seasons", Toast.LENGTH_LONG).show();

                    String[] seasons = new String[showViewModel.getNumSeasons()];
                    for (int i = 0; i < showViewModel.getNumSeasons(); i++) {
                        seasons[i] = "Season " + i;
                    }

                    setupSeasonSpinner(getView(), 1, seasons);


                    ArrayList<Episode> list = new ArrayList<>();
                    for (Episode episode : showViewModel.getEpisodes(1)) {
                        list.add(episode);
                    }
                    ListView episode_listview = fragment.getView().findViewById(R.id.episode_listview);
                    episodeBaseAdapter = new EpisodeBaseAdapter(getActivity(), list, getResources());
                    episode_listview.setAdapter(episodeBaseAdapter);
                    showViewModel.getFinishedEpisodes(show.getTrakt_id());
                }
            } else if(ShowViewModel.GOT_EPISODE_DATA.equals(intent.getAction())){
                Toast.makeText(context, "Got Episode Data!", Toast.LENGTH_LONG).show();
                ArrayList<Episode> list = new ArrayList<>();
                for (Episode episode : showViewModel.getEpisodes(1)) {
                    list.add(episode);
                }
                ListView episode_listview = fragment.getView().findViewById(R.id.episode_listview);
                episodeBaseAdapter = new EpisodeBaseAdapter(getActivity(), list, getResources());
                episode_listview.setAdapter(episodeBaseAdapter);
            }
        }
    };




}
