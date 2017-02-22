package com.srinnix.kindergarten.util;

import android.content.Context;

import com.srinnix.kindergarten.constant.ChatConstant;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;

/**
 * Created by Administrator on 2/22/2017.
 */

public class SocketUtil {
    public static void onConnected(Context context, Socket socket) {
        DebugLog.i("Connected");
        if (socket == null) {
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ChatConstant._ID, SharedPreUtils.getInstance(context).getCurrentUserID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit(ChatConstant.EVENT_SETUP_ID, jsonObject);
    }

    public static void onDisconnect(Socket socket) {
        DebugLog.i("Disconnected");

    }

    public static void onReconnect(Socket socket) {
        DebugLog.i("Reconnect");
    }
}
