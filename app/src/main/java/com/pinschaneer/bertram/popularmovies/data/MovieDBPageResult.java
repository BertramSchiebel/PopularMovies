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

    private ArrayList<ResultData> mResults;

    public MovieDBPageResult() {
        mResults = new ArrayList<>();
    }

    /**
     * Factory method to create a instance of this class according to a given JSON data string
     *
     * @param jsonDataString the JSON data string
     * @return a Instance of this class or null if parsing has an error
     */
    public static MovieDBPageResult createMovieDBPageResult(String jsonDataString
    ) {
        MovieDBPageResult movieDBPageResult = new MovieDBPageResult();
        try {

            JSONObject listSearchResult = new JSONObject(jsonDataString);

            if (listSearchResult.has(MDB_RESULTS)) {
                JSONArray resultList = listSearchResult.getJSONArray(MDB_RESULTS);
                for (int i = 0; i < resultList.length(); i++) {
                    JSONObject entryJson = resultList.getJSONObject(i);
                    ResultData movieResultEntry = ResultData.createMovieResultData(entryJson.toString());
                    if (null != movieResultEntry) {
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
    public ArrayList<ResultData> getResults() {
        return mResults;
    }


    /**
     * This class parse the single result and holds its data
     */
    public static class ResultData {

        private static final String TAG = ResultData.class.getSimpleName();

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
        public static ResultData createMovieResultData(String resultJsonString) {
            ResultData resultData = new ResultData();
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
         *
         * @param id the given Id
         */
        private void setId(int id) {
            this.id = id;
        }

    }
}
