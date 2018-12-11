package com.pinschaneer.bertram.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    final static String API_KEY_PARAM = "api_key";
    final static String LANGUAGE_PARAM = "language";
    final static String PAGE_PARAM = "page";
    // TODO Add API Key
    private static final String API_KEY = "";
    private static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie";

    public static URL buildUrl(String movieCommand) {
        String baseUrl = MOVIE_DB_BASE_URL + "/" + movieCommand;
        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, "en-US")
                .appendQueryParameter(PAGE_PARAM, "1")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;

    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {


        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } catch (EOFException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return null;
    }
}
