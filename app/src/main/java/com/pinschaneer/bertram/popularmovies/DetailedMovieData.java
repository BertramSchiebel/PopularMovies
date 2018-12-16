package com.pinschaneer.bertram.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.pinschaneer.bertram.popularmovies.data.MovieDetailData;
import com.pinschaneer.bertram.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class DetailedMovieData extends AppCompatActivity {
    private TextView mDisplayIdTextView;
    private TextView mDisplayDescriptionTextView;
    private String mDetailedMovieId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_movie_data);

        mDisplayIdTextView = findViewById(R.id.tv_movie_detail_id);
        mDisplayDescriptionTextView = findViewById(R.id.tv_movie_detail_description);
        Intent startActivityIntent = getIntent();

        if (startActivityIntent != null) {
            if (startActivityIntent.hasExtra(Intent.EXTRA_TEXT)) {
                mDetailedMovieId = startActivityIntent.getStringExtra(Intent.EXTRA_TEXT);
                mDisplayIdTextView.setText(mDetailedMovieId);
            }
        }

        loadMovieDetails();
    }

    private void loadMovieDetails() {
        String command = "movie/" + mDetailedMovieId;
        new FetchMovieDetailData().execute(command);

    }

    class FetchMovieDetailData extends AsyncTask<String, Void, String> {

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
                MovieDetailData details = MovieDetailData.crateMovieDetailData(response);
                if (details != null) {
                    mDisplayDescriptionTextView.setText(details.getDescription());
                }
            }
        }
    }
}
