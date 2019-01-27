package com.pinschaneer.bertram.popularmovies.data;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Class to hold a data entry of the movie database.
 */
@Entity(tableName = "movie")
public class MovieDataEntry
{
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
