package com.pinschaneer.bertram.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailedMovieData extends AppCompatActivity {
    TextView mDisplayIdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_movie_data);

        mDisplayIdTextView = findViewById(R.id.tv_movie_detail_id);
        String mDetailedMovieId;

        Intent startActivityIntent = getIntent();

        if (startActivityIntent != null) {
            if (startActivityIntent.hasExtra(Intent.EXTRA_TEXT)) {
                mDetailedMovieId = startActivityIntent.getStringExtra(Intent.EXTRA_TEXT);
                mDisplayIdTextView.setText(mDetailedMovieId);
            }
        }
    }
}
