package com.example.smartnotifyer.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    private AlarmHelper alarmHelper;
    public static int count = 0;
    public AlarmReceiver(){
        //Empty Constructor
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("ALMALM", "Received alarm, use intent to pass data");

        alarmHelper = new AlarmHelper();
        alarmHelper.setAlarmInNextMinute(context.getApplicationContext()); count++;

        if (count == 10){
            alarmHelper.stopAlarm();
        }
    }
}
