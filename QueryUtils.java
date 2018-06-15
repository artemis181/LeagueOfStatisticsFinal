package com.example.iyengara18.leagueofstatistics;

import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public final class QueryUtils {

    public static int summonerId;
    public static int accountId;
    private static String REQUEST_CHAMP_URL;
    private static String REQUEST_MATCH_DATA_URL;
    private static String REQUEST_ITEM_URL;
    private static String REQUEST_PLAYER_INFO;
    private static final String API_KEY = "RGAPI-858f2b46-2af9-42b9-aa8f-a94b9da46ef4";

    final static String LOG_TAG = MainActivity.class.getSimpleName();

    /** Sample JSON response for a USGS query */
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static ArrayList<ChampMasteryInfo> extractMasteryInfo(String JSONResponse) {

        // Create an empty ArrayList that we can start adding matches to
        ArrayList<ChampMasteryInfo> champMasteries = new ArrayList<>();

        try {
            JSONArray masteryArray = new JSONArray(JSONResponse);
            String name="";
            String epithet="";
            int masteryLevel;
            int pointsLeft;
            int tokens;
            int champId;
            for(int i=4;i<7;i++){
                ChampMasteryInfo mastery;
                JSONObject masteryObj = masteryArray.getJSONObject(i);
                masteryLevel = masteryObj.getInt("championLevel");
                pointsLeft = masteryObj.getInt("championPointsUntilNextLevel");
                tokens = masteryObj.getInt("tokensEarned");
                champId = masteryObj.getInt("championId");
                REQUEST_CHAMP_URL = "https://na1.api.riotgames.com/lol/static-data/v3/champions/"+champId+"?locale=en_US&api_key="+API_KEY;
                String champJSON = fetchChampName(REQUEST_CHAMP_URL);
                name = (String)extractChampName(champJSON).get(0);
                epithet = (String)extractChampName(champJSON).get(1);
                    mastery = new ChampMasteryInfo(name, epithet, masteryLevel, tokens);
                champMasteries.add(mastery);
            }
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the Mastery JSON results", e);
        }

        // Return the list of earthquakes
        return champMasteries;
    }

    public static ArrayList extractChampName(String JSONResponse){
        String champName="";
        String champEpithet="";
        ArrayList champInfo = new ArrayList();
        try{
            JSONObject champ = new JSONObject(JSONResponse);
            champName = champ.getString("name");
            champEpithet = champ.getString("title");
        }catch(JSONException e){
            Log.e("QueryUtils", "Problem parsing the champ results");
        }
        champInfo.add(champName);
        champInfo.add(champEpithet);
        return champInfo;
    }

    public static ArrayList extractPlayerData(String JSONResponse){
        ArrayList playerData = new ArrayList();
        int champId;
        String champJSON;
        try{
            JSONArray root = new JSONArray(JSONResponse);
            for(int i=0;i<3;i++){
                JSONObject champ = root.getJSONObject(i);
                champId = champ.getInt("championId");
                REQUEST_CHAMP_URL = "https://na1.api.riotgames.com/lol/static-data/v3/champions/"+champId+"?locale=en_US&api_key="+API_KEY;
                champJSON = fetchChampName(REQUEST_CHAMP_URL);
                playerData.add(extractChampName(champJSON).get(0));
            }
        }catch(JSONException e){
            Log.e("QueryUtils", "Problem parsing fav champ results");
        }
        return playerData;
    }
    public static ArrayList extractSummonerId(String JSONResponse){
        ArrayList info = new ArrayList();
        try{
            JSONObject playerInfo = new JSONObject(JSONResponse);
            summonerId = playerInfo.getInt("id");
            accountId = playerInfo.getInt("accountId");
            info.add(summonerId);
            info.add(accountId);
        }catch(JSONException e) {
            Log.e("QueryUtils", "Problem parsing summoner id results"+JSONResponse);
        }
        return info;
    }

    public static ArrayList<MatchHistoryInfo> extractMatchData(String JSONResponse){
        ArrayList<MatchHistoryInfo> matchHistory = new ArrayList<>();
        ArrayList infoFromMatch = new ArrayList();
        String matchInfoJSON;
        int champId;
        long matchId;
        String name;
        String KDA;
        int[] itemsByNumber;
        boolean result;
        String resultString;
        try{
            for(int i=3;i<5;i++){
                JSONObject champIdRoot = new JSONObject(JSONResponse);
                JSONArray matches = champIdRoot.getJSONArray("matches");
                JSONObject matchData = matches.getJSONObject(i);
                champId = matchData.getInt("champion");
                matchId = matchData.getLong("gameId");
                REQUEST_MATCH_DATA_URL = "https://na1.api.riotgames.com/lol/match/v3/matches/"+matchId+"?api_key="+API_KEY;
                matchInfoJSON = fetchChampId(REQUEST_MATCH_DATA_URL);
                result = extractResult(matchInfoJSON);
                REQUEST_CHAMP_URL = "https://na1.api.riotgames.com/lol/static-data/v3/champions/"+champId+"?locale=en_US&api_key="+API_KEY;
                String champJSON = fetchChampName(REQUEST_CHAMP_URL);
                name = (String)extractChampName(champJSON).get(0);
                KDA = extractKDA(matchInfoJSON);
                itemsByNumber = extractItemNumbers(matchInfoJSON);
                String[] itemList = new String[6];
                String itemJSON;
                for(int j=0;j<3;j++){
                    REQUEST_ITEM_URL = "https://na1.api.riotgames.com/lol/static-data/v3/items/"+itemsByNumber[j]+"?locale=en_US&api_key="+API_KEY;
                    itemJSON=fetchItems(REQUEST_ITEM_URL);
                    itemList[j]=extractItems(itemJSON);
                }
                if(result){
                    resultString = "Win";
                }else{
                    resultString = "Loss";
                }
                matchHistory.add(new MatchHistoryInfo(resultString, name, itemList, KDA));
            }
        }catch(JSONException e){
            Log.e("QueryUtils", "Problem parsing match history results");
        }
        return matchHistory;
    }

    public static String extractItems(String JSONResponse){
        String itemName="";
        try{
            JSONObject root = new JSONObject(JSONResponse);
            itemName = root.getString("name");
        }catch(JSONException e){
            Log.e("QueryUtils", "Problem parsing item name results");
        }

        return itemName;
    }

    public static int[] extractItemNumbers(String JSONResponse){
        int[] itemsByNumber = new int[3];
        //TODO get the items by number and make another extract method to get the item names
        int gottenId;
        int playerNum=0;
        try{
            JSONObject root = new JSONObject(JSONResponse);
            JSONArray participants = root.getJSONArray("participants");
            JSONArray participantIdentities = root.getJSONArray("participantIdentities");
            for(int i=0;i<10;i++){
                gottenId = participantIdentities.getJSONObject(i).getJSONObject("player").getInt("accountId");
                if( gottenId == accountId){
                    playerNum = i;
                }
            }
            JSONObject perp = participants.getJSONObject(playerNum);
            JSONObject stats = perp.getJSONObject("stats");
            itemsByNumber[0]=stats.getInt("item0");
            itemsByNumber[1]=stats.getInt("item1");
            itemsByNumber[2]=stats.getInt("item2");
//            itemsByNumber[3]=stats.getInt("item3");
//            itemsByNumber[4]=stats.getInt("item4");
//            itemsByNumber[5]=stats.getInt("item5");
        }catch(JSONException e){
            Log.e("QueryUtils", "Problem parsing item number results");
        }
        return itemsByNumber;
    }

    public static String extractKDA(String JSONResponse){
        int kills=0;
        int deaths=0;
        int assists=0;
        int gottenId;
        int playerNum=0;
        try{
            JSONObject root = new JSONObject(JSONResponse);
            JSONArray participants = root.getJSONArray("participants");
            JSONArray participantIdentities = root.getJSONArray("participantIdentities");
            for(int i=0;i<10;i++) {
                gottenId = participantIdentities.getJSONObject(i).getJSONObject("player").getInt("accountId");
                if (gottenId == accountId) {
                    playerNum = i;
                }
            }
            kills = participants.getJSONObject(playerNum).getJSONObject("stats").getInt("kills");
            deaths = participants.getJSONObject(playerNum).getJSONObject("stats").getInt("deaths");
            assists = participants.getJSONObject(playerNum).getJSONObject("stats").getInt("assists");
        }catch(JSONException e){
            Log.e("QueryUtils", "Problem parsing kills results");
        }
        return kills+"/"+deaths+"/"+assists;
    }

    public static boolean extractResult(String JSONResponse){
        boolean result=false;
        int playerNum=0;
        int gottenId=0;
        try{
            JSONObject root = new JSONObject(JSONResponse);
            JSONArray participants = root.getJSONArray("participants");
            JSONArray participantIdentities = root.getJSONArray("participantIdentities");
            for(int i=0;i<10;i++){
                gottenId = participantIdentities.getJSONObject(i).getJSONObject("player").getJSONObject("stats").getInt("accountId");
                if( gottenId == accountId){
                    playerNum = i;
                }
            }
            result = participants.getJSONObject(playerNum).getJSONObject("stats").getBoolean("win");
        }catch(JSONException e){
            Log.e("QueryUtils", "Problem parsing match specific results");
        }
        return result;
    }

    public static int extractLevel(String JSONResponse){
        int level=0;
        try{
            JSONObject root = new JSONObject(JSONResponse);
            level = root.getInt("summonerLevel");
        }catch(JSONException e){
            Log.e("QueryUtils", "Error parsing level results");
        }
        return level;
    }

    public static ArrayList extractTime(String JSONResponse){
        ArrayList time = new ArrayList();
        long epoch=0;
        try{
            JSONObject root = new JSONObject(JSONResponse);
            JSONArray matches = root.getJSONArray("matches");
            JSONObject latest = matches.getJSONObject(0);
            epoch = latest.getLong("timestamp");
        }catch(JSONException e){
            Log.e("QueryUtils", "Problem parsing time");
        }
        Date dt = new Date(epoch);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        time.add(hours);
        time.add(minutes);
        return time;
    }

    public static List<ChampMasteryInfo> fetchMasteryData(String requestUrl){
        URL url = createUrl(requestUrl);
        String JSONResponse = null;
        try{
            JSONResponse = makeHttpRequest(url);
        }catch(IOException e){
            Log.e(LOG_TAG, "Error creating connection");
        }
        if(JSONResponse.equals("")){
            Log.e(LOG_TAG, "WTF");
        }
        List<ChampMasteryInfo> champMasteries = extractMasteryInfo(JSONResponse);
        return champMasteries;
    }

    public static ArrayList fetchSummonerId(String requestUrl){
        URL url = createUrl(requestUrl);
        String JSONResponse = null;
        try{
            JSONResponse = makeHttpRequest(url);
        }catch(IOException e){
            Log.e(LOG_TAG, "Error creating connection");
        }
        return extractSummonerId(JSONResponse);
    }

    public static String fetchItems(String requestUrl){
        URL url = createUrl(requestUrl);
        String JSONResponse = null;
        try{
            JSONResponse = makeHttpRequest(url);
        }catch(IOException e){
            Log.e(LOG_TAG, "Error creating connection");
        }
        return JSONResponse;
    }

    public static String fetchChampId(String requestUrl){
        URL url = createUrl(requestUrl);
        String JSONResponse = null;
        try{
            JSONResponse = makeHttpRequest(url);
        }catch(IOException e){
            Log.e(LOG_TAG, "Error creating connection");
        }
        return JSONResponse;
    }

    public static ArrayList fetchTime(String requestUrl){
        URL url = createUrl(requestUrl);
        String JSONResponse = null;
        try{
            JSONResponse = makeHttpRequest(url);
        }catch(IOException e){
            Log.e(LOG_TAG, "Error creating connection");
        }
        return extractTime(JSONResponse);
    }

    public static String fetchChampName(String requestUrl){
        URL url = createUrl(requestUrl);
        String JSONResponse = null;
        try{
            JSONResponse = makeHttpRequest(url);
        }catch(IOException e){
            Log.e(LOG_TAG, "Error creating connection");
        }
        return JSONResponse;
    }

    public static ArrayList fetchPlayerData(String requestUrl, String name){
        URL url = createUrl(requestUrl);
        String JSONResponse = null;
        String mName = name;
        try{
            JSONResponse = makeHttpRequest(url);
        }catch(IOException e){
            Log.e(LOG_TAG, "Error creating connection");
        }
        ArrayList playerData = extractPlayerData(JSONResponse);
        REQUEST_PLAYER_INFO = "https://na1.api.riotgames.com/lol/summoner/v3/summoners/by-name/"+mName+"?api_key="+API_KEY;
        String levelJSON = fetchLevelData(REQUEST_PLAYER_INFO);
        playerData.add(extractLevel(levelJSON));
        return playerData;
    }

    public static String fetchLevelData(String requestUrl){
        URL url = createUrl(requestUrl);
        String JSONResponse = null;
        try{
            JSONResponse = makeHttpRequest(url);
        }catch(IOException e){
            Log.e(LOG_TAG, "Error creating connection");
        }
        return JSONResponse;
    }

    public static List<MatchHistoryInfo> fetchMatchData(String requestUrl){
        URL url = createUrl(requestUrl);
        String JSONResponse = null;
        try{
            JSONResponse = makeHttpRequest(url);
        }catch(IOException e){
            Log.e(LOG_TAG, "Error creating connection");
        }
        List<MatchHistoryInfo> matchHistory = extractMatchData(JSONResponse);
        return matchHistory;
    }

    private static URL createUrl(String urlString){
        URL url = null;
        try{
            url = new URL(urlString);
        }catch(MalformedURLException e){
            Log.e(LOG_TAG, "Error creating URL", e);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(URL url)throws IOException{
        String JSONResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
//            urlConnection.setRequestProperty("Content-length", "0");
//            urlConnection.setUseCaches(false);
//            urlConnection.setAllowUserInteraction(false);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setReadTimeout(10000);
            urlConnection.connect();
            if(urlConnection.getResponseCode()==200 || urlConnection.getResponseCode()==201){
                inputStream = urlConnection.getInputStream();
                JSONResponse = readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG, "Error response code:" + urlConnection.getResponseCode());
            }
        }catch(IOException e){
            Log.e(LOG_TAG, "Problem retrieving the JSON results");
        }finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return JSONResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null) {
                output.append(line+"\n");
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}

