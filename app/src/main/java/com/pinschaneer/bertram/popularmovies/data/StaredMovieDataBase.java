package com.pinschaneer.bertram.popularmovies.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

@Database(entities = {MovieDataEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class StaredMovieDataBase extends RoomDatabase
{
    private static final String LOG_TAG = StaredMovieDataBase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "staredMovies";
    private static StaredMovieDataBase instance;


    public static StaredMovieDataBase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Create new Database");
                instance = Room.databaseBuilder(context.getApplicationContext(), StaredMovieDataBase.class, DATABASE_NAME).build();
            }
        }
        Log.d(LOG_TAG, "Get the Database");
        return instance;
    }

    public abstract MovieDataDao movieDataDao();

}
