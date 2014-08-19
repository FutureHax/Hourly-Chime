package com.t3hh4xx0r.hourlychime;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Vibrator;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Arrays;

/**
 * Created by r2doesinc on 6/30/14.
 */
public class ChimeTask extends AsyncTask<Void, Void, Void> {
    GoogleApiClient mGoogleApiClient;
    Context ctx;

    public ChimeTask(GoogleApiClient mGoogleApiClient, Context ctx) {
        this.mGoogleApiClient = mGoogleApiClient;
        this.ctx = ctx;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
        SettingsProvider s = new SettingsProvider(ctx);

        if (s.getIsEnabledOnDevice()) {
            Vibrator vibe = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
            if (s.getSimpleVibrationPattern() != -1) {
                vibe.vibrate(s.getSimpleVibrationPattern());
            } else {
                vibe.vibrate(s.getCustomVibrationPattern(), -1);
            }
        }

        if (s.getIsEnabledOnWear()) {
            for (Node node : nodes.getNodes()) {
                if (s.getSimpleVibrationPattern() != -1) {
                    Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(),
                            Long.toString(s.getSimpleVibrationPattern()), null);
                } else {
                    Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(),
                            Arrays.toString(s.getCustomVibrationPattern()), null);
                }
            }
        }
        return null;
    }
}
