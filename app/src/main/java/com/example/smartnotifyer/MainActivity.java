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
import android.widget.Toast;

import com.example.smartnotifyer.adapter.StatAdapter;
import com.example.smartnotifyer.alarm.AlarmHelper;
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

    private AlarmHelper alarmHelper;

    Context context;
    ConvertStats tool;

    TextView tvInterval;
    SeekBar barSetInterval;
    int hour = 12;
    int minute = 0;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        tool = new ConvertStats(context);

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
                hour = (barSetInterval.getProgress() == 0) ? 1 : barSetInterval.getProgress();

                long endTimeUpdate = System.currentTimeMillis();
                long startTimeUpdate = endTimeUpdate - (long) hour  * 60000 * 60 + (long) minute * 60000;

                UsageStatsManager usageStatsManagerUpdate = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
                List<UsageStats> usageStatsListUpdate = usageStatsManagerUpdate.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTimeUpdate, endTimeUpdate);
                stats.clear();

                tool.setStats(usageStatsListUpdate, stats, startTimeUpdate);
                statAdapter.notifyDataSetChanged();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        long endTime = System.currentTimeMillis();
        long startTime = endTime - (long) hour * 60000 * 60 + (long) minute * 60000;

        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        tool.setStats(usageStatsList, stats, startTime);
        setStatRecycler(stats);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //Set alarm, close app and expect wake up in 1 minute
        alarmHelper = new AlarmHelper();
        alarmHelper.setAlarmInNextMinute(this);
        Toast.makeText(this, "Wake Up In Background", Toast.LENGTH_SHORT).show();
//        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    private void setStatRecycler(List<Stat> statList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        statRecycler = findViewById(R.id.rv_usage_stats);
        statRecycler.setLayoutManager(layoutManager);

        statAdapter = new StatAdapter(this, statList);
        statRecycler.setAdapter(statAdapter);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}