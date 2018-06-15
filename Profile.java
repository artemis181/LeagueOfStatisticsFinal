package com.example.iyengara18.leagueofstatistics;


import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList>{
    private String champOne;
    private String champTwo;
    private String champThree;
    int level;
    TextView champOneView;
    TextView champTwoView;
    TextView champThreeView;
    TextView levelView;
    private static final int CHAMP_ID=1;
    private static final String API_KEY = "RGAPI-858f2b46-2af9-42b9-aa8f-a94b9da46ef4";
    private static String REQUEST_URL = "https://na1.api.riotgames.com/lol/champion-mastery/v3/champion-masteries/by-summoner/"+Info_Tab_Layout.summonerId+"?api_key="+API_KEY;


    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        champOneView = view.findViewById(R.id.champOneView);
        champTwoView = view.findViewById(R.id.champTwoView);
        champThreeView = view.findViewById(R.id.champThreeView);
        levelView = view.findViewById(R.id.level);
        getLoaderManager().initLoader(CHAMP_ID, null,this);
        return view;
    }

    @Override
    public Loader<ArrayList> onCreateLoader(int i, Bundle bundle) {
        return new FavLoader(this.getContext(), REQUEST_URL, Info_Tab_Layout.summonerName);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList> loader, ArrayList x){
        champOne = (String)x.get(0);
        champTwo = (String)x.get(1);
        champThree = (String)x.get(2);
        level = (int)x.get(3);
        champOneView.setText("Favorite Champion: "+champOne);
        champTwoView.setText("Second Favorite: "+champTwo);
        champThreeView.setText("Third Favorite"+champThree);
        levelView.setText("You are currently level "+ level);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList> loader){
    }

}
