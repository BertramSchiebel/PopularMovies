package com.pinschaneer.bertram.popularmovies.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDataDao
{
    @Query("SELECT * FROM movie")
    List<MovieDataEntry> getAllMovieData();

    @Insert
    void insertMovieData(MovieDataEntry movieDataEntry);

    @Delete
    void deleteMovieData(MovieDataEntry movieDataEntry);
}
