package com.pinschaneer.bertram.popularmovies.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class parse the single result of holds its data
 * the results which will be  parse are from commands like /movie/popular or /movie/top_rated
 */
public class MovieResultData {
    private static final String MDB_ID = "id";
    private static final String MDB_POSTER_PATH = "poster_path";
    private static final String MDB_TITLE = "title";
    private static final String TAG = MovieResultData.class.getSimpleName();

    private String title;

    private String posterPath;

    /**
     * Factory method to create a instance of this class according to a given JSON data string
     *
     * @param resultJsonString the JSON data string
     * @return a Instance of this class or null if parsing has an error
     */
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
            Log.e(TAG, "Input is not a valid JSON string");
            return null;
        }
        return resultData;
    }

    private int id;

    private void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getPosterImageUrl() {
        return "https://image.tmdb.org/t/p/w500" + posterPath;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    private String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Id: " + getId() + "; Title: " + getTitle();
    }
}
