package com.srinnix.kindergarten.messageeventbus;

import com.srinnix.kindergarten.model.Message;

/**
 * Created by anhtu on 2/26/2017.
 */

public class MessageServerReceived {
    public final Message data;

    public MessageServerReceived(Message data) {
        this.data = data;
    }
}
