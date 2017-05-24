package com.srinnix.kindergarten.request.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.srinnix.kindergarten.model.Post;

/**
 * Created by anhtu on 5/24/2017.
 */

public class DetailPostResponse {
    @SerializedName("post")
    @Expose
    private Post post;

    @SerializedName("user_like")
    @Expose
    private boolean isUserLike;

    public Post getPost() {
        return post;
    }

    public boolean isUserLike() {
        return isUserLike;
    }
}
