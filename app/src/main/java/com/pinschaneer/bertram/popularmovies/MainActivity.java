package com.pinschaneer.bertram.popularmovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pinschaneer.bertram.popularmovies.data.MovieDBPageResult;
import com.pinschaneer.bertram.popularmovies.data.MovieResultData;
import com.pinschaneer.bertram.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieListAdapterOnClickHandler, AdapterView.OnItemSelectedListener {
    private String mCommand = "movie/popular";
    private RecyclerView mMovieListRecyclerView;
    private MovieListAdapter mMovieListAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private Spinner mMovieQuerySpinner;
    private ProgressBar mLoadingPageIndicator;
    private AsyncTask<String, MovieDBPageResult, MovieDBPageResult> mFetchMovieDataTask;

    private void showErrorMessage() {
        mMovieListRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showMovieResults() {
        mMovieListRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

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

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mMovieQuerySpinner = findViewById(R.id.sp_switch_move_query);
        Resources res = getResources();
        String[] mPossibleMovieSelections = res.getStringArray(R.array.movie_list_queries);
        mMovieQuerySpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> movieQuerySpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mPossibleMovieSelections);
        movieQuerySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mMovieQuerySpinner.setAdapter(movieQuerySpinnerAdapter);

        mFetchMovieDataTask = new FetchMovieDataTask(this);

        mLoadingPageIndicator = findViewById(R.id.pb_display_page_count);
    }

    private void loadMovieData() {
        if (null != mFetchMovieDataTask) {
            AsyncTask.Status status = mFetchMovieDataTask.getStatus();
            if (status != AsyncTask.Status.RUNNING) {
                mMovieListAdapter.clearMovieData();
                mFetchMovieDataTask = new FetchMovieDataTask(this).execute(mCommand);
            }
        }
    }


    @Override
    public void onClick(MovieResultData movieData) {
        Context context = this;
        Intent startDetailedMovieActivity = new Intent(context, DetailedMovieData.class);
        startDetailedMovieActivity.putExtra(Intent.EXTRA_TEXT, Integer.toString(movieData.getId()));
        startActivity(startDetailedMovieActivity);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

        switch (pos) {
            case 0:
                mCommand = getString(R.string.most_popular_command);
                break;
            case 1:
                mCommand = getString(R.string.top_rated_command);
                break;

            default:
                mCommand = getString(R.string.most_popular_command);
        }

        loadMovieData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //auto generated stub
    }


    @SuppressLint("StaticFieldLeak")
    private class FetchMovieDataTask extends AsyncTask<String, MovieDBPageResult, MovieDBPageResult> {

        private final Context mContext;
        private int mCurrentPageLoading;


        private FetchMovieDataTask(Context context) {
            this.mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMovieQuerySpinner.setEnabled(false);
            mLoadingPageIndicator.setProgress(0);
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mLoadingPageIndicator.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onCancelled(MovieDBPageResult movieDBPageResult) {
            super.onCancelled(movieDBPageResult);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            onPostExecute(null);
            mLoadingPageIndicator.setVisibility(View.INVISIBLE);
            mMovieQuerySpinner.setEnabled(true);

        }


        @Override
        protected MovieDBPageResult doInBackground(String... searchParams) {
            if (searchParams.length == 0) {
                return null;
            }
            int totalPages = 5;
            mCurrentPageLoading = 1;
            mLoadingPageIndicator.setProgress(mCurrentPageLoading);
            MovieDBPageResult pageResult;
            mLoadingPageIndicator.setMax(totalPages);

            do {
                URL movieDbUrl = NetworkUtils.buildUrl(searchParams[0], Integer.toString(mCurrentPageLoading));
                try {
                    String response = NetworkUtils.getResponseFromHttpUrl(movieDbUrl);
                    pageResult = MovieDBPageResult.createMovieDBPageResult(response);
                    //totalPages = pageResult.getTotalPages();
                    publishProgress(pageResult);
                    mCurrentPageLoading++;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            } while (mCurrentPageLoading <= totalPages);

            return null;
        }

        @Override
        protected void onProgressUpdate(MovieDBPageResult... values) {
            super.onProgressUpdate(values);

            if (values.length > 0) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mMovieListAdapter.setMovieData(values[0].getResults());
                mLoadingPageIndicator.setProgress(mCurrentPageLoading);
                showMovieResults();
            }
        }

        @Override
        protected void onPostExecute(MovieDBPageResult movieDBPageResult) {
            super.onPostExecute(movieDBPageResult);

            mMovieQuerySpinner.setEnabled(true);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mLoadingPageIndicator.setVisibility(View.INVISIBLE);
            if (mMovieListAdapter.getItemCount() == 0) {
                mErrorMessageDisplay.setText(R.string.error_message);
                showErrorMessage();
            } else {

                String msg = String.format(Locale.getDefault(), getString(R.string.message_loaded_data_set), mMovieListAdapter.getItemCount());
                Toast.makeText(mContext, msg, Toast.LENGTH_LONG)
                        .show();

            }
        }
    }
}
