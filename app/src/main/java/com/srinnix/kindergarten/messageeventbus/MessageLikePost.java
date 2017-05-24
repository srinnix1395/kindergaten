package com.srinnix.kindergarten.messageeventbus;

import com.srinnix.kindergarten.request.model.LikeResponse;

/**
 * Created by anhtu on 5/24/2017.
 */

public class MessageLikePost {
    public final String idPost;
    public final boolean isLike;
    public final int numberOfLikes;

    public MessageLikePost(LikeResponse likeResponse, int numberOfLikes) {
        idPost = likeResponse.getIdPost();
        isLike = likeResponse.isLike();
        this.numberOfLikes = numberOfLikes;
    }
}
