package com.srinnix.kindergarten.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.bulletinboard.activity.DetailActivity;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.model.Message;
import com.srinnix.kindergarten.util.SharedPreUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * Created by anhtu on 3/4/2017.
 */

public class KindergartenMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (!SharedPreUtils.getInstance(this).isUserSignedIn()) {
            return;
        }

        Map<String, String> data = remoteMessage.getData();
        try {
            if (data.containsKey("post")) {
                handleNotificationPost(data.get("post"));
            } else if (data.containsKey("message")) {
                handleMessageChat(data.get("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void handleNotificationPost(String data) throws JSONException {
        if (!SharedPreUtils.getInstance(this).getFlagReceiveNotification()) {
            return;
        }

        JSONObject jsonObject = new JSONObject(data);

        String idPost = jsonObject.getString("_id");
        String content = jsonObject.getString("content");
        String image = null;
        if (jsonObject.has("image")) {
            image = jsonObject.getString("image");
        }

        Intent intent = new Intent(this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_ID, idPost);
        intent.putExtras(bundle);

        NotificationCompat.Builder builder = getNotificationBuilder(intent, getString(R.string.important_post));

        //big remote view
        RemoteViews bigView = new RemoteViews(getPackageName(), R.layout.view_big_notification);
        bigView.setTextColor(R.id.textview_header, Color.BLACK);
        bigView.setTextViewText(R.id.textview_content, content);
        bigView.setTextColor(R.id.textview_content, Color.parseColor("#80000000"));
        builder.setCustomBigContentView(bigView);

        Notification notification = builder.build();

        if (image != null) {
            Handler mainHandler = new Handler(getMainLooper());

            String finalImage = image;
            Runnable myRunnable = () -> {
                NotificationTarget notificationTarget = new NotificationTarget(
                        this,
                        bigView,
                        R.id.imageview_image,
                        notification,
                        AppConstant.NOTIFICATION_ID);

                Glide.with(this)
                        .load(finalImage)
                        .asBitmap()
                        .into(notificationTarget);
            };
            mainHandler.post(myRunnable);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(AppConstant.NOTIFICATION_ID, notification);
    }

    private void handleMessageChat(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);

        String name = jsonObject.getString("name");
        int accountType = jsonObject.getInt("account_type");
        String account;
        if (accountType == AppConstant.ACCOUNT_PARENTS) {
            account = "PH";
        } else {
            account = "GV";
        }

        String image;
        if (jsonObject.has("image_user")) {
            image = jsonObject.getString("image_user");
        } else {
            image = null;
        }

        int messageType = jsonObject.getInt("message_type");
        String message;
        if (messageType == ChatConstant.MSG_TYPE_TEXT) {
            message = jsonObject.getString("message");
        } else if (messageType == ChatConstant.MSG_TYPE_ICON_HEART) {
            message = new String(Character.toChars(0x2764));
        } else {
            message = "Đã gửi 1 ảnh";
        }

        String contentText = String.format("%s %s: %s", account, name, message);

        String id = jsonObject.getString("_id");
        String idSender = jsonObject.getString("_id_sender");
        String idReceiver = jsonObject.getString("_id_receiver");
        String idConversation = jsonObject.getString("_id_conversation");
        long createdAt = jsonObject.getLong("created_at");

        saveMessage(id, idSender, idReceiver, idConversation, messageType == ChatConstant.MSG_TYPE_TEXT
                ? message : null, messageType, createdAt);

        Single.fromCallable(() -> getImageOnline(image))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> {
                    Intent intent = new Intent(this, DetailActivity.class);
                    intent.putExtra(AppConstant.KEY_FRAGMENT, AppConstant.FRAGMENT_DETAIL_CHAT);
                    Bundle bundle = new Bundle();

                    intent.putExtras(bundle);

                    NotificationCompat.Builder builder = getNotificationBuilder(intent, contentText);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder.setColor(ContextCompat.getColor(KindergartenMessagingService.this, R.color.colorPrimary));
                    }

                    if (bitmap.getWidth() != 1) {
                        builder.setLargeIcon(bitmap);
                    }
                    Notification notification = builder.setStyle(new NotificationCompat.InboxStyle()
                            .setBigContentTitle(getString(R.string.school_name)))
                            .build();

                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(ChatConstant.NOTIFICATION_ID, notification);
                }, Throwable::printStackTrace);
    }

    private void saveMessage(String id, String idSender, String idReceiver, String conversationId,
                             String messageContent, int messageType, long createdAt) {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            Message message = realm1.createObject(Message.class);
            message.setId(id);
            message.setIdSender(idSender);
            message.setIdReceiver(idReceiver);
            message.setConversationId(conversationId);
            if (messageContent != null) {
                message.setMessage(messageContent);
            }
            message.setMessageType(messageType);
            message.setStatus(ChatConstant.FRIEND_RECEIVED);
            message.setCreatedAt(createdAt);
        });
        realm.close();
    }

    private Bitmap getImageOnline(String strURL) {
        if (strURL == null) {
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_4444);
        }
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_4444);
        } catch (IOException e) {
            e.printStackTrace();
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_4444);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder(Intent intent, String contentText) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_children_white);
        builder.setContentTitle(getString(R.string.school_name));
        builder.setContentText(contentText);
        builder.setWhen(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(ContextCompat.getColor(KindergartenMessagingService.this, R.color.colorPrimary));
        }
        builder.setVibrate(new long[]{
                0, 200, 200, 600, 600
        });
        builder.mNotification.flags = Notification.FLAG_AUTO_CANCEL;

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DetailActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_ONE_SHOT
                );
        builder.setContentIntent(pendingIntent);
        return builder;
    }
}
