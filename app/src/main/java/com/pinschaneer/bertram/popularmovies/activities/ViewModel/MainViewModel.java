package com.pinschaneer.bertram.popularmovies.activities.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.pinschaneer.bertram.popularmovies.data.MovieDBPageResult;
import com.pinschaneer.bertram.popularmovies.data.MovieDataEntry;
import com.pinschaneer.bertram.popularmovies.data.MovieListAdapter;
import com.pinschaneer.bertram.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainViewModel extends AndroidViewModel
{
    //private MovieListAdapter movieListAdapter;

    private String command = "movie/popular";

    private MutableLiveData<ArrayList<MovieDataEntry>> movieDataEntries;

    public MainViewModel(Application application) {
        super(application);
        ArrayList<MovieDataEntry> movieList = new ArrayList<>();
        movieDataEntries = new MutableLiveData();
        movieDataEntries.setValue(movieList);
        loadMovieData();
    }

    public MutableLiveData<ArrayList<MovieDataEntry>> getMovieDataEntries() {
        return movieDataEntries;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void Init(MovieListAdapter.MovieListAdapterOnClickHandler clickHandler) {
        //        if (movieListAdapter == null) {
        //            movieListAdapter = new MovieListAdapter(clickHandler);
        //        }


    }


    public void loadMovieData() {
        movieDataEntries.getValue().clear();
        new FetchMovieData().execute(command);
    }

    //    public MovieListAdapter getMovieListAdapter() {
    //        return movieListAdapter;
    //    }

    public boolean hasData() {
        return (movieDataEntries.getValue().size() > 0);
    }

    private class FetchMovieData extends AsyncTask<String, MovieDBPageResult, MovieDBPageResult>
    {
        @Override
        protected MovieDBPageResult doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            int totalPages = 5;
            int currentPageLoading = 1;
            MovieDBPageResult pageResult;
            do {
                URL movieDbUrl = NetworkUtils.buildUrl(params[0], Integer.toString(currentPageLoading));
                try {
                    String response = NetworkUtils.getResponseFromHttpUrl(movieDbUrl);
                    pageResult = MovieDBPageResult.parseMovieDbPageResult(response);
                    publishProgress(pageResult);
                    currentPageLoading++;
                }
                catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            } while (currentPageLoading <= totalPages);

            return null;
        }

        @Override
        protected void onProgressUpdate(MovieDBPageResult... values) {
            super.onProgressUpdate(values);

            if (values.length > 0) {
                ArrayList<MovieDataEntry> entries = movieDataEntries.getValue();
                entries.addAll(values[0].getResults());
                movieDataEntries.postValue(entries);
            }
        }
    }


}
