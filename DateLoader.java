package com.example.iyengara18.leagueofstatistics;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

public class DateLoader extends AsyncTaskLoader<ArrayList> {

    private String mUrl;

    public DateLoader(Context context, String url){
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public ArrayList loadInBackground(){
        ArrayList time = QueryUtils.fetchTime(mUrl);
        return time;
    }
}
