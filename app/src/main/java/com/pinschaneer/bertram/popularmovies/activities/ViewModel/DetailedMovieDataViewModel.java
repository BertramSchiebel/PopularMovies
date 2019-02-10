package com.pinschaneer.bertram.popularmovies.activities.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.pinschaneer.bertram.popularmovies.data.MovieDetailData;
import com.pinschaneer.bertram.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class DetailedMovieDataViewModel extends AndroidViewModel
{
    private String movieId;
    private boolean isLoadingActive;
    private boolean isLoadingSuccessfull;
    private MutableLiveData<MovieDetailData> movieData;

    public DetailedMovieDataViewModel(Application application) {

        super(application);
        movieId = "";
    }

    public boolean isLoadingActive() {
        return isLoadingActive;
    }

    public boolean isLoadingSuccessfull() {
        return isLoadingSuccessfull;
    }

    public MutableLiveData<MovieDetailData> getMovieData() {
        return movieData;
    }

    public boolean hasData() {
        return !movieId.isEmpty() && isLoadingSuccessfull;
    }

    public void init(String movieId) {
        this.movieId = movieId;
        loadMovieDetails();
        movieData = new MutableLiveData<>();
    }

    /**
     * load the details of a movie by a network request
     */
    private void loadMovieDetails() {
        String command = "movie/" + movieId;
        new FetchMovieDetailData().execute(command);
    }


    class FetchMovieDetailData extends AsyncTask<String, Void, String>
    {

        /**
         * Settings before executing the async task
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoadingActive = true;
            isLoadingSuccessfull = true;
        }

        /**
         * runs an async task to get data from the internet
         *
         * @param params the parameter to build the URL for the network request
         * @return A JSON string of the network request or null if the request fails
         */
        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            String response;
            URL movieDbUrl = NetworkUtils.buildUrl(params[0], "1");
            try {
                response = NetworkUtils.getResponseFromHttpUrl(movieDbUrl);
            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        /**
         * after the task is finished the received data will be processed
         *
         * @param response the received data from the network request
         */
        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            MovieDetailData details = null;
            if (response != null) {
                details = MovieDetailData.crateMovieDetailData(response);
                if (details != null) {
                    isLoadingActive = false;
                    isLoadingSuccessfull = true;
                }
            }
            else {
                isLoadingSuccessfull = false;
            }
            movieData.postValue(details);
        }
    }
}
