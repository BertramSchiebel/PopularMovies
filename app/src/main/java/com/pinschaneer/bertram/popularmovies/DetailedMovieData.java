package com.pinschaneer.bertram.popularmovies;

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

import com.pinschaneer.bertram.popularmovies.data.MovieDetailData;
import com.pinschaneer.bertram.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Locale;

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

    private void loadMovieDetails() {
        String command = "movie/" + mDetailedMovieId;
        new FetchMovieDetailData().execute(command);

    }

    private void displayLoadingIsActive(boolean loadingIsActive) {
        int detailVisability = View.VISIBLE;
        int progressVisability = View.INVISIBLE;
        if (loadingIsActive) {
            detailVisability = View.INVISIBLE;
            progressVisability = View.VISIBLE;
        }

        ProgressBar loadingIndicator = findViewById(R.id.detail_loading_indicator);
        loadingIndicator.setVisibility(progressVisability);

        TextView displayTitle = findViewById(R.id.tv_movie_detail_title);
        displayTitle.setVisibility(detailVisability);

        TextView displayDescription = findViewById(R.id.tv_movie_detail_description);
        displayDescription.setVisibility(detailVisability);

        TextView dispayRating = findViewById(R.id.movie_detail_rating);
        dispayRating.setVisibility(detailVisability);

        TextView displayReleaseDate = findViewById(R.id.movie_detail_release_date);
        displayReleaseDate.setVisibility(detailVisability);

        ImageView poster = findViewById(R.id.movie_detail_image);
        poster.setVisibility(detailVisability);
    }

    private void populateDisplayInformation(MovieDetailData movieDetails) {
        TextView displayTitle = findViewById(R.id.tv_movie_detail_title);
        displayTitle.setText(movieDetails.getTitle());

        TextView displayDescription = findViewById(R.id.tv_movie_detail_description);
        displayDescription.setText(movieDetails.getDescription());

        TextView dispayRating = findViewById(R.id.movie_detail_rating);
        String rating = String.format(Locale.getDefault(), "%.1f/10", movieDetails.getAverageVote());

        Resources resources = getResources();
        String ratingtext = resources.getString(R.string.rating) + "\n" + rating;
        dispayRating.setText(ratingtext);

        TextView displayReleaseDate = findViewById(R.id.movie_detail_release_date);
        Locale current = getResources().getConfiguration().locale;
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, current);
        String releaseDate = dateFormat.format(movieDetails.getReleaseDate());

        String releaseDateText = resources.getString(R.string.release_date) + "\n" + releaseDate;
        displayReleaseDate.setText(releaseDateText);

        ImageView poster = findViewById(R.id.movie_detail_image);
        Picasso.get()
                .load(movieDetails.getPosterImageUrl())
                .into(poster);

    }

    private void showErrorMessage() {
        displayLoadingIsActive(true);
        ProgressBar loadingIndicator = findViewById(R.id.detail_loading_indicator);
        loadingIndicator.setVisibility(View.INVISIBLE);
        TextView errorMessage = findViewById(R.id.detail_error_message);
        Resources res = getResources();
        errorMessage.setText(res.getText(R.string.error_message));
        errorMessage.setVisibility(View.VISIBLE);
    }

    @SuppressLint("StaticFieldLeak")
    class FetchMovieDetailData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            displayLoadingIsActive(true);
        }

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
