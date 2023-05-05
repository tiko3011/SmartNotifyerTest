package com.example.smartnotifyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LauncherActivity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.smartnotifyer.adapter.StatAdapter;
import com.example.smartnotifyer.model.Stat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String[] arrNames = {"youtube", "viber", "beeline", "instagram", "gallery3d", "ecommerence", "music", "smartnotifyer", "launcher", "sbrowser", "forest", "popupcalculator", "settings", "snapchat", "telegram"};
    String[] arrNamesChanged = {"Youtube", "Viber", "My Team", "Instagram", "Gallery", "E Commerence", "Music", "Smart Notifyer", "One UI Home", "Samsung Browser", "Forest", "Calculator", "Settings", "Snapchat", "Telegram"};

    RecyclerView statRecycler;
    StatAdapter statAdapter;

    long endTime = System.currentTimeMillis();
    long startTime = endTime - 14*60000*60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        List<Stat> stats = new ArrayList<>();

        if (usageStatsList != null && usageStatsList.size() > 0) {
            for (int i = 0; i < usageStatsList.size(); i++) {
                UsageStats usageStats = usageStatsList.get(i);

                if (usageStats.getTotalTimeInForeground() / 60000 > 0) {
                    String packageName = usageStats.getPackageName();
                    PackageManager packageManager = getPackageManager();
                    String appName = null;
                    Drawable icon = null;

                    try {
                        ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);

                        appName = packageManager.getApplicationLabel(appInfo).toString();
                        String totalTimeUsed = convertHour(usageStats.getTotalTimeInForeground());
                        icon = getPackageManager().getApplicationIcon(usageStats.getPackageName());

                        stats.add(new Stat(i, appName , totalTimeUsed, icon));
                        Log.i("statInfo", usageStats.getPackageName());
                    }
                    catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();

                        appName = changePackageName(packageName);
                        String totalTimeUsed = convertHour(usageStats.getTotalTimeInForeground());
                        icon = getResources().getDrawable(R.mipmap.ic_launcher);

                        stats.add(new Stat(i, appName, totalTimeUsed, icon));
                        Log.i("statInfo", usageStats.getPackageName());
                    }
                }
            }

        } else {
            statRecycler.setVisibility(View.GONE);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        setStatRecycler(stats);
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

    public String convertHour(long milliseconds){
        long minute = milliseconds / 60000;

        if (minute > 60){
            return String.valueOf(minute / 60) + " h " + String.valueOf(minute % 60) + " m";
        } else {
            return String.valueOf(minute + " m");
        }
    }
}