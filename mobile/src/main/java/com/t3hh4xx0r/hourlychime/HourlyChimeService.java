package com.t3hh4xx0r.hourlychime;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;

public class HourlyChimeService extends Service implements
        GoogleApiClient.ConnectionCallbacks {

    GoogleApiClient mGoogleApiClient;
    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    BroadcastReceiver tickWatcher = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DateTime now = new DateTime(Calendar.getInstance());

            if (now.getMinuteOfHour() == 0) {
                int[] quietHours = new SettingsProvider(context).getQuietHours();
                if (quietHours.length == 4) {
                    DateTime start = new DateTime().withHourOfDay(quietHours[0]).withMinuteOfHour(quietHours[1]);
                    DateTime end = new DateTime().withHourOfDay(quietHours[2]).withMinuteOfHour(quietHours[3]);
                    if (end.isBefore(start)) {
                        end = end.withFieldAdded(DurationFieldType.days(), 1);
                    }
                    if (now.getHourOfDay() <= end.getHourOfDay()) {
                        now = now.withFieldAdded(DurationFieldType.days(), 1);
                    }
                    boolean isInQuietHours = new Interval(start, end).contains(now);
                    Log.d("NOW###############################3", "" + fmt.print(now));
                    Log.d("START###############################3", "" + fmt.print(start));
                    Log.d("END###############################3", "" + fmt.print(end));

                    if (!isInQuietHours) {
                        Log.d("SENDING###############################3", "" + fmt.print(now));
                        mGoogleApiClient.connect();
                    }
                } else {
                    mGoogleApiClient.connect();
                }
            }
        }
    };

    public HourlyChimeService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();

        registerReceiver(tickWatcher, new IntentFilter(Intent.ACTION_TIME_TICK));

    }

    public void chime() {
        new SendMessageTask(mGoogleApiClient, this).execute();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(tickWatcher);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        chime();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
