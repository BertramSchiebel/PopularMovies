package com.pinschaneer.bertram.popularmovies.activities;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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

import com.pinschaneer.bertram.popularmovies.R;
import com.pinschaneer.bertram.popularmovies.activities.ViewModel.MainViewModel;
import com.pinschaneer.bertram.popularmovies.activities.ViewModel.MainViewModelFactory;
import com.pinschaneer.bertram.popularmovies.data.MovieDBPageResult;
import com.pinschaneer.bertram.popularmovies.data.MovieListAdapter;
import com.pinschaneer.bertram.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;


/**
 * This class is responsible to display the views of the main activity
 */
public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieListAdapterOnClickHandler, AdapterView.OnItemSelectedListener {

    private RecyclerView mMovieListRecyclerView;

    private MainViewModel viewModel;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private Spinner mMovieQuerySpinner;
    private ProgressBar mLoadingPageIndicator;
    private AsyncTask<String, MovieDBPageResult, MovieDBPageResult> mFetchMovieDataTask;

    /**
     * Display a error message
     */
    private void showErrorMessage() {
        mMovieListRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    /**
     * Display the results of an Network request.
     */
    private void showMovieResults() {
        mMovieListRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

    /**
     * Create the activity
     *
     * @param savedInstanceState the saved instant state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int spanCount;
        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanCount = 2;
        }
        else {
            spanCount = 3;
        }

        mMovieListRecyclerView = findViewById(R.id.rv_movie_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        mMovieListRecyclerView.setLayoutManager(layoutManager);
        viewModel = ViewModelProviders.of(this, new MainViewModelFactory(this.getApplication(), this)).get(MainViewModel.class);


        mMovieListRecyclerView.setAdapter(viewModel.getMovieListAdapter());

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mMovieQuerySpinner = findViewById(R.id.sp_switch_move_query);
        Resources res = getResources();
        String[] mPossibleMovieSelections = res.getStringArray(R.array.movie_list_queries);
        mMovieQuerySpinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> movieQuerySpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mPossibleMovieSelections);
        movieQuerySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mMovieQuerySpinner.setAdapter(movieQuerySpinnerAdapter);

        if (!viewModel.hasData()) {
            mFetchMovieDataTask = new FetchMovieDataTask(this);
        }
        mLoadingPageIndicator = findViewById(R.id.pb_display_page_count);
    }


    /**
     * Load the movie data in an async task
     */
    private void loadMovieData() {
        if (null != mFetchMovieDataTask) {
            AsyncTask.Status status = mFetchMovieDataTask.getStatus();
            if (status != AsyncTask.Status.RUNNING) {
                viewModel.getMovieListAdapter().clearMovieData();
                mFetchMovieDataTask = new FetchMovieDataTask(this).execute(viewModel.getCommand());
            }
        }
    }


    /**
     * Event handler for the click on a movie poster
     * This handler will start a new activity with the detailed movie data
     *
     * @param movieData the data of the clicked movie poster
     */
    @Override
    public void onClick(MovieDBPageResult.ResultData movieData) {
        Context context = this;
        Intent startDetailedMovieActivity = new Intent(context, DetailedMovieData.class);
        startDetailedMovieActivity.putExtra(Intent.EXTRA_TEXT, Integer.toString(movieData.getId()));
        startActivity(startDetailedMovieActivity);
    }

    /**
     * The event handler for the spinner of the sorting for the movies
     *
     * @param adapterView the click adapte view
     * @param view        the clicked view
     * @param pos         the position of the adapter clicked
     * @param id          the Id
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

        switch (pos) {
            case 0:
                viewModel.setCommand(getString(R.string.most_popular_command));
                break;
            case 1:
                viewModel.setCommand(getString(R.string.top_rated_command));
                break;

            default:
                viewModel.setCommand(getString(R.string.most_popular_command));
        }

        loadMovieData();
    }

    /**
     * Auto generated method of  the interface AdapterView.OnItemSelectedListener
     * @param adapterView the adapter view
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //auto generated stub
    }


    /**
     * Async task to get the requested movie data from the network
     */
    @SuppressLint("StaticFieldLeak")
    private class FetchMovieDataTask extends AsyncTask<String, MovieDBPageResult, MovieDBPageResult> {

        private final Context mContext;
        private int mCurrentPageLoading;


        /**
         * constructor
         * @param context the context of the task
         */
        private FetchMovieDataTask(Context context) {
            this.mContext = context;
        }

        /**
         * Adjustment before the execution
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMovieQuerySpinner.setEnabled(false);
            mLoadingPageIndicator.setProgress(0);
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mLoadingPageIndicator.setVisibility(View.VISIBLE);
        }


        /**
         * The task that is done asynchronously
         * @param searchParams thr parameter for the network request
         * @return null
         */
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
                    publishProgress(pageResult);
                    mCurrentPageLoading++;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            } while (mCurrentPageLoading <= totalPages);

            return null;
        }


        /**
         * Displays the progress of the async task
         * @param values the data of the search progres
         */
        @Override
        protected void onProgressUpdate(MovieDBPageResult... values) {
            super.onProgressUpdate(values);

            if (values.length > 0) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                viewModel.getMovieListAdapter().setMovieData(values[0].getResults());
                mLoadingPageIndicator.setProgress(mCurrentPageLoading);
                showMovieResults();
            }
        }


        /**
         * Displays the summery of network request or an error message
         * @param movieDBPageResult always null
         */
        @Override
        protected void onPostExecute(MovieDBPageResult movieDBPageResult) {
            super.onPostExecute(movieDBPageResult);

            mMovieQuerySpinner.setEnabled(true);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mLoadingPageIndicator.setVisibility(View.INVISIBLE);
            if (viewModel.getMovieListAdapter().getItemCount() == 0) {
                mErrorMessageDisplay.setText(R.string.error_message);
                showErrorMessage();
            } else {

                String msg = String.format(Locale.getDefault(), getString(R.string.message_loaded_data_set), viewModel.getMovieListAdapter().getItemCount());
                Toast.makeText(mContext, msg, Toast.LENGTH_LONG)
                        .show();

            }
        }
    }
}
