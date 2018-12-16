package com.pinschaneer.bertram.popularmovies.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieResultData {
    public static final String MDB_ID = "id";
    public static final String MDB_POSTER_PATH = "poster_path";
    public static final String MDB_TITLE = "title";
    private static final String TAG = MovieResultData.class.getSimpleName();

    public static MovieResultData createMovieResultData(String resultJsonString) {
        MovieResultData resultData = new MovieResultData();
        try {
            JSONObject movieDataJSON = new JSONObject(resultJsonString);
            if (movieDataJSON.has(MDB_ID)) {
                resultData.setId(movieDataJSON.getInt(MDB_ID));
            } else {
                Log.e(TAG, "Id is not available");
                return null;
            }
            if (movieDataJSON.has(MDB_POSTER_PATH)) {
                resultData.setPosterPath(movieDataJSON.getString(MDB_POSTER_PATH));
            }

            if (movieDataJSON.has(MDB_TITLE)) {
                resultData.setTitle(movieDataJSON.getString(MDB_TITLE));
            }

        } catch (JSONException e) {
            Log.e(TAG, "Input is not a valid JSAON stiring");
            return null;
        }
        return resultData;
    }

    private String posterPath;

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    private int id;

    public void setId(int id) {
        this.id = id;
    }

    private String title;

    public void setTitle(String title) {
        this.title = title;
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
