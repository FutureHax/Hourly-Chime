package com.t3hh4xx0r.hourlychime;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by r2doesinc on 6/30/14.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, HourlyChimeService.class);

        // Start the service, keeping the device awake while it is launching.
        Log.d("SimpleWakefulReceiver", "Starting service @ " + SystemClock.elapsedRealtime());
        startWakefulService(context, service);
    }
}