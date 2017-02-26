package com.srinnix.kindergarten.messageeventbus;

import com.srinnix.kindergarten.model.Message;

/**
 * Created by anhtu on 2/26/2017.
 */

public class MessageServerReceived {
    public final Message data;
    public final String id;

    public MessageServerReceived(Message data, String id) {
        this.data = data;
        this.id = id;
    }
}
