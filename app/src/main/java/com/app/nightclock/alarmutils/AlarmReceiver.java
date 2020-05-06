package com.app.nightclock.alarmutils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.app.nightclock.alarm.SettingsActivity;

public class AlarmReceiver extends BroadcastReceiver {
    String TAG = "AlarmReceiver";

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase("android.intent.action.BOOT_COMPLETED")) {
                Log.d(this.TAG, "onReceive: BOOT_COMPLETED");
                LocalData localData = new LocalData(context);
                //NotificationScheduler.setReminder(context, AlarmReceiver.class, localData.get_hour(), localData.get_min());
                return;
            }
        }
        Log.d(this.TAG, "onReceive: ");
        //NotificationScheduler.showNotification(context, SettingsActivity.class, "Remind Me", "Watch them now?");
    }
}
