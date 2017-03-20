package com.srinnix.kindergarten.service;

import android.support.v4.util.ArrayMap;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by anhtu on 3/4/2017.
 */

public class KindergartenMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        handleMessage(remoteMessage);
    }

    private void handleMessage(RemoteMessage remoteMessage) {
        ArrayMap<String, String> arrayMap = (ArrayMap<String, String>) remoteMessage.getData();


    }
}
