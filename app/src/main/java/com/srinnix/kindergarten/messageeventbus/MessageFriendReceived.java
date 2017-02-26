package com.srinnix.kindergarten.messageeventbus;

/**
 * Created by anhtu on 2/26/2017.
 */

public class MessageFriendReceived {
    public final Object data;

    public MessageFriendReceived(Object data) {
        this.data = data;
    }
}
