package com.example.smartnotifyer.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Stats")
public class Stat {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "stat_id")
    public int statId;

    @NonNull
    @ColumnInfo(name = "stat_name")
    public String statName;

    @NonNull
    @ColumnInfo(name = "stat_time")
    public String statTime;


    public Stat(@NonNull String statName, String statTime) {
        this.statName = statName;
        this.statTime = statTime;
    }

    @NonNull
    public String getStatName() {
        return statName;
    }

    public void setStatName(@NonNull String statName) {
        this.statName = statName;
    }

    @NonNull
    public String getStatTime() {
        return statTime;
    }

    public void setStatTime(@NonNull String statTime) {
        this.statTime = statTime;
    }
}
