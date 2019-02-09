package com.pinschaneer.bertram.popularmovies.activities;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.pinschaneer.bertram.popularmovies.activities.MovieListAdapter;

public class MainViewModel extends AndroidViewModel
{
    private MovieListAdapter movieListAdapter;

    private String command = "movie/popular";

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

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
