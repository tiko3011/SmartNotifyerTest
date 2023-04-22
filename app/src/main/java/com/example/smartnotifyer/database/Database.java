package com.example.smartnotifyer.database;

import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Statistic.class}, version = 1)
public abstract class Database extends RoomDatabase {

    public abstract StatisticDao statisticDao();
}
