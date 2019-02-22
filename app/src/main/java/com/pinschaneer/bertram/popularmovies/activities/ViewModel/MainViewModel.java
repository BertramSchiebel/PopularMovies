package com.pinschaneer.bertram.popularmovies.activities.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.pinschaneer.bertram.popularmovies.data.MovieListAdapter;

public class MainViewModel extends AndroidViewModel
{
    private MovieListAdapter movieListAdapter;

    private String command = "movie/popular";

    public MainViewModel(Application application) {
        super(application);
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void Init(MovieListAdapter.MovieListAdapterOnClickHandler clickHandler) {
        if (movieListAdapter == null) {
            movieListAdapter = new MovieListAdapter(clickHandler);
        }
    }

    public MovieListAdapter getMovieListAdapter() {
        return movieListAdapter;
    }

    public boolean hasData() {
        return (movieListAdapter.getItemCount() > 0);
    }
}
