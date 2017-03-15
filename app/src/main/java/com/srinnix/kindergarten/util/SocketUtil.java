package com.srinnix.kindergarten.util;

import android.content.Context;

import com.srinnix.kindergarten.KinderApplication;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.messageeventbus.MessageChat;
import com.srinnix.kindergarten.messageeventbus.MessageContactStatus;
import com.srinnix.kindergarten.messageeventbus.MessageDisconnect;
import com.srinnix.kindergarten.messageeventbus.MessageFriendReceived;
import com.srinnix.kindergarten.messageeventbus.MessageServerReceived;
import com.srinnix.kindergarten.model.Message;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by Administrator on 2/22/2017.
 */

public class SocketUtil {
    private Socket mSocket;

    public void connect(Context context) {
        try {
            IO.Options options = new IO.Options();

            SharedPreUtils sharedPreUtils = SharedPreUtils.getInstance(context);
            options.query = "token=" + sharedPreUtils.getToken() +
                    "&id=" + sharedPreUtils.getUserID() +
                    "&account_type=" + sharedPreUtils.getAccountType();

            mSocket = IO.socket(ChatConstant.SERVER_URL, options);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.on(Socket.EVENT_CONNECT, args -> onConnected())
                .on(Socket.EVENT_DISCONNECT, args -> onDisconnect())
                .on(Socket.EVENT_MESSAGE, this::onMessage)
                .on(ChatConstant.EVENT_SETUP_CONTACT, this::onSetupContacts)
                .on(ChatConstant.EVENT_SEND_SUCCESSFULLY, this::onSendSuccessfully)
                .on(ChatConstant.EVENT_TYPING, args -> onTyping(args[0]));
        mSocket.connect();
    }

    public boolean isConnected() {
        return mSocket != null && mSocket.connected();
    }

    private void onConnected() {
        DebugLog.i("Connected");
    }

    private void onDisconnect() {
        DebugLog.i("Disconnect");
        EventBus.getDefault().post(new MessageDisconnect());
    }

    private void onSetupContacts(Object[] args) {
        DebugLog.i("onSetupContacts");

        Observable.just(((JSONArray) args[0]))
                .map(jsonArray -> {
                    ArrayList<String> arrayList = JsonUtil.parseListContact(args[0]);
                    return new MessageContactStatus(arrayList);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messageContactStatus -> EventBus.getDefault().post(messageContactStatus));
    }

    public void sendMessage(Message message) {
        JSONObject jsonMessage = JsonUtil.getJsonMessage(message);
        mSocket.emit(Socket.EVENT_MESSAGE, jsonMessage
                , (Ack) args -> {
                    try {
                        onServerReceived(args[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void onServerReceived(Object arg) throws JSONException {
        JSONObject data = (JSONObject) arg;
        String id = data.getString(ChatConstant._ID_MESSAGE_CLIENT);

        Single.fromCallable(() -> {
            Realm realm = KinderApplication.getInstance().getRealm();
            RealmResults<Message> results = realm.where(Message.class)
                    .equalTo("id", id)
                    .findAll();

            if (results != null && results.size() == 1) {
                Message message = results.get(0);
                message.setId(data.getString(ChatConstant._ID));
                message.setCreatedAt(data.getLong(ChatConstant.CREATED_AT));
                message.setStatus(ChatConstant.SERVER_RECEIVED);

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(message);
                realm.commitTransaction();
                return message;
            }
            return null;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> EventBus.getDefault().post(new MessageServerReceived(message, id)));


    }

    private void onSendSuccessfully(Object[] args) {
        JSONObject ack = (JSONObject) args[0];

        Single.fromCallable(() -> {
            Realm realm = KinderApplication.getInstance().getRealm();

            RealmResults<Message> results = realm.where(Message.class)
                    .equalTo("id", ack.getString(ChatConstant._ID))
                    .findAll();

            if (results != null && results.size() == 1) {
                Message message = results.get(0);
                message.setId(ack.getString(ChatConstant._ID));
                message.setStatus(ChatConstant.FRIEND_RECEIVED);

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(message);
                realm.commitTransaction();
                return message;
            }
            return null;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> EventBus.getDefault().post(new MessageFriendReceived(message)));
    }

    private void onMessage(Object[] args) {
        Ack ack = (Ack) args[0];
        ack.call();

        JSONObject jsonObject = (JSONObject) args[0];

        Single.fromCallable(() -> {
            Realm realm = KinderApplication.getInstance().getRealm();

            realm.beginTransaction();
            Message message = realm.createObject(Message.class);
            try {
                message.setId(jsonObject.getString(ChatConstant._ID));
                message.setIdSender(jsonObject.getString(ChatConstant._ID_SENDER));
                message.setIdReceiver(jsonObject.getString(ChatConstant._ID_RECEIVER));
                message.setMessage(message.getMessage());
                message.setCreatedAt(message.getCreatedAt());
                message.setStatus(ChatConstant.FRIEND_RECEIVED);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            realm.commitTransaction();
            return message;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(message -> EventBus.getDefault().post(new MessageChat(message)));
    }

    public void sendStatusTyping(String idSender, String idReceiver, boolean isTyping) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ChatConstant._ID_SENDER, idSender);
            jsonObject.put(ChatConstant._ID_RECEIVER, idReceiver);
            jsonObject.put(ChatConstant.IS_TYPING, isTyping);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSocket.emit(ChatConstant.EVENT_TYPING, jsonObject);
    }

    private void onTyping(Object arg) {
        JSONObject jsonObject = (JSONObject) arg;

        try {
            EventBus.getDefault().post(JsonUtil.getMessageTyping(jsonObject));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
