package com.pinschaneer.bertram.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.pinschaneer.bertram.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView mMovieDisplayTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMovieDisplayTextView = findViewById(R.id.tv_movie_display);

        loadMovieData();
    }

    private void loadMovieData() {
        new FetchMovieDataTask().execute("popular");
    }

    public class FetchMovieDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... searchParams) {
            if (searchParams.length == 0) {
                return null;
            }

            URL movieDbUrl = NetworkUtils.buildUrl(searchParams[0]);
            try {
                String response = NetworkUtils.getResponseFromHttpUrl(movieDbUrl);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String movieData) {
            if (movieData != null) {
                mMovieDisplayTextView.setText(movieData);

            }
        }
    }
}
