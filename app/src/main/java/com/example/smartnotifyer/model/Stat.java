package com.example.smartnotifyer.model;

import android.graphics.drawable.Drawable;

import com.example.smartnotifyer.MainActivity;
import com.example.smartnotifyer.tools.ConvertStats;

public class Stat implements Comparable<Stat>{

    int id;
    String packageName;
    String timeUsed;
    Drawable packageIcon;

    public Stat(String packageName, String timeUsed, Drawable packageIcon) {
        this.packageName = packageName;
        this.timeUsed = timeUsed;
        this.packageIcon = packageIcon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(String timeUsed) {
        this.timeUsed = timeUsed;
    }

    public Drawable getPackageIcon() {
        return packageIcon;
    }

    public void setPackageIcon(Drawable packageIcon) {
        this.packageIcon = packageIcon;
    }

    @Override
    public int compareTo(Stat o) {
        ConvertStats tool = new ConvertStats();
        return Long.compare(tool.convertStringToHour(o.timeUsed) , tool.convertStringToHour(this.timeUsed)) ;
    }
}
