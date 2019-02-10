package com.pinschaneer.bertram.popularmovies.activities.ViewModel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.pinschaneer.bertram.popularmovies.data.MovieListAdapter;

public class MainViewModelFactory implements ViewModelProvider.Factory
{
    private MovieListAdapter.MovieListAdapterOnClickHandler clickHandler;
    private Application application;

    public MainViewModelFactory(Application application,
                                MovieListAdapter.MovieListAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(application, clickHandler);
    }
}
