package com.pinschaneer.bertram.popularmovies.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pinschaneer.bertram.popularmovies.R;
import com.pinschaneer.bertram.popularmovies.data.MovieDetailData;
import com.pinschaneer.bertram.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Locale;

/**
 * This class is responsible for the detaild view of movie
 */
public class DetailedMovieData extends AppCompatActivity {

    private String mDetailedMovieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_movie_data);
        this.setTitle(R.string.detailed_movie_title);

        Intent startActivityIntent = getIntent();

        if (startActivityIntent != null) {
            if (startActivityIntent.hasExtra(Intent.EXTRA_TEXT)) {
                mDetailedMovieId = startActivityIntent.getStringExtra(Intent.EXTRA_TEXT);
            }
        }

        loadMovieDetails();
    }

    /**
     * load the details of a movie by a network request
     */
    private void loadMovieDetails() {
        String command = "movie/" + mDetailedMovieId;
        new FetchMovieDetailData().execute(command);

    }

    /**
     * Set the visibility of the views in this activity
     * During loading from the Network most of the views are invisible
     * after receiving the data the viability will change with this method.
     *
     * @param loadingIsActive Indicates weather loading from network is active or not.
     */
    private void displayLoadingIsActive(boolean loadingIsActive) {
        int detailVisibility = View.VISIBLE;
        int progressVisibility = View.INVISIBLE;
        if (loadingIsActive) {
            detailVisibility = View.INVISIBLE;
            progressVisibility = View.VISIBLE;
        }

        ProgressBar loadingIndicator = findViewById(R.id.detail_loading_indicator);
        loadingIndicator.setVisibility(progressVisibility);

        TextView displayTitle = findViewById(R.id.tv_movie_detail_title);
        displayTitle.setVisibility(detailVisibility);

        TextView displayDescription = findViewById(R.id.tv_movie_detail_description);
        displayDescription.setVisibility(detailVisibility);

        TextView displayRating = findViewById(R.id.movie_detail_rating);
        displayRating.setVisibility(detailVisibility);

        TextView displayReleaseDate = findViewById(R.id.movie_detail_release_date);
        displayReleaseDate.setVisibility(detailVisibility);

        ImageView poster = findViewById(R.id.movie_detail_image);
        poster.setVisibility(detailVisibility);
    }


    /**
     * Displays the details of a movie in the intended views
     *
     * @param movieDetails the given detailed data of the movie
     */
    private void populateDisplayInformation(MovieDetailData movieDetails) {
        TextView displayTitle = findViewById(R.id.tv_movie_detail_title);
        displayTitle.setText(movieDetails.getTitle());

        TextView displayDescription = findViewById(R.id.tv_movie_detail_description);
        displayDescription.setText(movieDetails.getDescription());

        TextView displayRating = findViewById(R.id.movie_detail_rating);
        String rating = String.format(Locale.getDefault(), "%.1f/10", movieDetails.getAverageVote());

        String ratingText = rating;
        displayRating.setText(ratingText);

        TextView displayReleaseDate = findViewById(R.id.movie_detail_release_date);
        Locale current = getResources().getConfiguration().locale;
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, current);
        String releaseDate = dateFormat.format(movieDetails.getReleaseDate());

        String releaseDateText = releaseDate;
        displayReleaseDate.setText(releaseDateText);

        ImageView poster = findViewById(R.id.movie_detail_image);
        Picasso.get()
                .load(movieDetails.getPosterImageUrl())
                .placeholder(R.drawable.default_poster)
                .error(R.drawable.error_poster)
                .into(poster);
    }


    /**
     * Displays an error Message
     */
    private void showErrorMessage() {
        displayLoadingIsActive(true);
        ProgressBar loadingIndicator = findViewById(R.id.detail_loading_indicator);
        loadingIndicator.setVisibility(View.INVISIBLE);
        TextView errorMessage = findViewById(R.id.detail_error_message);
        Resources res = getResources();
        errorMessage.setText(res.getText(R.string.error_message));
        errorMessage.setVisibility(View.VISIBLE);
    }

    /**
     * The Async task to get the movie details from the network
     */
    @SuppressLint("StaticFieldLeak")
    class FetchMovieDetailData extends AsyncTask<String, Void, String> {

        /**
         * Settings before executing the async task
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            displayLoadingIsActive(true);
        }

        /**
         * runs an async task to get data from the internet
         *
         * @param params the parameter to build the URL for the network request
         * @return A JSON string of the network request or null if the request fails
         */
        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String response;
            URL movieDbUrl = NetworkUtils.buildUrl(params[0], "1");
            try {
                response = NetworkUtils.getResponseFromHttpUrl(movieDbUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        /**
         * after the task is finished the received data will be processed
         * @param response the received data from the network request
         */
        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                MovieDetailData movieDetails = MovieDetailData.crateMovieDetailData(response);
                if (movieDetails != null) {
                    displayLoadingIsActive(false);
                    populateDisplayInformation(movieDetails);
                }
            } else {
                showErrorMessage();
            }
        }
    }
}
