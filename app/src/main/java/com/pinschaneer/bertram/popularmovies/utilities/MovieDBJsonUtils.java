package com.pinschaneer.bertram.popularmovies.utilities;

import android.util.Log;

import com.pinschaneer.bertram.popularmovies.data.MovieResultData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieDBJsonUtils {
    public static final String MDB_RESULTS = "results";
    private static final String TAG = MovieDBJsonUtils.class.getSimpleName();

    public static ArrayList<MovieResultData> getMovieResults(String movieResultsJson) {
        ArrayList<MovieResultData> movieResults = new ArrayList<>();
        try {
            JSONObject resultJson = new JSONObject(movieResultsJson);
            if (resultJson.has(MDB_RESULTS)) {
                JSONArray resultList = resultJson.getJSONArray(MDB_RESULTS);
                for (int i = 0; i < resultList.length(); i++) {
                    JSONObject entryJson = resultList.getJSONObject(i);
                    MovieResultData movieResultEntry = new MovieResultData(entryJson.toString());
                    movieResults.add(movieResultEntry);
                }

            } else {
                Log.e(TAG, "input sting has wrong JSON Form");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieResults;
    }

}
