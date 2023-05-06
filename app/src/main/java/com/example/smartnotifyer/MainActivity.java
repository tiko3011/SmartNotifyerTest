package com.example.smartnotifyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.smartnotifyer.adapter.StatAdapter;
import com.example.smartnotifyer.model.Stat;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    String[] arrNames = {"youtube", "viber", "beeline", "instagram", "gallery3d", "ecommerence", "music", "smartnotifyer", "launcher", "sbrowser", "forest", "popupcalculator", "settings", "snapchat", "telegram"};
    String[] arrNamesChanged = {"Youtube", "Viber", "My Team", "Instagram", "Gallery", "E Commerence", "Music", "Smart Notifyer", "One UI Home", "Samsung Browser", "Forest", "Calculator", "Settings", "Snapchat", "Telegram"};


    RecyclerView statRecycler;
    StatAdapter statAdapter;

    List<Stat> stats = new ArrayList<>();

    TextView tvInterval;
    SeekBar barSetInterval;
    int hour = 12;
    int minute = 0;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        tvInterval = findViewById(R.id.tv_interval);
        barSetInterval = findViewById(R.id.bar_set_interval);
        barSetInterval.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Called when the progress value changes
                tvInterval.setText(String.valueOf(barSetInterval.getProgress()));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Called when the user starts interacting with the SeekBar
            }
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Called when the user stops interacting with the SeekBar

                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                Thread getTime = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        LocalTime currentTime = LocalTime.now();

                        int hourThread = currentTime.getHour();
                        int minuteThread = currentTime.getMinute();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Run in MainThread

                                hour = hourThread;
                                minute = minuteThread;

                                Log.i("currentTime", "Current Time Real: " + String.valueOf(hour) + ":" + String.valueOf(minute));
                            }
                        });
                    }
                });
                getTime.start();
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                if (barSetInterval.getProgress() == 0){
                    hour = 1;
                } else {
                    hour = barSetInterval.getProgress();
                } tvInterval.setText(String.valueOf(hour));

                long endTimeUpdate = System.currentTimeMillis();
                long startTimeUpdate = endTimeUpdate - (long) hour  * 60000 * 60 + (long) minute * 60000;
                Log.i("currentTimeUpdate", "Current Time Set: " +String.valueOf(hour) + ":" + String.valueOf(minute));

                UsageStatsManager usageStatsManagerUpdate = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
                List<UsageStats> usageStatsListUpdate = usageStatsManagerUpdate.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTimeUpdate, endTimeUpdate);
                stats.clear();

                setStats(usageStatsListUpdate, stats, startTimeUpdate);
            //    checkDuplicates(stats);
                statAdapter.notifyDataSetChanged();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        long endTime = System.currentTimeMillis();
        long startTime = endTime - (long) hour * 60000 * 60 + (long) minute * 60000;
        Log.i("currentTime", String.valueOf(hour) + ":" + String.valueOf(minute));

        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        setStats(usageStatsList, stats, startTime);
//        checkDuplicates(stats);
        setStatRecycler(stats);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    }

    private void setStatRecycler(List<Stat> statList) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        statRecycler = findViewById(R.id.rv_usage_stats);
        statRecycler.setLayoutManager(layoutManager);

        statAdapter = new StatAdapter(this, statList);
        statRecycler.setAdapter(statAdapter);
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
    public String convertHourToString(long milliseconds){
        long minute = milliseconds / 60000;

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @SuppressLint("UseCompatLoadingForDrawables")
    public void setStats(List<UsageStats> usageStatsList, List<Stat> myStat, long startTimeUpdate){

        if (usageStatsList != null && usageStatsList.size() > 0) {
            for (int i = 0; i < usageStatsList.size(); i++) {
                UsageStats usageStats = usageStatsList.get(i);

                if (usageStats.getTotalTimeInForeground() / 60000 > 0 && usageStats.getLastTimeUsed() >= startTimeUpdate) {
                    String packageName = usageStats.getPackageName();
                    PackageManager packageManager = getPackageManager();
                    String appName = null;
                    Drawable icon = null;

                    try {
                        ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);

                        appName = packageManager.getApplicationLabel(appInfo).toString();
                        String totalTimeUsed = convertHourToString(usageStats.getTotalTimeInForeground());
                        icon = getPackageManager().getApplicationIcon(usageStats.getPackageName());

                        myStat.add(new Stat(i, appName , totalTimeUsed, icon));
                        Log.i("statInfo", usageStats.getPackageName());
                    }
                    catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();

                        appName = changePackageName(packageName);
                        String totalTimeUsed = convertHourToString(usageStats.getTotalTimeInForeground());
                        icon = getResources().getDrawable(R.mipmap.ic_launcher);

                        myStat.add(new Stat(i, appName, totalTimeUsed, icon));
                        Log.i("statInfo", usageStats.getPackageName());
                    }
                }
            }

        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void checkDuplicates(List<Stat> stats){
        for (int i = 0; i < stats.size(); i++) {
            String packageNameI = stats.get(i).getPackageName();
            long timeUsedI = convertStringToHour(stats.get(i).getTimeUsed());

            for (int j = 0; j < i; j++) {
                String packageNameJ = stats.get(j).getPackageName();
                long timeUsedJ = convertStringToHour(stats.get(j).getTimeUsed());

                if (packageNameI.equals(packageNameJ)){
//                    stats.get(i).setTimeUsed(convertHourToString(timeUsedI + timeUsedJ));
//                    stats.get(j).setTimeUsed(convertHourToString(timeUsedI + timeUsedJ));

                    //stats.remove(i);
                    Log.i("TIKO", "REMOVED STAT: " + packageNameJ);
                    //break;
                }
            }
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}