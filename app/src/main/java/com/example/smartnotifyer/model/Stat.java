package com.example.smartnotifyer.model;

import android.graphics.drawable.Drawable;

public class Stat {

    int id;
    String packageName;
    String timeUsed;
    Drawable packageIcon;

    public Stat(int id, String packageName, String timeUsed, Drawable packageIcon) {
        this.id = id;
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
}
