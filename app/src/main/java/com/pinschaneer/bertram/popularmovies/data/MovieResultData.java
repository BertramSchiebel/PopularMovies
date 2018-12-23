package com.pinschaneer.bertram.popularmovies.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class parse the single result of holds its data
 * the results which will be  parse are from commands like /movie/popular or /movie/top_rated
 */
public class MovieResultData {

    private static final String TAG = MovieResultData.class.getSimpleName();

    private static final String MDB_ID = "id";
    private static final String MDB_POSTER_PATH = "poster_path";

    private String posterPath;
    private int id;

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

        } catch (JSONException e) {
            Log.e(TAG, "Input is not a valid JSON string");
            return null;
        }
        return resultData;
    }


    /**
     * Sets the part of url to the path ov the movie poster
     *
     * @param posterPath the given path part
     */
    private void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    /**
     * Gets the complete path to the movie poster
     *
     * @return a URL string to the complete path
     */
    public String getPosterImageUrl() {
        return "https://image.tmdb.org/t/p/w500" + posterPath;
    }


    /**
     * Gets tje Id from the movie
     *
     * @return the movie Id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the Id of the movie
     * @param id the given Id
     */
    private void setId(int id) {
        this.id = id;
    }

}
