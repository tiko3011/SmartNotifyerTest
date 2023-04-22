package com.example.smartnotifyer.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "stats")
public class Statistic implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo (name = "package")
    public String name;

    @ColumnInfo (name = "time")
    public long time;

    public Statistic(String name, long time) {
        this.name = name;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", time=" + time +
                '}';
    }
}
