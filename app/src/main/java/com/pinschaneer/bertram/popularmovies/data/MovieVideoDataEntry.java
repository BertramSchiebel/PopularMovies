package com.pinschaneer.bertram.popularmovies.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieVideoDataEntry
{
    private static final String MDB_ID = "id";
    private static final String MDB_KEY = "key";
    private static final String MDB_NAME = "name";
    private static final String MDB_SITE = "site";
    private static final String MDB_TYPE = "type";
    private String id;
    private String key;
    private String name;
    private String site;
    private String type;

    public static MovieVideoDataEntry crateMovieDetailVideoData(JSONObject jsonData) {
        MovieVideoDataEntry videoData = new MovieVideoDataEntry();
        try {
            if (jsonData.has(MDB_ID)) {
                videoData.setId(jsonData.getString(MDB_ID));
            }
            else {
                return null;
            }
            if (jsonData.has(MDB_KEY)) {
                videoData.setKey(jsonData.getString(MDB_KEY));
            }
            if (jsonData.has(MDB_NAME)) {
                videoData.setName(jsonData.getString(MDB_NAME));
            }
            if (jsonData.has(MDB_SITE)) {
                videoData.setSite(jsonData.getString(MDB_SITE));
            }
            if (jsonData.has(MDB_TYPE)) {
                videoData.setType(jsonData.getString(MDB_TYPE));
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return videoData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
