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
    private String id;
    private String key;
    private String name;
    private String site;
    private String type;

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

    public boolean isYouTubeVideo() {
        return getSite().equals("YouTube");
    }

    public Uri getYouTubeUri() {
        Uri uri = Uri.parse("vnd.youtube://" + getKey());
        return uri;
    }
}
