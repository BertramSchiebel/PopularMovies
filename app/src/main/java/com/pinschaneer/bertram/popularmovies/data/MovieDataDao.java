package com.pinschaneer.bertram.popularmovies.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDataDao
{
    @Query("SELECT * FROM movie")
    List<MovieDataEntry> getAllMovieData();

    @Query("SELECT * FROM movie")
    LiveData<List<MovieDataEntry>> getLiveDataAllMovieData();


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMovieData(MovieDataEntry movieDataEntry);

    @Delete
    void deleteMovieData(MovieDataEntry movieDataEntry);

}
