package com.pinschaneer.bertram.popularmovies.data;


import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.pinschaneer.bertram.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * This class parse the JSON string for detailed movie information and
 * holds its data after parsing
 */
public class MovieDetailData {
    private static final String MDB_TITLE = "title";
    private static final String MDB_ID = "id";
    private static final String MDB_OVERVIEW = "overview";
    private static final String MDB_RELEASE_DATE = "release_date";
    private static final String MDB_POSTER_PATH = "poster_path";
    private static final String MDB_VOTE_AVERAGE = "vote_average";
    private static final String MDB_RESULTS = "results";

    private String title;
    private String description;
    private Date releaseDate;
    private String posterPath;
    private double averageVote;
    private int id;
    private MutableLiveData<ArrayList<TrailerEntry>> videos;

    private MutableLiveData<ArrayList<ReviewEntry>> reviews;

    public MovieDetailData(){
        videos = new MutableLiveData<>();
        reviews = new MutableLiveData<>();
    }

    /**
     * Factory method to parse the given JSON string and returns
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
            if (movieDataJSON.has(MDB_ID)) {
                movieDetailData.setId(movieDataJSON.getInt(MDB_ID));
            }

            if (movieDataJSON.has(MDB_OVERVIEW)) {
                movieDetailData.setDescription(movieDataJSON.getString(MDB_OVERVIEW));
            }

            if (movieDataJSON.has(MDB_POSTER_PATH)) {
                movieDetailData.setPosterPath(movieDataJSON.getString(MDB_POSTER_PATH));
            }

            if (movieDataJSON.has(MDB_RELEASE_DATE)) {
                String dateString = movieDataJSON.getString(MDB_RELEASE_DATE);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
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

            movieDetailData.loadMovieVideos(movieDetailData.getId());
            movieDetailData.loadReviews(movieDetailData.getId());

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return movieDetailData;
    }

    public MutableLiveData<ArrayList<ReviewEntry>> getReviews() {
        return reviews;
    }

    private void loadReviews(int movieId) {
        String command = "movie/" + movieId + "/reviews";
        new RetrieveReviewDataTask().execute(command);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MutableLiveData<ArrayList<TrailerEntry>> getVideos() {
        return videos;
    }

    /**
     * @return gets the release Date of the movie
     */
    public Date getReleaseDate() {
        return releaseDate;
    }

    /**
     * Sets the release Date of the movie
     *
     * @param releaseDate the given date
     */
    private void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath(){return posterPath;}
    /**
     * Sets the path to image of the movie poster
     *
     * @param posterPath the given path
     */
    private void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    /**
     * @return the avarage vote of the movie
     */
    public double getAverageVote() {
        return averageVote;
    }

    /**
     * Sets the average vote of the movie
     *
     * @param averageVote the given average vote
     */
    private void setAverageVote(double averageVote) {
        this.averageVote = averageVote;
    }

    /**
     * @return Gets the title of the movie details
     */
    public String getTitle() {
        return title;
    }


    /**
     * Sets the title of the movie
     * @param title the given title
     */
    private void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return Gets the description of the movie details
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the movie
     * @param description the given description
     */
    private void setDescription(String description) {
        this.description = description;
    }

    private void loadMovieVideos(int movieId){

        String command = "movie/" + movieId+"/videos";
        new RetrieveVideoDataTask().execute(command);
    }

    private ArrayList<ReviewEntry> parseReviewsResonse(String response) {
        ArrayList<ReviewEntry> result = new ArrayList<>();
        try {
            JSONObject jasonResponse = new JSONObject(response);
            if (jasonResponse.has(MDB_RESULTS)) {
                JSONArray resultList = jasonResponse.getJSONArray(MDB_RESULTS);
                for (int i = 0; i < resultList.length(); i++) {
                    JSONObject jsonEntry = resultList.getJSONObject(i);
                    ReviewEntry review = ReviewEntry.crateReviewData(jsonEntry);
                    if (review != null) {
                        result.add(review);
                    }
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private ArrayList<TrailerEntry> parseGetVideoResonse(String response) {
        ArrayList<TrailerEntry> result = new ArrayList<>();
        try {
            JSONObject jasonResponse = new JSONObject(response);
            if (jasonResponse.has(MDB_RESULTS)) {
                JSONArray resultList = jasonResponse.getJSONArray(MDB_RESULTS);
                for (int i = 0; i < resultList.length(); i++) {
                    JSONObject jsonEntry = resultList.getJSONObject(i);
                    TrailerEntry trailer = TrailerEntry.crateTrailerData(jsonEntry);
                    if (trailer != null) {
                        result.add(trailer);
                    }
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * Gets a URL string to the movie poster
     * @return the complete URL if the movie has a path to a image otherwise a empty string
     */
    public String getPosterImageUrl() {
        if (posterPath != null && !posterPath.isEmpty()) {

            return "https://image.tmdb.org/t/p/w500" + posterPath;
        } else {
            return "";
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class RetrieveVideoDataTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            String command = params[0];
            URL url = NetworkUtils.buildUrl(command, "1");
            String response;
            try {
                response = NetworkUtils.getResponseFromHttpUrl(url);
            }
            catch (IOException e) {
                e.printStackTrace();
                return "";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (!response.isEmpty()){
                ArrayList<TrailerEntry> videoList = parseGetVideoResonse(response);
                if (videoList != null) {
                    videos.postValue(videoList);
                }
            }
        }
    }

    private class RetrieveReviewDataTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params) {
            String command = params[0];
            URL url = NetworkUtils.buildUrl(command, "1");
            String response;
            try {
                response = NetworkUtils.getResponseFromHttpUrl(url);
            }
            catch (IOException e) {
                e.printStackTrace();
                return "";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (!response.isEmpty()) {
                ArrayList<ReviewEntry> reviewList = parseReviewsResonse(response);
                if (reviewList != null) {
                    reviews.postValue(reviewList);
                }
            }
        }
    }
}
