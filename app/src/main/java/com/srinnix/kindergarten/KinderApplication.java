package com.srinnix.kindergarten;

import android.app.Application;

import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.SocketUtil;

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
                mSocket = IO.socket(ChatConstant.SERVER_URL);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            mSocket.on(Socket.EVENT_CONNECT, args -> SocketUtil.onConnected(this, mSocket))
                    .on(Socket.EVENT_DISCONNECT, args -> SocketUtil.onDisconnect(mSocket))
                    .on(Socket.EVENT_RECONNECT, args -> SocketUtil.onReconnect(mSocket));
            mSocket.connect();
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
