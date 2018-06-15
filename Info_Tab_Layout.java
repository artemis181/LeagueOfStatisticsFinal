package com.example.iyengara18.leagueofstatistics;

import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Info_Tab_Layout extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<ArrayList>{
    ViewPager vPager;
    TabLayout tabLayout;
    SampleFragmentPagerAdapter fragAdapt;
    TextView name;
    Button wotd;
    int hours=15;
    int min=37;
    private static final int DATE_ID=1;
    private static final String EXTRA_SUMNAME = "com.example.iyengara18.leagueofstatistics.name";
    private static final String EXTRA_SUMID = "com.example.iyengara18.leagueofstatistics.id";
    private static final String EXTRA_ACCID = "com.example.iyengara18.leagueofstatistics.acc";
    private static final String API_KEY="RGAPI-858f2b46-2af9-42b9-aa8f-a94b9da46ef4";
    private static String REQUEST_TIME_URL="https://na1.api.riotgames.com/lol/match/v3/matchlists/by-account/211078242?api_key="+API_KEY;
    public static String summonerName;
    public static int summonerId;
    public static int accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_tab_layout);
        vPager = findViewById(R.id.viewPager);
        fragAdapt = new SampleFragmentPagerAdapter(getSupportFragmentManager());
        vPager.setAdapter(fragAdapt);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(vPager);
        name = findViewById(R.id.nameView);
        summonerName=getIntent().getStringExtra(EXTRA_SUMNAME);
        summonerId=getIntent().getIntExtra(EXTRA_SUMID, 0);
        accountId=getIntent().getIntExtra(EXTRA_ACCID, 0);
        name.setText(getIntent().getStringExtra(EXTRA_SUMNAME));
        wotd = findViewById(R.id.WoTDView);
        wotd.setOnClickListener(this);
    }

    public static Intent newIntent(Context packageContext,String summonerName, int sumId, int accId){
        Intent i = new Intent(packageContext, Info_Tab_Layout.class);
        i.putExtra(EXTRA_SUMNAME, summonerName);
        i.putExtra(EXTRA_SUMID, sumId);
        i.putExtra(EXTRA_ACCID, accId);
        return i;
    }

    @Override
    public void onClick(View view){
        Intent i = new Intent(android.provider.AlarmClock.ACTION_SET_ALARM);
        getSupportLoaderManager().initLoader(DATE_ID, null, this);
        i.putExtra(android.provider.AlarmClock.EXTRA_HOUR, hours);
        i.putExtra(android.provider.AlarmClock.EXTRA_MINUTES, min);
        Toast.makeText(this, "Timer set", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Loader<ArrayList> onCreateLoader(int i, Bundle bundle) {
        return new DateLoader(this, REQUEST_TIME_URL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList> loader, ArrayList times){
        hours = (int)times.get(0);
        min = (int)times.get(1);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList> loader){
    }
}
