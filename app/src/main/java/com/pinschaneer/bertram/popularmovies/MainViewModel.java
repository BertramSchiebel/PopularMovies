package com.pinschaneer.bertram.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

public class MainViewModel extends AndroidViewModel
{
    private MovieListAdapter movieListAdapter;

    public MainViewModel(Application application,
                         MovieListAdapter.MovieListAdapterOnClickHandler clickHandler) {
        super(application);
        movieListAdapter = new MovieListAdapter(clickHandler);
    }

    public MovieListAdapter getMovieListAdapter() {
        return movieListAdapter;
    }

    public boolean hasData() {
        return (movieListAdapter.getItemCount() > 0);
    }
}
