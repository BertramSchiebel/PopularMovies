package com.pinschaneer.bertram.popularmovies.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieDBPageResult {

    public static final String MDB_RESULTS = "results";
    public static final String MDB_PAGE = "page";
    public static final String MDB_TOTAL_PAGES = "total_pages";

    private static final String TAG = MovieDBPageResult.class.getSimpleName();

    private int mPageNo;
    private ArrayList<MovieResultData> mResults;
    private int mTotal_pages;

    public static MovieDBPageResult createMovieDBPageResult(String jsonDataString
    ) {
        MovieDBPageResult movieDBPageResult = new MovieDBPageResult();
        ArrayList<MovieResultData> resultsDatalist = new ArrayList<>();
        movieDBPageResult.setResults(resultsDatalist);
        try {

            JSONObject listSearchResult = new JSONObject(jsonDataString);

            if (listSearchResult.has(MDB_PAGE)) {
                movieDBPageResult.setPageNo(listSearchResult.getInt(MDB_PAGE));
            }


            if (listSearchResult.has(MDB_RESULTS)) {
                JSONArray resultList = listSearchResult.getJSONArray(MDB_RESULTS);
                for (int i = 0; i < resultList.length(); i++) {
                    JSONObject entryJson = resultList.getJSONObject(i);
                    MovieResultData movieResultEntry = MovieResultData.createMovieResultData(entryJson.toString());
                    if (null != movieResultEntry) {
                        resultsDatalist.add(movieResultEntry);
                    }
                }

            } else {
                Log.e(TAG, "input sting has wrong JSON Form");
            }

            if (listSearchResult.has(MDB_TOTAL_PAGES)) {
                movieDBPageResult.setTotalPages(listSearchResult.getInt(MDB_TOTAL_PAGES));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return movieDBPageResult;
    }

    public int getPageNo() {
        return mPageNo;
    }

    public void setPageNo(int mPageNo) {
        this.mPageNo = mPageNo;
    }

    public ArrayList<MovieResultData> getResults() {
        return mResults;
    }

    public void setResults(ArrayList<MovieResultData> mResults) {
        this.mResults = mResults;
    }

    public int getTotalPages() {
        return mTotal_pages;
    }

    public void setTotalPages(int mTotal_pages) {
        this.mTotal_pages = mTotal_pages;
    }
}
