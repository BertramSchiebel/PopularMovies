package com.pinschaneer.bertram.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pinschaneer.bertram.popularmovies.data.MovieResultData;
import com.pinschaneer.bertram.popularmovies.utilities.MovieDBJsonUtils;
import com.pinschaneer.bertram.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieListAdapterOnClickHandler {
    private RecyclerView mMovieListRecyclerView;
    private MovieListAdapter mMovieListAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingInidcattor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieListRecyclerView = findViewById(R.id.rv_movie_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mMovieListRecyclerView.setLayoutManager(layoutManager);
        mMovieListAdapter = new MovieListAdapter(this);
        mMovieListRecyclerView.setAdapter(mMovieListAdapter);



        loadMovieData();
    }

    private void loadMovieData() {
        new FetchMovieDataTask().execute("popular");
    }

    @Override
    public void onClick(MovieResultData movieData) {
        Context context = this;
        Toast.makeText(context, movieData.toString(), Toast.LENGTH_SHORT)
                .show();
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
                ArrayList<MovieResultData> movieDataList = MovieDBJsonUtils.getMovieResults(movieData);
                mMovieListAdapter.setMovieData(movieDataList);

            }
        }
    }
}
