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

    public MovieDBPageResult(String jsonDataString
    ) {

        mResults = new ArrayList<>();
        try {

            JSONObject listSearchResult = new JSONObject(jsonDataString);

            if (listSearchResult.has(MDB_PAGE)) {
                setPageNo(listSearchResult.getInt(MDB_PAGE));
            }


            if (listSearchResult.has(MDB_RESULTS)) {
                JSONArray resultList = listSearchResult.getJSONArray(MDB_RESULTS);
                for (int i = 0; i < resultList.length(); i++) {
                    JSONObject entryJson = resultList.getJSONObject(i);
                    MovieResultData movieResultEntry = new MovieResultData(entryJson.toString());
                    mResults.add(movieResultEntry);
                }

            } else {
                Log.e(TAG, "input sting has wrong JSON Form");
            }

            if (listSearchResult.has(MDB_TOTAL_PAGES)) {
                setTotalPages(listSearchResult.getInt(MDB_TOTAL_PAGES));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
