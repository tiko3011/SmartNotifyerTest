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

import com.example.smartnotifyer.database.Database;
import com.example.smartnotifyer.database.DatabaseClient;
import com.example.smartnotifyer.database.Statistic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the current time and Interval
        long endTime = System.currentTimeMillis();
        long startTime = endTime - 60 * 1000 * 60 * 24;

        // Get the app usage stats
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        // List View
        ListView listStats = findViewById(R.id.listStats);

        ArrayList<String> packageNames = new ArrayList<>();
        ArrayList<Long> packageTimes = new ArrayList<>();

        ListAdapter customAdapter = new ListAdapter(this, packageNames, packageTimes);

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
                    packageTimes.add(totalTime);
                }
            }
        } else {
            Log.i("statInfo", "No stats available");
        }

          // Add to database
//        MyThread threadStarter = new MyThread();
//        threadStarter.start();
    }

    class MyTestThread extends Thread{
        @Override
        public void run() {
            super.run();
            List<Statistic> stats = DatabaseClient.getInstance(getApplicationContext()).getDatabase().statisticDao().getAll();

            for (int i = 0; i < stats.size(); i++) {
                Log.i("TIGRAN", stats.get(i).toString());
            }
        }
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();

            DatabaseClient.getInstance(getApplicationContext()).getDatabase().statisticDao().deleteAll();

            long endTime = System.currentTimeMillis();
            long startTime = endTime - 60 * 1000 * 60 * 24;

            // Get the app usage stats
            UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
            List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

            ArrayList<String> packageNames = new ArrayList<>();
            ArrayList<Long> packageTimes = new ArrayList<>();

            // Display the app usage stats
            for (int i = 0; i < usageStatsList.size(); i++) {
                UsageStats usageStats = usageStatsList.get(i); // Access the element at index i

                if (usageStats.getTotalTimeInForeground() / 60000 != 0) {
                    String packageName = usageStats.getPackageName();
                    packageName = packageName.substring(packageName.indexOf(".") + 1);
                    long totalTime = usageStats.getTotalTimeInForeground() / 60000;

                    packageNames.add(packageName);
                    packageTimes.add(totalTime);
                }
            }

            // Insert in database
            for (int i = 0; i < packageNames.size(); i++) {
                DatabaseClient.getInstance(getApplicationContext())
                        .getDatabase().statisticDao()
                        .insertStatistic(new Statistic(packageNames.get(i), packageTimes.get(i)));
            }

            List<Statistic> stats = DatabaseClient.getInstance(getApplicationContext()).getDatabase().statisticDao().getAll();

            for (int i = 0; i < stats.size(); i++) {
                Log.i("TIGRAN", stats.get(i).toString());
            }
        }
    }

}