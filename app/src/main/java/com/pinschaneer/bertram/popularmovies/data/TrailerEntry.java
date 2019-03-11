package com.pinschaneer.bertram.popularmovies.data;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

public class TrailerEntry
{
    private static final String MDB_ID = "id";
    private static final String MDB_KEY = "key";
    private static final String MDB_NAME = "name";
    private static final String MDB_SITE = "site";
    private static final String MDB_TYPE = "type";
    private String key;
    private String name;
    private String site;

    public static TrailerEntry crateTrailerData(JSONObject jsonData) {
        TrailerEntry trailer = new TrailerEntry();
        try {
            if (jsonData.has(MDB_ID)) {
                trailer.setId(jsonData.getString(MDB_ID));
            }
            else {
                return null;
            }
            if (jsonData.has(MDB_KEY)) {
                trailer.setKey(jsonData.getString(MDB_KEY));
            }
            if (jsonData.has(MDB_NAME)) {
                trailer.setName(jsonData.getString(MDB_NAME));
            }
            if (jsonData.has(MDB_SITE)) {
                trailer.setSite(jsonData.getString(MDB_SITE));
            }
            if (jsonData.has(MDB_TYPE)) {
                trailer.setType(jsonData.getString(MDB_TYPE));
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return trailer;
    }

    private void setId(String id) {
        String id1 = id;
    }

    private String getKey() {
        return key;
    }

    private void setKey(String key) {
        this.key = key;
    }

    String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    private String getSite() {
        return site;
    }

    private void setSite(String site) {
        this.site = site;
    }

    private void setType(String type) {
        String type1 = type;
    }

    public boolean isYouTubeVideo() {
        return getSite().equals("YouTube");
    }

    public Uri getYouTubeUri() {
        return Uri.parse("vnd.youtube://" + getKey());
    }
}
