package com.srinnix.kindergarten.messageeventbus;

import com.srinnix.kindergarten.model.Post;

/**
 * Created by anhtu on 4/25/2017.
 */

public class MessagePostSuccessfully {
    public final Post post;

    public MessagePostSuccessfully(Post post) {
        this.post = post;
    }
}
