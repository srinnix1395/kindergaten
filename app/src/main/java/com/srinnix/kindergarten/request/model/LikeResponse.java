package com.srinnix.kindergarten.request.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 3/1/2017.
 */

public class LikeResponse {

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("_id_post")
    @Expose
    private String idPost;

    @SerializedName("_id_user")
    @Expose
    private String idUser;

    @SerializedName("is_like")
    @Expose
    private boolean isLike;


    public String getId() {
        return id;
    }

    public String getIdPost() {
        return idPost;
    }

    public String getIdUser() {
        return idUser;
    }

    public boolean isLike() {
        return isLike;
    }
}
