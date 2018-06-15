package com.example.iyengara18.leagueofstatistics;

public class MatchHistoryInfo {

    private String mMatchResult;
    private String mChampUsed;
    private String mKDA;
    private String[] mItems;

    public MatchHistoryInfo(String matchResult, String champUsed, String[] items, String KDA){
        mMatchResult = matchResult;
        mChampUsed = champUsed;
        mKDA = KDA;
        mItems = items;
    }

    public String getMatchResult(){
        return mMatchResult;
    }

    public void setMatchResult(String courseTitle){
        mMatchResult = courseTitle;
    }

    public String getChampUsed(){
        return mChampUsed;
    }

    public void setChampUsed(String courseInstructor){
        mChampUsed = courseInstructor;
    }

    public String getKDA(){
        return mKDA;
    }

    public void setKDA(String KDA){
        mKDA = KDA;
    }

    public String[] getItems() {
        return mItems;
    }

    public void setmItems(String[] items){
        mItems = items;
    }
}