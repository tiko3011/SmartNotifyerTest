package com.example.smartnotifyer.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {

    private static final String DB_NAME = "stats_db";
    private static DatabaseClient instance;
    private final Database database;

    public DatabaseClient(Context context) {
        database = Room.databaseBuilder(context, Database.class, DB_NAME).build();
    }

    public static synchronized DatabaseClient getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseClient(context);
        }
        return instance;
    }

    public Database getDatabase(){
        return database;
    }


}
