package com.example.smartnotifyer.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StatisticDao {

    @Query("SELECT * FROM stats")
    List<Statistic> getAll();

    @Insert
    long insertStatistic(Statistic statistic);

    @Query("DELETE FROM stats")
    void deleteAll();
}
