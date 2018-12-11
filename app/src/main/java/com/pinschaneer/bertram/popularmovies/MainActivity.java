package com.pinschaneer.bertram.popularmovies;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        URL movieDbUrl = NetworkUtils.buildUrl("popular");
        try {
            String response = NetworkUtils.getResponseFromHttpUrl(movieDbUrl);
            if (response != null) {
                mMovieDisplayTextView.setText(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
