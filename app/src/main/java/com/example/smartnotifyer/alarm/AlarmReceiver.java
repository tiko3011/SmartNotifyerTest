package com.example.smartnotifyer.alarm;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.example.smartnotifyer.MainActivity;
import com.example.smartnotifyer.R;
import com.example.smartnotifyer.model.Stat;
import com.example.smartnotifyer.tools.ConvertStats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {

    List<Stat> statsAlarm = new ArrayList<>();

    Context context;
    private AlarmHelper alarmHelper;

    public AlarmReceiver(Context context){
        this.context = context;
    }

    public AlarmReceiver(){
        //Empty Constructor
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("ALMALM", "Received alarm, use intent to pass data");

        alarmHelper = new AlarmHelper();
        alarmHelper.setAlarmInNextMinute(context.getApplicationContext());

        ConvertStats tool = new ConvertStats(context.getApplicationContext());

        long endTimeAlarm = System.currentTimeMillis();
        long startTimeAlaram = endTimeAlarm - 60000;

        statsAlarm.clear();
        UsageStatsManager usageStatsManagerAlarm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsListAlarm = usageStatsManagerAlarm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTimeAlaram, endTimeAlarm);

        if (usageStatsListAlarm != null && usageStatsListAlarm.size() > 0) {
            for (int i = 0; i < usageStatsListAlarm.size(); i++) {
                UsageStats usageStats = usageStatsListAlarm.get(i);

                if (usageStats.getTotalTimeInForeground() / 60000 > 0 && usageStats.getLastTimeUsed() >= startTimeAlaram) {
                    String packageName = usageStats.getPackageName();
                    PackageManager packageManager = context.getPackageManager();
                    String appName = null;
                    Drawable icon = null;

                    try {
                        ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);

                        appName = packageManager.getApplicationLabel(appInfo).toString();
                        String totalTimeUsed = tool.convertMiliToString(usageStats.getTotalTimeInForeground());
                        icon = context.getPackageManager().getApplicationIcon(usageStats.getPackageName());

                        statsAlarm.add(new Stat(appName , totalTimeUsed, icon));
                        Log.i("statInfo", usageStats.getPackageName());
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();

                        appName = tool.changePackageName(packageName);
                        String totalTimeUsed = tool.convertMiliToString(usageStats.getTotalTimeInForeground());
                        icon = context.getResources().getDrawable(R.mipmap.ic_launcher);

                        statsAlarm.add(new Stat(appName, totalTimeUsed, icon));
                        tool.checkDuplicates(statsAlarm);
                        Collections.sort(statsAlarm);
                    }
                }
            }
        } else {
            Log.i("ALMALM", "Aint no stats");
        }


        for (int i = 0; i < statsAlarm.size(); i++) {
            Log.i("ALMALM", statsAlarm.get(i).toString());
        }

    }
}
