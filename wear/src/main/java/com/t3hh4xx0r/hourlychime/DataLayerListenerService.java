package com.t3hh4xx0r.hourlychime;

import android.content.Context;
import android.media.AudioManager;
import android.os.Vibrator;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.Arrays;
import java.util.List;

public class DataLayerListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        Log.d("VOLUME - STREAM_SYSTEM", Integer.toString(audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM)));
        Log.d("VOLUME - STREAM_NOTIFICATION", Integer.toString(audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION)));
        Log.d("VOLUME - STREAM_RING", Integer.toString(audioManager.getStreamVolume(AudioManager.STREAM_RING)));

        String patternString = messageEvent.getPath();
        if (patternString.contains("[")) {
            List<String> list = Arrays.asList(patternString.substring(1, patternString.length() - 1).split(", "));
            long[] res = new long[list.size()];
            for (int i = 0; i < list.size(); i++) {
                res[i] = Long.parseLong(list.get(i));
            }
            vibe.vibrate(res, -1);
        } else {
            vibe.vibrate(Long.parseLong(patternString));
        }

    }


}