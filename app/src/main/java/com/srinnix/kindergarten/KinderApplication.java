package com.srinnix.kindergarten;

import android.app.Application;
import android.util.Log;

import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.SharedPreUtils;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by anhtu on 2/17/2017.
 */

public class KinderApplication extends Application {

    public static final String TAG = "KinderApplication";

    private static KinderApplication mInstance;

    private Socket mSocket;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        connect();
    }

    private void connect() {
        if (!SharedPreUtils.getInstance(this).isUserSignedIn()) {
            try {
                mSocket = IO.socket(AppConstant.SERVER_URL);
                Log.i(TAG, "connect: " + mSocket.id());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            mSocket.on(Socket.EVENT_CONNECT, args -> {
                DebugLog.i("Connected");
            }).on(Socket.EVENT_DISCONNECT, args -> {
                DebugLog.i("Disconnected");
            }).on(Socket.EVENT_RECONNECT, args -> {
                DebugLog.i("Reconnect");
            });
            mSocket.connect();
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
