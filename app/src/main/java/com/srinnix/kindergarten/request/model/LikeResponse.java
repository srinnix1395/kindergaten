package com.srinnix.kindergarten.request.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 3/1/2017.
 */

public class LikeResponse {

    @SerializedName("_id_post")
    @Expose
    private String idPost;

    @SerializedName("is_like")
    @Expose
    private boolean isLike;

    public String getIdPost() {
        return idPost;
    }

    public boolean isLike() {
        return isLike;
    }
}
