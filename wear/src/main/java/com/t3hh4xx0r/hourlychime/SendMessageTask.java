package com.t3hh4xx0r.hourlychime;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.Arrays;

/**
 * Created by r2doesinc on 6/30/14.
 */
public class SendMessageTask extends AsyncTask<Void, Void, Void> {
    GoogleApiClient mGoogleApiClient;
    Context ctx;

    public SendMessageTask(GoogleApiClient mGoogleApiClient, Context ctx) {
        this.mGoogleApiClient = mGoogleApiClient;
        this.ctx = ctx;
    }

    @Override
    protected Void doInBackground(Void... params) {
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();

        for (Node node : nodes.getNodes()) {
                Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(),
                        "open", null);
        }
        return null;
    }
}
