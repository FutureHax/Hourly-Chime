package com.t3hh4xx0r.hourlychime;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by r2doesinc on 6/30/14.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    /**
     * A {@link android.support.v4.content.WakefulBroadcastReceiver} to manage the wakelock for us.
     *
     * Broadcast recievers have a very limited amount of time to complete their tasks generally,
     * and will die without a wakelock.
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, HourlyChimeService.class);
        startWakefulService(context, service);
    }
}