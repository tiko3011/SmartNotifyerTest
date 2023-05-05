package com.example.smartnotifyer.model;

public class Stat {

    int id;
    String packageName;
    String timeUsed;

    public Stat(int id, String packageName, String timeUsed) {
        this.id = id;
        this.packageName = packageName;
        this.timeUsed = timeUsed;
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
}
