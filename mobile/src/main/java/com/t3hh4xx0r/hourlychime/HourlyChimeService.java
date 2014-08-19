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

    BroadcastReceiver tickWatcher = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DateTime now = new DateTime(Calendar.getInstance());

            /**
             * Ensure the minute is 0, meaning the top of the hour has just been struck.
             */
            if (now.getMinuteOfHour() == 0) {
                int[] quietHours = new SettingsProvider(context).getQuietHours();
                /**
                 * Ensure valid qiet hours are set.
                 */
                if (quietHours.length == 4) {
                    /**
                     *
                     * If quiet hours end is before start, the the end is actually the "tomorrow" of
                     * the start time, so add a day
                     *
                     */
                    DateTime start = new DateTime().withHourOfDay(quietHours[0]).withMinuteOfHour(quietHours[1]);
                    DateTime end = new DateTime().withHourOfDay(quietHours[2]).withMinuteOfHour(quietHours[3]);
                    if (end.isBefore(start)) {
                        end = end.withFieldAdded(DurationFieldType.days(), 1);
                    }
                    if (now.getHourOfDay() <= end.getHourOfDay()) {
                        now = now.withFieldAdded(DurationFieldType.days(), 1);
                    }
                    boolean isInQuietHours = new Interval(start, end).contains(now);
                    if (!isInQuietHours) {
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

        /**
         * Prepare our listener for the {@link Intent.ACTION_TIME_TICK) event. This will trigger
         * the app at the change of every minute.
         */
        registerReceiver(tickWatcher, new IntentFilter(Intent.ACTION_TIME_TICK));

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

    /**
     * Once the connection has been established, start the chime event.
     *
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        new ChimeTask(mGoogleApiClient, this).execute();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
