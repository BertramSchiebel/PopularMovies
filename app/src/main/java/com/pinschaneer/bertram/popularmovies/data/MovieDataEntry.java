package com.pinschaneer.bertram.popularmovies.data;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Class to hold a data entry of the movie database.
 */
@Entity(tableName = "movie")
public class MovieDataEntry
{
    private static final String TAG = MovieDataEntry.class.getSimpleName();

    @PrimaryKey
    private int id;
    private String title;
    private String posterPath;
    private String description;
    private double averageVote;
    private Date releaseDate;

    public MovieDataEntry(int id, String title, String posterPath, String description,
                          double averageVote, Date releaseDate) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.description = description;
        this.averageVote = averageVote;
        this.releaseDate = releaseDate;
    }

    public MovieDataEntry(String jsonData) {
        final String MDB_ID = "id";
        final String MDB_TITLE = "title";
        final String MDB_OVERVIEW = "overview";
        final String MDB_RELEASE_DATE = "release_date";
        final String MDB_POSTER_PATH = "poster_path";
        final String MDB_VOTE_AVERAGE = "vote_average";
        try {
            JSONObject movieDataJSON = new JSONObject(jsonData);
            if (movieDataJSON.has(MDB_ID)) {
                setId(movieDataJSON.getInt(MDB_ID));
            }
            else {
                Log.e(TAG, "Id is not available");
                throw new JSONException("Id for movie is not available!");
            }

            if (movieDataJSON.has(MDB_TITLE)) {
                setTitle(movieDataJSON.getString(MDB_TITLE));
            }

            if (movieDataJSON.has(MDB_OVERVIEW)) {
                setDescription(movieDataJSON.getString(MDB_OVERVIEW));
            }

            if (movieDataJSON.has(MDB_POSTER_PATH)) {
                setPosterPath(movieDataJSON.getString(MDB_POSTER_PATH));
            }

            if (movieDataJSON.has(MDB_RELEASE_DATE)) {
                String dateString = movieDataJSON.getString(MDB_RELEASE_DATE);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date releaseDate;
                try {
                    releaseDate = format.parse(dateString);
                }
                catch (ParseException e) {
                    e.printStackTrace();
                    releaseDate = null;
                }
                setReleaseDate(releaseDate);
            }
            if (movieDataJSON.has(MDB_VOTE_AVERAGE)) {
                setAverageVote(movieDataJSON.getDouble(MDB_VOTE_AVERAGE));
            }

        }
        catch (JSONException e) {
            Log.e(TAG, "Input is not a valid JSON string");
            this.id = -1;
        }


    }

    /**
     * Gets the complete path to the movie poster
     *
     * @return a URL string to the complete path
     */
    public String getPosterImageUrl() {
        return "https://image.tmdb.org/t/p/w500" + posterPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAverageVote() {
        return averageVote;
    }

    public void setAverageVote(double averageVote) {
        this.averageVote = averageVote;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}
