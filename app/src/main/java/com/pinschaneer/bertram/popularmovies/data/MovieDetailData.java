package com.pinschaneer.bertram.popularmovies.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class parse the JSON string for detaild movie informations and
 * holds its data after parisng
 */
public class MovieDetailData {
    private static final String TAG = MovieDetailData.class.getSimpleName();

    private static final String MDB_TITLE = "title";
    private static final String MDB_OVERVIEW = "overview";

    private String mTitle;
    private String mDescription;


    /**
     * Factroy method to parse the given JSON string and returns
     * an instance of thi class
     *
     * @param jsonData the given JSON string to parse
     * @return An instance of this class with values of the given JSON string otherwise null
     */
    public static MovieDetailData crateMovieDetailData(String jsonData) {
        MovieDetailData movieDetailData = new MovieDetailData();
        try {
            JSONObject movieDataJSON = new JSONObject(jsonData);
            if (movieDataJSON.has(MDB_TITLE)) {
                movieDetailData.setTitle(movieDataJSON.getString(MDB_TITLE));
            }

            if (movieDataJSON.has(MDB_OVERVIEW)) {
                movieDetailData.setDescription(movieDataJSON.getString(MDB_OVERVIEW));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return movieDetailData;
    }

    /**
     * @return Gets the title of the movie details
     */
    public String getTitle() {
        return mTitle;
    }


    /**
     * @param title Set the title of the movie details
     */
    private void setTitle(String title) {
        this.mTitle = title;
    }

    /**
     * @return Gets the desxription of the movie details
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * @param description Sets the description of the movie details
     */
    private void setDescription(String description) {
        this.mDescription = description;
    }


}
