package com.pinschaneer.bertram.popularmovies.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.pinschaneer.bertram.popularmovies.R;
import com.pinschaneer.bertram.popularmovies.data.MovieDataEntry;
import com.pinschaneer.bertram.popularmovies.data.MovieListAdapter;

import java.util.ArrayList;


/**
 * This class is responsible to display the views of the main activity
 */
public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieListAdapterOnClickHandler, AdapterView.OnItemSelectedListener {

    private RecyclerView mMovieListRecyclerView;
    private MovieListAdapter movieListAdapter;
    private MainViewModel viewModel;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private Spinner mMovieQuerySpinner;

    /**
     * Display a error message
     */
    private void showErrorMessage() {
        mMovieListRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    /**
     * Display the results of an Network request.
     */
    private void showMovieResults() {
        mMovieListRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);

    }

    private void showLoadingIsActive() {
        mMovieListRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
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

        movieListAdapter = new MovieListAdapter(this);
        mMovieListRecyclerView.setAdapter(movieListAdapter);

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mMovieQuerySpinner = findViewById(R.id.sp_switch_move_query);
        Resources res = getResources();
        String[] mPossibleMovieSelections = res.getStringArray(R.array.movie_list_queries);
        mMovieQuerySpinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> movieQuerySpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mPossibleMovieSelections);
        movieQuerySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mMovieQuerySpinner.setAdapter(movieQuerySpinnerAdapter);

        setupViewModel();
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getMovieDataEntries().observe(this, new Observer<ArrayList<MovieDataEntry>>()
        {
            @Override
            public void onChanged(@Nullable ArrayList<MovieDataEntry> movieDataEntries) {
                movieListAdapter.setMovieData(movieDataEntries);
            }
        });

        viewModel.getIsLoading().observe(this, new Observer<Boolean>()
        {
            @Override
            public void onChanged(@Nullable Boolean isLoading) {
                if (isLoading) {
                    showLoadingIsActive();
                }
            }
        });

        viewModel.getHasData().observe(this, new Observer<Boolean>()
        {
            @Override
            public void onChanged(@Nullable Boolean hasData) {
                if (hasData) {
                    showMovieResults();
                }
            }
        });

        viewModel.getHasLoadingError().observe(this, new Observer<Boolean>()
        {
            @Override
            public void onChanged(@Nullable Boolean hasLoadingError) {
                if (hasLoadingError) {
                    showErrorMessage();
                }
            }
        });

    }

    /**
     * Event handler for the click on a movie poster
     * This handler will start a new activity with the detailed movie data
     *
     * @param movieData the data of the clicked movie poster
     */
    @Override
    public void onClick(MovieDataEntry movieData) {
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

        viewModel.loadMovieData();
    }

    /**
     * Auto generated method of  the interface AdapterView.OnItemSelectedListener
     * @param adapterView the adapter view
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //auto generated stub
    }

}
