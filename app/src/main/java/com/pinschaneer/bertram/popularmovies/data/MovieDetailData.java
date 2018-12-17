package com.pinschaneer.bertram.popularmovies.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class parse the JSON string for detaild movie informations and
 * holds its data after parisng
 */
public class MovieDetailData {
    private static final String TAG = MovieDetailData.class.getSimpleName();

    private static final String MDB_TITLE = "title";
    private static final String MDB_OVERVIEW = "overview";
    private static final String MDB_RELEASE_DATE = "release_date";
    private static final String MDB_POSTER_PATH = "poster_path";
    private static final String MDB_VOTE_AVERAGE = "vote_average";

    private String mTitle;
    private String mDescription;
    private Date mReleaseDate;
    private String mPosterPath;
    private double mAverageVote;

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

            if (movieDataJSON.has(MDB_POSTER_PATH)) {
                movieDetailData.setPosterPath(movieDataJSON.getString(MDB_POSTER_PATH));
            }

            if (movieDataJSON.has(MDB_RELEASE_DATE)) {
                String dateString = movieDataJSON.getString(MDB_RELEASE_DATE);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date releaseDate;
                try {
                    releaseDate = format.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                    releaseDate = null;
                }
                movieDetailData.setReleaseDate(releaseDate);
            }

            if (movieDataJSON.has(MDB_VOTE_AVERAGE)) {
                movieDetailData.setAverageVote(movieDataJSON.getDouble(MDB_VOTE_AVERAGE));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return movieDetailData;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.mReleaseDate = releaseDate;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        this.mPosterPath = posterPath;
    }

    public double getAverageVote() {
        return mAverageVote;
    }

    public void setAverageVote(double averageVote) {
        this.mAverageVote = averageVote;
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

    public String getPosterImageUrl() {
        if (mPosterPath != null && !mPosterPath.isEmpty()) {

            String url = "https://image.tmdb.org/t/p/w500" + mPosterPath;
            return url;
        } else {
            return "";
        }
    }

}
