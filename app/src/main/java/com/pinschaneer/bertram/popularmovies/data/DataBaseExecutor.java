package com.pinschaneer.bertram.popularmovies.data;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DataBaseExecutor
{
    private static final Object LOCK = new Object();
    private static DataBaseExecutor instance;
    private final Executor diskIO;

    private DataBaseExecutor(Executor diskIO) {
        this.diskIO = diskIO;
    }

    public static DataBaseExecutor getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new DataBaseExecutor(Executors.newSingleThreadExecutor());
            }
        }
        return instance;
    }

    public Executor diskIO() {
        return diskIO;
    }

}