package com.example.smartnotifyer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LauncherActivity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the current time and Interval
        long endTime = System.currentTimeMillis();
        long startTime = endTime - 60 * 1000 * 60 * 17;

        // Get the app usage stats
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        // List View
        ListView listStats = findViewById(R.id.listStats);

        ArrayList<String> packageNames = new ArrayList<>();
        ArrayList<Long> totalTimes = new ArrayList<>();

        ListAdapter customAdapter = new ListAdapter(this, packageNames, totalTimes);

        listStats.setAdapter(customAdapter);

        // Display the app usage stats
        if (usageStatsList != null && !usageStatsList.isEmpty()) {
            for (int i = 0; i < usageStatsList.size(); i++) {
                UsageStats usageStats = usageStatsList.get(i); // Access the element at index i

                if (usageStats.getTotalTimeInForeground() / 60000 != 0) {
                    String packageName = usageStats.getPackageName();
                    packageName = packageName.substring(packageName.indexOf(".") + 1);
                    long totalTime = usageStats.getTotalTimeInForeground() / 60000;

                    packageNames.add(packageName);
                    totalTimes.add(totalTime);
                }
            }
        } else {
            Log.i("NAH", "No stats available");
        }

    }
}