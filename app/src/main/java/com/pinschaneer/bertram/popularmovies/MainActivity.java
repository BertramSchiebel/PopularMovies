package com.pinschaneer.bertram.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pinschaneer.bertram.popularmovies.data.MovieDBPageResult;
import com.pinschaneer.bertram.popularmovies.data.MovieResultData;
import com.pinschaneer.bertram.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieListAdapterOnClickHandler {
    public static final String MDB_GET_POPULAR_COMMAND = "movie/popular";
    private RecyclerView mMovieListRecyclerView;
    private MovieListAdapter mMovieListAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingInidcattor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieListRecyclerView = findViewById(R.id.rv_movie_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMovieListRecyclerView.setLayoutManager(layoutManager);
        mMovieListAdapter = new MovieListAdapter(this);
        mMovieListRecyclerView.setAdapter(mMovieListAdapter);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);
        mLoadingInidcattor = findViewById(R.id.pb_loading_indicator);

        loadMovieData();
    }

    private void loadMovieData() {
        new FetchMovieDataTask(this).execute(MDB_GET_POPULAR_COMMAND);
    }

    private void showErrorMessage() {
        mMovieListRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(MovieResultData movieData) {
        Context context = this;
        Intent startDetaildMovieActivity = new Intent(context, DetailedMovieData.class);
        startDetaildMovieActivity.putExtra(Intent.EXTRA_TEXT, Integer.toString(movieData.getId()));
        startActivity(startDetaildMovieActivity);
    }

    public class FetchMovieDataTask extends AsyncTask<String, MovieDBPageResult, MovieDBPageResult> {

        private Context mContext;

        public FetchMovieDataTask(Context context) {
            this.mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingInidcattor.setVisibility(View.VISIBLE);
        }

        @Override
        protected MovieDBPageResult doInBackground(String... searchParams) {
            if (searchParams.length == 0) {
                return null;
            }
            int totalPages = 20;
            int aktualPage = 1;
            MovieDBPageResult pageResult = null;
            do {
                URL movieDbUrl = NetworkUtils.buildUrl(searchParams[0], Integer.toString(aktualPage));
                try {
                    String response = NetworkUtils.getResponseFromHttpUrl(movieDbUrl);
                    pageResult = MovieDBPageResult.createMovieDBPageResult(response);
                    //totalPages = pageResult.getTotalPages();
                    publishProgress(pageResult);
                    aktualPage++;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            } while (aktualPage < totalPages);

            return null;
        }

        @Override
        protected void onProgressUpdate(MovieDBPageResult... values) {
            super.onProgressUpdate(values);
            if (values.length > 0) {
                mLoadingInidcattor.setVisibility(View.INVISIBLE);
                mMovieListAdapter.setMovieData(values[0].getResults());
            }
        }

        @Override
        protected void onPostExecute(MovieDBPageResult movieDBPageResult) {
            super.onPostExecute(movieDBPageResult);
            mLoadingInidcattor.setVisibility(View.INVISIBLE);
            if (mMovieListAdapter.getItemCount() == 0) {
                mErrorMessageDisplay.setText(R.string.error_message);
                showErrorMessage();
            } else {

                String msg = String.format("Download finished, received : %d data sets", mMovieListAdapter.getItemCount());
                Toast.makeText(mContext, msg, Toast.LENGTH_LONG)
                        .show();
            }
        }
    }
}
