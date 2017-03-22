package com.srinnix.kindergarten.messageeventbus;

import com.srinnix.kindergarten.model.Message;

/**
 * Created by anhtu on 2/26/2017.
 */

public class MessageFriendReceived {
    public final Message data;

    public MessageFriendReceived(Message data) {
        this.data = data;
    }
}
