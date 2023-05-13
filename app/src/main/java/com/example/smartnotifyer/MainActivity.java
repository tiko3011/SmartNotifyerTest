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
import com.example.smartnotifyer.tools.ConvertStats;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    RecyclerView statRecycler;
    StatAdapter statAdapter;

    List<Stat> stats = new ArrayList<>();

    ConvertStats tool = new ConvertStats();
    TextView tvInterval;
    SeekBar barSetInterval;
    int hour = 12;
    int minute = 0;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                LocalTime currentTime = LocalTime.now();
                hour = currentTime.getHour();
                minute = currentTime.getMinute();
                ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                hour = (barSetInterval.getProgress() == 0) ? 1 : barSetInterval.getProgress();

                long endTimeUpdate = System.currentTimeMillis();
                long startTimeUpdate = endTimeUpdate - (long) hour  * 60000 * 60 + (long) minute * 60000;

                UsageStatsManager usageStatsManagerUpdate = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
                List<UsageStats> usageStatsListUpdate = usageStatsManagerUpdate.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTimeUpdate, endTimeUpdate);
                stats.clear();

                setStats(usageStatsListUpdate, stats, startTimeUpdate);
                tool.checkDuplicates(stats);
                statAdapter.notifyDataSetChanged();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        long endTime = System.currentTimeMillis();
        long startTime = endTime - (long) hour * 60000 * 60 + (long) minute * 60000;

        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        setStats(usageStatsList, stats, startTime);
        tool.checkDuplicates(stats);
        setStatRecycler(stats);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    private void setStatRecycler(List<Stat> statList) {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        statRecycler = findViewById(R.id.rv_usage_stats);
        statRecycler.setLayoutManager(layoutManager);

        statAdapter = new StatAdapter(this, statList);
        statRecycler.setAdapter(statAdapter);
    }

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
                        String totalTimeUsed = tool.convertMiliToString(usageStats.getTotalTimeInForeground());
                        icon = getPackageManager().getApplicationIcon(usageStats.getPackageName());

                        myStat.add(new Stat(appName , totalTimeUsed, icon));
                        Log.i("statInfo", usageStats.getPackageName());
                    }
                    catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();

                        appName = tool.changePackageName(packageName);
                        String totalTimeUsed = tool.convertMiliToString(usageStats.getTotalTimeInForeground());
                        icon = getResources().getDrawable(R.mipmap.ic_launcher);

                        myStat.add(new Stat(appName, totalTimeUsed, icon));
                        tool.checkDuplicates(stats);
                        Collections.sort(stats);
                    }
                }
            }

        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}