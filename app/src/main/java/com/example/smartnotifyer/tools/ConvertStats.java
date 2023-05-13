package com.example.smartnotifyer.tools;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.usage.UsageStats;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.smartnotifyer.R;
import com.example.smartnotifyer.model.Stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConvertStats {
    String[] arrNames = {"youtube", "viber", "beeline", "instagram", "gallery3d", "ecommerence", "music", "smartnotifyer", "launcher", "sbrowser", "forest", "popupcalculator", "settings", "snapchat", "telegram", "pinterest", "musically", "maps", "bazarblot", "roommvvm", "alarmik"};
    String[] arrNamesChanged = {"Youtube", "Viber", "My Team", "Instagram", "Gallery", "E Commerence", "Music", "Smart Notifyer", "One UI Home", "Samsung Browser", "Forest", "Calculator", "Settings", "Snapchat", "Telegram", "Pinterest", "TikTok", "Map", "Blot",  "MVVM Example", "Alarmik"};

    Context context;
    public ConvertStats(Context context){
        this.context = context;
    }

    public ConvertStats(){
        // Empty Constructor
    }

    public void checkDuplicates(List<Stat> stats){
        for (int i = 0; i < stats.size(); i++) {
            String packageNameI = stats.get(i).getPackageName();

            for (int j = 0; j < i; j++) {
                String packageNameJ = stats.get(j).getPackageName();

                if (packageNameI.equals(packageNameJ)){
                    String totalTime = convertMinuteToString(((Long) convertStringToHour(stats.get(j).getTimeUsed()))  + (Long) convertStringToHour(stats.get(i).getTimeUsed()));
                    stats.get(i).setTimeUsed(totalTime);
                    stats.remove(stats.get(j));
                    break;
                }
            }
        }

    }

    public String changePackageName(String str){

        str += ".";
        List<String> substrings = new ArrayList<>();

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '.') {
                for (int j = i + 1; j < str.length(); j++) {
                    if (str.charAt(j) == '.') {
                        String substring = str.substring(i + 1, j);
                        substrings.add(substring);
                        i = j - 1; // Skip to the next dot
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < substrings.size(); i++) {
            for (int j = 0; j < arrNames.length; j++) {

                if (substrings.get(i).equals(arrNames[j])){
                    return arrNamesChanged[j];
                }

            }
        }

        return str;
    }
    public String convertMiliToString(long milliseconds){
        long minute = milliseconds / 60000;

        if (minute > 60){
            return String.valueOf(minute / 60) + " h " + String.valueOf(minute % 60) + " m";
        } else {
            return String.valueOf(minute + " m");
        }
    }

    public String convertMinuteToString(long minute){
        if (minute > 60){
            return String.valueOf(minute / 60) + " h " + String.valueOf(minute % 60) + " m";
        } else {
            return String.valueOf(minute + " m");
        }
    }
    public long convertStringToHour(String timeString){
        if (timeString.contains("h")) {
            // Format: Y h Z m
            String[] parts = timeString.split(" ");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[2]);
            return hours * 60 + minutes;
        } else {
            // Format: X m
            String minutesString = timeString.split(" ")[0];
            return Long.parseLong(minutesString);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setStats(List<UsageStats> usageStatsList, List<Stat> myStat, long startTimeUpdate){

        if (usageStatsList != null && usageStatsList.size() > 0) {
            for (int i = 0; i < usageStatsList.size(); i++) {
                UsageStats usageStats = usageStatsList.get(i);

                if (usageStats.getTotalTimeInForeground() / 60000 > 0 && usageStats.getLastTimeUsed() >= startTimeUpdate) {
                    String packageName = usageStats.getPackageName();
                    PackageManager packageManager = context.getPackageManager();
                    String appName = null;
                    Drawable icon = null;

                    try {
                        ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);

                        appName = packageManager.getApplicationLabel(appInfo).toString();
                        String totalTimeUsed = convertMiliToString(usageStats.getTotalTimeInForeground());
                        icon = context.getPackageManager().getApplicationIcon(usageStats.getPackageName());

                        myStat.add(new Stat(appName , totalTimeUsed, icon));
                        Log.i("statInfo", usageStats.getPackageName());
                    }
                    catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();

                        appName = changePackageName(packageName);
                        String totalTimeUsed = convertMiliToString(usageStats.getTotalTimeInForeground());
                        icon = context.getResources().getDrawable(R.mipmap.ic_launcher);

                        myStat.add(new Stat(appName, totalTimeUsed, icon));
                        checkDuplicates(myStat);
                        Collections.sort(myStat);
                    }
                }
            }

        }

    }

    public void someTest(){
        Log.i("ALMALM" , "SOMETEST");
    }

}
