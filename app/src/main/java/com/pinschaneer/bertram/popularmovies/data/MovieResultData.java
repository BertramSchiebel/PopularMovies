package com.pinschaneer.bertram.popularmovies.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieResultData {
    public static final String MDB_ID = "id";
    public static final String MDB_POSTER_PATH = "poster_path";
    public static final String MDB_TITLE = "title";
    private static final String TAG = MovieResultData.class.getSimpleName();
    private String posterPath;
    private int id;
    private String title;

    public MovieResultData(String resultJsonString) {
        try {
            JSONObject movieDataJSON = new JSONObject(resultJsonString);
            if (movieDataJSON.has(MDB_ID)) {
                id = movieDataJSON.getInt(MDB_ID);
            } else {
                id = -1;
                Log.e(TAG, "Id is not available");
            }
            if (movieDataJSON.has(MDB_POSTER_PATH)) {
                posterPath = movieDataJSON.getString(MDB_POSTER_PATH);
            } else {
                posterPath = "";
            }
            if (movieDataJSON.has(MDB_TITLE)) {
                title = movieDataJSON.getString(MDB_TITLE);
            } else {
                title = "";
            }

        } catch (JSONException e) {
            Log.e(TAG, "Input is not a valid JSAON stiring");
            id = -1;
            posterPath = null;
            title = null;
        }
    }

    public int getId() {
        return id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Id: " + getId() + "; Titel: " + getTitle();
    }
}
