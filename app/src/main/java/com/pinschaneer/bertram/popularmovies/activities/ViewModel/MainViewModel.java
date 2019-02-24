package com.pinschaneer.bertram.popularmovies.activities.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.pinschaneer.bertram.popularmovies.data.MovieDBPageResult;
import com.pinschaneer.bertram.popularmovies.data.MovieDataEntry;
import com.pinschaneer.bertram.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainViewModel extends AndroidViewModel
{
    private String command = "movie/popular";

    private MutableLiveData<ArrayList<MovieDataEntry>> movieDataEntries;


    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<Boolean> hasData;
    private MutableLiveData<Boolean> hasLoadingError;

    public MainViewModel(Application application) {
        super(application);
        ArrayList<MovieDataEntry> movieList = new ArrayList<>();
        movieDataEntries = new MutableLiveData<>();
        movieDataEntries.setValue(movieList);
        isLoading = new MutableLiveData<>();
        hasData = new MutableLiveData<>();
        hasLoadingError = new MutableLiveData<>();

        loadMovieData();
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<Boolean> getHasData() {
        return hasData;
    }

    public MutableLiveData<Boolean> getHasLoadingError() {
        return hasLoadingError;
    }

    public MutableLiveData<ArrayList<MovieDataEntry>> getMovieDataEntries() {
        return movieDataEntries;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void loadMovieData() {
        movieDataEntries.getValue().clear();
        new FetchMovieData().execute(command);
        isLoading.postValue(true);
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
                    if (!hasData.getValue()) {
                        hasLoadingError.postValue(true);
                    }
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
                hasData.postValue(true);
            }
        }

        @Override
        protected void onPostExecute(MovieDBPageResult movieDBPageResult) {
            super.onPostExecute(movieDBPageResult);
            isLoading.postValue(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            movieDataEntries.getValue().clear();
            hasLoadingError.postValue(false);
            hasData.postValue(false);
            isLoading.postValue(true);

        }
    }


}
