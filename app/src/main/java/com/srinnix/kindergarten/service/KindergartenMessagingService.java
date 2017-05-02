package com.srinnix.kindergarten.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.util.ArrayMap;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.srinnix.kindergarten.R;

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

        String idPost = arrayMap.get("_id");
        String content = arrayMap.get("content");
        String image = null;
        if (arrayMap.containsKey("image")) {
            image = arrayMap.get("image");
        }

        //small remote view
        RemoteViews smallView = new RemoteViews(getPackageName(), R.layout.view_notification);
        smallView.setTextColor(R.id.textview_header, Color.BLACK);
        smallView.setTextColor(R.id.textview_content, Color.parseColor("#80000000"));

        //big remote view
        RemoteViews bigView = new RemoteViews(getPackageName(), R.layout.view_big_notification);
        bigView.setTextColor(R.id.textview_header, Color.BLACK);
        bigView.setTextViewText(R.id.textview_content, content);
        bigView.setTextColor(R.id.textview_content, Color.parseColor("#80000000"));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_children_white);
        builder.setVibrate(new long[]{
                0, 200, 200, 600, 600
        });
        builder.mNotification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        builder.setContent(smallView);
        builder.setCustomBigContentView(bigView);

//        Intent intent = new Intent(this, MainActivity.class);
//        intent.setAction(AppConstant.ACTION_NOTIFICATION);
//        Bundle bundle = new Bundle();
//        bundle.putString(ApiConstant._ID_HOUSE, idHouse);
//        intent.putExtras(bundle);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        int time = (int) System.currentTimeMillis();
        if (image != null) {
            Handler mainHandler = new Handler(getMainLooper());

            String finalImage = image;
            Runnable myRunnable = () -> {
                NotificationTarget notificationTarget = new NotificationTarget(
                        this,
                        bigView,
                        R.id.imageview_image,
                        notification,
                        time);

                Glide.with(this)
                        .load(finalImage)
                        .asBitmap()
                        .into(notificationTarget);
            };
            mainHandler.post(myRunnable);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(time, notification);

    }
}
