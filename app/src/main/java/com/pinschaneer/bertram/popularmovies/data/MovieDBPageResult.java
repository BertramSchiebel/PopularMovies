package com.pinschaneer.bertram.popularmovies.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class parse the response of the themoviedb.org server and holds the data
 * of results with more the one page like the commands /movie/popular or /movie/top_rated
 */
public class MovieDBPageResult {
    private static final String TAG = MovieDBPageResult.class.getSimpleName();

    private static final String MDB_RESULTS = "results";

    private final ArrayList<MovieDataEntry> mResults;

    private MovieDBPageResult() {
        mResults = new ArrayList<>();
    }

    /**
     * Factory method to create a instance of this class according to a given JSON data string
     *
     * @param jsonDataString the JSON data string
     * @return a Instance of this class or null if parsing has an error
     */
    public static MovieDBPageResult parseMovieDbPageResult(String jsonDataString
    ) {
        MovieDBPageResult movieDBPageResult = new MovieDBPageResult();
        try {

            JSONObject listSearchResult = new JSONObject(jsonDataString);

            if (listSearchResult.has(MDB_RESULTS)) {
                    JSONArray resultList = listSearchResult.getJSONArray(MDB_RESULTS);
                    for (int i = 0; i < resultList.length(); i++) {
                        JSONObject entryJson = resultList.getJSONObject(i);
                        MovieDataEntry movieResultEntry = new MovieDataEntry(entryJson.toString());
                        if (movieResultEntry.getId() > 0) {
                            movieDBPageResult.getResults().add(movieResultEntry);
                        }
                }

            } else {
                Log.e(TAG, "Input is not a valid JSON string");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return movieDBPageResult;
    }

    /**
     * @return the array list of movie entries from the JSON string
     */
    public ArrayList<MovieDataEntry> getResults() {
        return mResults;
    }

}
