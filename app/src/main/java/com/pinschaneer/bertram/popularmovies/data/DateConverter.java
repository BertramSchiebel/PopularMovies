package com.pinschaneer.bertram.popularmovies.data;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class DateConverter
{
    @TypeConverter
    public static Date toDate(Long timestamp) {

        return (null == timestamp) ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}
