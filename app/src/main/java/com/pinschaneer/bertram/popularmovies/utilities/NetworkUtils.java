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

/**
 * These utilities will be used to  communicate with the themociedb.org server
 */
public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    final static String API_KEY_PARAM = "api_key";

    final static String LANGUAGE_PARAM = "language";

    final static String PAGE_PARAM = "page";


    /**
     * These Api key is your personal key in order to communicate you have to
     * receive your key from the website  https://www.themoviedb.org/
     */
    private static final String API_KEY = "";

    private static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3";

    /**
     * Builds a Url which can receive data from the themociedb.org server
     *
     * @param movieCommand the command of request
     * @param page         the page number if the request gives more then one resulat page
     * @return the URL for requesting the themociedb.org server
     */
    public static URL buildUrl(String movieCommand, String page) {
        String baseUrl = MOVIE_DB_BASE_URL + "/" + movieCommand;
        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, "en-US")
                .appendQueryParameter(PAGE_PARAM, page)
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


    /**
     * This method obens a Network connection to the given Url an returns the responese of this Url
     *
     * @param url The URL to get Data from
     * @return the response of the Url
     * @throws IOException Related to network and stream reading
     */
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
