package com.example.iyengara18.leagueofstatistics;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

public class FavLoader extends AsyncTaskLoader<ArrayList> {

    private String mUrl;
    private String mName;

    public FavLoader(Context context, String url, String name){
        super(context);
        mUrl = url;
        mName = name;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public ArrayList loadInBackground(){
        ArrayList fav = QueryUtils.fetchPlayerData(mUrl, mName);
        return fav;
    }
}
