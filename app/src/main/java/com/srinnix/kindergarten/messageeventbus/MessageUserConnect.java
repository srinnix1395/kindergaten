package com.srinnix.kindergarten.messageeventbus;

/**
 * Created by anhtu on 3/17/2017.
 */

public class MessageUserConnect {
    public final String id;
    public final boolean isConnected;

    public MessageUserConnect(String id, boolean isConnected) {
        this.id = id;
        this.isConnected = isConnected;
    }
}
