package com.example.smartnotifyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LauncherActivity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.smartnotifyer.adapter.StatAdapter;
import com.example.smartnotifyer.model.Stat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String[] arr = new String[]{
            "youtube",
            "viber",
            "beeline",
            "instagram",
            "gallery3d",
            "ecommerence",
            "music",
            "smartnotifyer",
            "launcher",
            "sbrowser",
            "forest",
            "popupcalculator",
            "settings",
            "snapchat",
            "telegram"
    };


    private TextView usageStatsTextView;

    RecyclerView statRecycler;
    StatAdapter statAdapter;

    // Current time and interval
    long endTime = System.currentTimeMillis();
    long startTime = endTime - 14*60000*60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Stat> stats = new ArrayList<>();

        // App usage stats list
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        // Put List in TextView
        if (usageStatsList != null && usageStatsList.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < usageStatsList.size(); i++) {
                UsageStats usageStats = usageStatsList.get(i);
                if (usageStats.getTotalTimeInForeground() / 60000 > 0) {
                    stats.add(new Stat(i, changePackageName(usageStats.getPackageName()) , String.valueOf(usageStats.getTotalTimeInForeground() / 60000)));
                    Log.i("statInfo", usageStats.getPackageName());
                }
            }

        } else {

        }


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
            for (int j = 0; j < arr.length; j++) {

                if (substrings.get(i).equals(arr[j])){
                    return arr[j];
                }

            }
        }

        return str;
    }

}