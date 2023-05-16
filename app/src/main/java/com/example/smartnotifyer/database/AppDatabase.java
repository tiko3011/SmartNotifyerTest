package com.example.smartnotifyer.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Stat.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract StatDao statDao();
}