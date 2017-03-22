package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anhtu on 3/13/2017.
 */

public class LikeModel {
    @SerializedName("_id_user")
    @Expose
    private String idUser;

    @SerializedName("name")
    @Expose
    private String name;

    public String getIdUser() {
        return idUser;
    }

    public String getName() {
        return name;
    }
}
