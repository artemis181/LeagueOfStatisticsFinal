package com.example.iyengara18.leagueofstatistics;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MatchHistory extends Fragment implements LoaderManager.LoaderCallbacks<List<MatchHistoryInfo>>{
    private RecyclerView mRecyclerView;
    private MatchAdapter matchAdapter;
    private static final int MATCH_HISTORY_ID=1;
    private static final String API_KEY="RGAPI-858f2b46-2af9-42b9-aa8f-a94b9da46ef4";
    private static String REQUEST_MATCH_URL="https://na1.api.riotgames.com/lol/match/v3/matchlists/by-account/"+Info_Tab_Layout.accountId+"?api_key="+API_KEY;


    public MatchHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_match_history, container, false);
        mRecyclerView = rootView.findViewById(R.id.recycle);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        updateUI();

        return rootView;
    }


    private void updateUI(){
        matchAdapter = new MatchAdapter(new ArrayList<MatchHistoryInfo>());
        mRecyclerView.setAdapter(matchAdapter);

        getLoaderManager().initLoader(MATCH_HISTORY_ID, null, this);
    }

    @Override
    public Loader<List<MatchHistoryInfo>> onCreateLoader(int i, Bundle bundle) {
        return new MatchHistoryLoader(this.getContext(), REQUEST_MATCH_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<MatchHistoryInfo>> loader, List<MatchHistoryInfo> matches){
        if(matches != null && !matches.isEmpty()){
            matchAdapter.setData(matches);
            matchAdapter.notifyDataSetChanged();
        }else{
            //mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<MatchHistoryInfo>> loader){
        matchAdapter.setData(new ArrayList<MatchHistoryInfo>());
    }

    private class MatchHolder extends RecyclerView.ViewHolder{
        private TextView mMatchResultTextView;
        private TextView mChampionUsedTextView;
        private TextView mKDATextView;
        private TextView itemOne;
        private TextView itemTwo;
        private TextView itemThree;
        private TextView itemFour;
        private TextView itemFive;
        private TextView itemSix;
        private MatchHistoryInfo mMatchHistory;

        public MatchHolder(View itemView){
            super(itemView);
            mMatchResultTextView = itemView.findViewById(R.id.matchResultView);
            mChampionUsedTextView = itemView.findViewById(R.id.championView);
            itemOne = itemView.findViewById((R.id.itemOneView));
            itemTwo = itemView.findViewById((R.id.itemTwoView));
            itemThree = itemView.findViewById((R.id.itemThreeView));
//            itemFour = itemView.findViewById((R.id.itemFourView));
//            itemFive = itemView.findViewById((R.id.itemFiveView));
//            itemSix = itemView.findViewById((R.id.itemSixView));
            mKDATextView = itemView.findViewById(R.id.KDAView);
        }

        public void bindMatch(MatchHistoryInfo matchHistory){
            mMatchHistory = matchHistory;
            String[] itemsName = mMatchHistory.getItems();
            mMatchResultTextView.setText(mMatchHistory.getMatchResult());
            mChampionUsedTextView.setText(mMatchHistory.getChampUsed());
            mKDATextView.setText(mMatchHistory.getKDA()+"");
            itemOne.setText(itemsName[0]);
            itemTwo.setText(itemsName[1]);
            itemThree.setText(itemsName[2]);
//            itemFour.setText(itemsName[3]);
//            itemFive.setText(itemsName[4]);
//            itemSix.setText(itemsName[5]);
        }
    }

    private class MatchAdapter extends RecyclerView.Adapter<MatchHolder>{
        private List<MatchHistoryInfo> mMatches;

        public MatchAdapter(List<MatchHistoryInfo> matches){
            mMatches = matches;
        }

        public void setData(List<MatchHistoryInfo> matches){
            mMatches = matches;
        }

        @Override
        public MatchHolder onCreateViewHolder(ViewGroup parent, int viewTyper){
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(R.layout.list_item_matches, parent, false);
            return new MatchHolder(view);
        }

        @Override
        public void onBindViewHolder(MatchHolder holder, int position){
            MatchHistoryInfo matchHistoryInfo = mMatches.get(position);
            holder.bindMatch(matchHistoryInfo);
        }

        @Override
        public int getItemCount(){
            return mMatches.size();
        }
    }

}