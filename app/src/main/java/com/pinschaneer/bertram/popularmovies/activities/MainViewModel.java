package com.pinschaneer.bertram.popularmovies.activities;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.pinschaneer.bertram.popularmovies.R;
import com.pinschaneer.bertram.popularmovies.data.FavoriteMovieDataBase;
import com.pinschaneer.bertram.popularmovies.data.MovieDBPageResult;
import com.pinschaneer.bertram.popularmovies.data.MovieDataEntry;
import com.pinschaneer.bertram.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class MainViewModel extends AndroidViewModel
{
    private String command = "movie/popular";

    private final MutableLiveData<ArrayList<MovieDataEntry>> webMovieDataEntries;

    public LiveData<List<MovieDataEntry>> getLocalMovieDataEntries() {
        return localMovieDataEntries;
    }

    private final LiveData<List<MovieDataEntry>> localMovieDataEntries;


    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<Boolean> hasData;
    private final MutableLiveData<Boolean> hasLoadingError;

    public MainViewModel(Application application) {
        super(application);
        ArrayList<MovieDataEntry> movieList = new ArrayList<>();
        webMovieDataEntries = new MutableLiveData<>();
        webMovieDataEntries.setValue(movieList);
        isLoading = new MutableLiveData<>();
        hasData = new MutableLiveData<>();
        hasLoadingError = new MutableLiveData<>();
        FavoriteMovieDataBase movieDataBase = FavoriteMovieDataBase.getInstance(application.getApplicationContext());

        localMovieDataEntries = movieDataBase.movieDataDao().getLiveDataAllMovieData();
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

    public MutableLiveData<ArrayList<MovieDataEntry>> getWebMovieDataEntries() {
        return webMovieDataEntries;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void loadMovieData() {
        if (!isCommandLocalDbCommand()) {
            webMovieDataEntries.getValue().clear();
            new FetchMovieData().execute(command);
            isLoading.postValue(true);
        }

    }

    public boolean isCommandLocalDbCommand() {
        String localDbCommand = getApplication().getResources().getString(R.string.local_database_command);
        return command.equals(localDbCommand);
    }

    @SuppressLint("StaticFieldLeak")
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
                ArrayList<MovieDataEntry> entries = webMovieDataEntries.getValue();
                entries.addAll(values[0].getResults());
                webMovieDataEntries.postValue(entries);
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
            webMovieDataEntries.getValue().clear();
            hasLoadingError.postValue(false);
            hasData.postValue(false);
            isLoading.postValue(true);

        }
    }


}
