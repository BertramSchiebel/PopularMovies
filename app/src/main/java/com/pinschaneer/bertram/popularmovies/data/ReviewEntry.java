package com.pinschaneer.bertram.popularmovies.data;

import org.json.JSONException;
import org.json.JSONObject;

public class ReviewEntry
{
    private static final String MDB_ID = "id";
    private static final String MDB_AUTHOR = "author";
    private static final String MDB_CONTENT = "content";
    private String id;
    private String author;
    private String content;

    public static ReviewEntry crateReviewData(JSONObject jsonData) {
        ReviewEntry review = new ReviewEntry();
        try {
            if (jsonData.has(MDB_ID)) {
                review.setId(jsonData.getString(MDB_ID));
            }
            else {
                return null;
            }
            if (jsonData.has(MDB_AUTHOR)) {
                review.setAuthor(jsonData.getString(MDB_AUTHOR));
            }
            if (jsonData.has(MDB_CONTENT)) {
                review.setContent(jsonData.getString(MDB_CONTENT));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


        return review;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

