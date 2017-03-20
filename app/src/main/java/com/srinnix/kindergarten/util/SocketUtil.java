package com.srinnix.kindergarten.util;

import android.content.Context;

import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.messageeventbus.MessageChat;
import com.srinnix.kindergarten.messageeventbus.MessageContactStatus;
import com.srinnix.kindergarten.messageeventbus.MessageDisconnect;
import com.srinnix.kindergarten.messageeventbus.MessageFriendReceived;
import com.srinnix.kindergarten.messageeventbus.MessageServerReceived;
import com.srinnix.kindergarten.messageeventbus.MessageTyping;
import com.srinnix.kindergarten.messageeventbus.MessageUserConnect;
import com.srinnix.kindergarten.model.Message;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by Administrator on 2/22/2017.
 */

public class SocketUtil {
    private Socket mSocket;

    public void connect(Context context) {
        if (!isConnected()) {
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
                    .on(ChatConstant.EVENT_USER_CONNECT, args -> onEventUserConnect(args[0], true))
                    .on(ChatConstant.EVENT_USER_DISCONNECT, args -> onEventUserConnect(args[0], false))
                    .on(ChatConstant.EVENT_SETUP_CONTACT, this::onSetupContactStatus)
                    .on(ChatConstant.EVENT_SEND_SUCCESSFULLY, this::onSendSuccessfully)
                    .on(ChatConstant.EVENT_TYPING, args -> onTyping(args[0]));
            mSocket.connect();
        }
    }

    private void onEventUserConnect(Object jsonObject, boolean isConnect) {
        if (isConnect) {
            DebugLog.i("User connected");
        } else {
            DebugLog.i("User disconnect");
        }

        String idUser = JsonUtil.getIdUserDisconnect(jsonObject);
        if (idUser.isEmpty()) {
            return;
        }

        EventBus.getDefault().post(new MessageUserConnect(idUser, isConnect));
    }

    private boolean isConnected() {
        return mSocket != null && mSocket.connected();
    }

    private void onConnected() {
        DebugLog.i("Connected");
    }

    private void onDisconnect() {
        DebugLog.i("Disconnect");
        EventBus.getDefault().post(new MessageDisconnect());
    }

    private void onSetupContactStatus(Object[] args) {
        DebugLog.i("onSetupContactStatus");

        Observable.just(((JSONArray) args[0]))
                .map(jsonArray -> {
                    ArrayList<String> arrayList = JsonUtil.parseListContactOnline(args[0]);
                    return new MessageContactStatus(arrayList);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messageContactStatus -> EventBus.getDefault().postSticky(messageContactStatus));
    }

    public void sendMessage(Message message) {
        JSONObject jsonMessage = JsonUtil.getJsonMessage(message);
        mSocket.emit(Socket.EVENT_MESSAGE, jsonMessage
                , (Ack) args -> onServerReceived(args[0]));
    }

    private void onServerReceived(Object arg) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {

            JSONObject data = (JSONObject) arg;
            String id = null;
            try {
                id = data.getString(ChatConstant._ID_MESSAGE_CLIENT);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Message message = realm1.where(Message.class)
                    .equalTo("id", id)
                    .findFirst();
            try {
                message.setId(data.getString(ChatConstant._ID));
                message.setCreatedAt(data.getLong(ChatConstant.CREATED_AT));
                message.setStatus(ChatConstant.SERVER_RECEIVED);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (EventBus.getDefault().hasSubscriberForEvent(MessageServerReceived.class)) {
                EventBus.getDefault().post(new MessageServerReceived(message));
            }
        });
        realm.close();
    }

    private void onSendSuccessfully(Object[] args) {
        JSONObject data = (JSONObject) args[0];

        Message[] messageEdited = new Message[1];

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            try {
                Message message = realm.where(Message.class)
                        .equalTo("id", data.getString(ChatConstant._ID))
                        .findFirst();

                message.setId(data.getString(ChatConstant._ID));
                message.setStatus(ChatConstant.FRIEND_RECEIVED);

                messageEdited[0] = new Message(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        realm.close();

        if (EventBus.getDefault().hasSubscriberForEvent(MessageFriendReceived.class)) {
            EventBus.getDefault().post(new MessageFriendReceived(messageEdited[0]));
        }
    }

    private void onMessage(Object[] args) {
        Ack ack = (Ack) args[args.length - 1];
        ack.call();

        JSONObject jsonObject = (JSONObject) args[0];
        final Message[] messageEdited = new Message[1];

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            Message message = realm.createObject(Message.class);
            try {
                message.setId(jsonObject.getString(ChatConstant._ID));
                message.setIdSender(jsonObject.getString(ChatConstant._ID_SENDER));
                message.setIdReceiver(jsonObject.getString(ChatConstant._ID_RECEIVER));
                message.setConversationId(jsonObject.getString(ChatConstant._ID_CONVERSATION));
                message.setMessage(jsonObject.getString(ChatConstant.MESSAGE));
                message.setCreatedAt(jsonObject.getLong(ChatConstant.CREATED_AT));
                message.setStatus(ChatConstant.FRIEND_RECEIVED);
                message.setDisplayIcon(true);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            messageEdited[0] = new Message(message);
        });
        realm.close();

        if (EventBus.getDefault().hasSubscriberForEvent(MessageChat.class)) {
            EventBus.getDefault().post(new MessageChat(messageEdited[0]));
        }
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
            if (EventBus.getDefault().hasSubscriberForEvent(MessageTyping.class)) {
                EventBus.getDefault().post(JsonUtil.getMessageTyping(jsonObject));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
