package com.pinschaneer.bertram.popularmovies.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.pinschaneer.bertram.popularmovies.R;
import com.pinschaneer.bertram.popularmovies.data.DataBaseExecutor;
import com.pinschaneer.bertram.popularmovies.data.MovieDataEntry;
import com.pinschaneer.bertram.popularmovies.data.MovieDetailData;
import com.pinschaneer.bertram.popularmovies.data.ReviewEntry;
import com.pinschaneer.bertram.popularmovies.data.ReviewListAdapter;
import com.pinschaneer.bertram.popularmovies.data.TrailerEntry;
import com.pinschaneer.bertram.popularmovies.data.TrailerListAdapter;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * This class is responsible for the detailed view of movie
 */
public class DetailedMovieDataActivity extends AppCompatActivity implements TrailerListAdapter.TrailerListAdapterOnClickHandler
{

    private int mDetailedMovieId;
    private DetailedMovieDataViewModel viewModel;

    private RecyclerView recyclerViewVideos;
    private TrailerListAdapter videoListAdapter;

    private RecyclerView recyclerViewReviews;
    private ReviewListAdapter reviewListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_movie_data);
        this.setTitle(R.string.detailed_movie_title);

        Intent startActivityIntent = getIntent();

        if (startActivityIntent != null) {
            if (startActivityIntent.hasExtra(Intent.EXTRA_TEXT)) {
                mDetailedMovieId = Integer.parseInt(startActivityIntent.getStringExtra(Intent.EXTRA_TEXT));
            }
        }

        recyclerViewVideos = findViewById(R.id.recyclerview_videos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewVideos.setLayoutManager(layoutManager);
        recyclerViewVideos.setHasFixedSize(true);
        videoListAdapter = new TrailerListAdapter(this);
        recyclerViewVideos.setAdapter(videoListAdapter);

        recyclerViewReviews = findViewById(R.id.recyclerview_reviews);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewReviews.setLayoutManager(layoutManager);
        recyclerViewReviews.setHasFixedSize(true);
        reviewListAdapter = new ReviewListAdapter();
        recyclerViewReviews.setAdapter(reviewListAdapter);


        viewModel = ViewModelProviders.of(this).get(DetailedMovieDataViewModel.class);
        if (!viewModel.hasData()) {
            viewModel.init(mDetailedMovieId);
        }

        viewModel.getMovieData().observe(this, new Observer<MovieDetailData>()
        {
            @Override
            public void onChanged(@Nullable MovieDetailData movieDetails) {
                DetailedMovieDataActivity.this.populateDisplayInformation(movieDetails);
                movieDetails.getVideos().observe(DetailedMovieDataActivity.this, new Observer<ArrayList<TrailerEntry>>()
                {
                    @Override
                    public void onChanged(@Nullable ArrayList<TrailerEntry> trailerEntries) {
                        videoListAdapter.setTrailerEntries(trailerEntries);
                    }
                });

                movieDetails.getReviews().observe(DetailedMovieDataActivity.this, new Observer<ArrayList<ReviewEntry>>()
                {
                    @Override
                    public void onChanged(@Nullable ArrayList<ReviewEntry> reviewEntries) {
                        reviewListAdapter.setReviewEntries(reviewEntries);
                    }
                });
            }
        });



    }


    /**
     * Set the visibility of the views in this activity
     * During loading from the Network most of the views are invisible
     * after receiving the data the viability will change with this method.
     *
     * @param loadingIsActive Indicates weather loading from network is active or not.
     */
    private void displayLoadingIsActive(boolean loadingIsActive) {
        int detailVisibility = View.VISIBLE;
        int progressVisibility = View.INVISIBLE;
        if (loadingIsActive) {
            detailVisibility = View.INVISIBLE;
            progressVisibility = View.VISIBLE;
        }

        ProgressBar loadingIndicator = findViewById(R.id.detail_loading_indicator);
        loadingIndicator.setVisibility(progressVisibility);

        TextView displayTitle = findViewById(R.id.tv_movie_detail_title);
        displayTitle.setVisibility(detailVisibility);

        TextView displayDescription = findViewById(R.id.tv_movie_detail_description);
        displayDescription.setVisibility(detailVisibility);

        TextView displayRating = findViewById(R.id.movie_detail_rating);
        displayRating.setVisibility(detailVisibility);

        TextView displayReleaseDate = findViewById(R.id.movie_detail_release_date);
        displayReleaseDate.setVisibility(detailVisibility);

        ImageView poster = findViewById(R.id.movie_detail_image);
        poster.setVisibility(detailVisibility);
    }


    /**
     * Displays the details of a movie in the intended views
     *
     * @param movieDetails the given detailed data of the movie
     */
    private void populateDisplayInformation(MovieDetailData movieDetails) {
        displayLoadingIsActive(viewModel.isLoadingActive());
        if (viewModel.isLoadingActive()) {
            return;
        }

        if (!viewModel.isLoadingSuccessfull()) {
            showErrorMessage();
            return;
        }
        if (isMarkedAsFavorite(mDetailedMovieId)) {
            ToggleButton markFavorite = findViewById(R.id.toggleButtonMarkAsFavorite);
            markFavorite.setChecked(true);
        }

        TextView displayTitle = findViewById(R.id.tv_movie_detail_title);
        displayTitle.setText(movieDetails.getTitle());

        TextView displayDescription = findViewById(R.id.tv_movie_detail_description);
        displayDescription.setText(movieDetails.getDescription());

        TextView displayRating = findViewById(R.id.movie_detail_rating);

        displayRating.setText(String.format(Locale.getDefault(), "%.1f/10", movieDetails.getAverageVote()));

        TextView displayReleaseDate = findViewById(R.id.movie_detail_release_date);
        Locale current = getResources().getConfiguration().locale;
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, current);

        displayReleaseDate.setText(dateFormat.format(movieDetails.getReleaseDate()));

        ImageView poster = findViewById(R.id.movie_detail_image);
        Picasso.get().load(movieDetails.getPosterImageUrl()).placeholder(R.drawable.default_poster).error(R.drawable.error_poster).into(poster);
    }


    /**
     * Displays an error Message
     */
    private void showErrorMessage() {
        displayLoadingIsActive(true);
        ProgressBar loadingIndicator = findViewById(R.id.detail_loading_indicator);
        loadingIndicator.setVisibility(View.INVISIBLE);
        TextView errorMessage = findViewById(R.id.detail_error_message);
        Resources res = getResources();
        errorMessage.setText(res.getText(R.string.error_message));
        errorMessage.setVisibility(View.VISIBLE);
    }

    public void favoriteClicked(View view) {
        ToggleButton tb = (ToggleButton) view;
        if (tb != null) {
            if (tb.isChecked()) {

                // mark this movie as favorite
                markAsFavorite();
            }
            else {
                deleteAsFavorite();
            }
        }
    }

    private void deleteAsFavorite() {
        final MovieDataEntry itemToDelete = getFavoriteMovie(mDetailedMovieId);
        if (!(itemToDelete == null)) {
            viewModel.getFavoriteMovies().remove(itemToDelete);
            DataBaseExecutor.getInstance().diskIO().execute(new Runnable()
            {
                @Override
                public void run() {
                    viewModel.getMovieDataBase().movieDataDao().deleteMovieData(itemToDelete);
                }
            });
        }
    }

    private MovieDataEntry getFavoriteMovie(int mDetailedMovieId) {
        MovieDataEntry movieDataEntry = null;
        for (MovieDataEntry movie : viewModel.getFavoriteMovies()) {
            if (movie.getId() == mDetailedMovieId) {
                movieDataEntry = movie;
                break;
            }
        }
        return movieDataEntry;
    }

    private void markAsFavorite() {

        final MovieDataEntry movieDataEntry = viewModel.getMovieDataEntry();

        DataBaseExecutor.getInstance().diskIO().execute(new Runnable()
        {
            @Override
            public void run() {
                viewModel.getMovieDataBase().movieDataDao().insertMovieData(movieDataEntry);
            }
        });

    }

    private boolean isMarkedAsFavorite(int movieId) {
        for (MovieDataEntry movie : viewModel.getFavoriteMovies()) {
            if (movieId == movie.getId()) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onClick(TrailerEntry trailerData) {
        if (trailerData.isYouTubeVideo()) {
            Uri uri = trailerData.getYouTubeUri();
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        else {
            Toast toast = Toast.makeText(this, "Is not YouTube Video", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
