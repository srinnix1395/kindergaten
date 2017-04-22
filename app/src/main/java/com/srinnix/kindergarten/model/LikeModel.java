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

    @SerializedName("name_user")
    @Expose
    private String name;

    @SerializedName("image_user")
    @Expose
    private String image;

    @SerializedName("account_type")
    @Expose
    private int accountType;

    @SerializedName("created_at")
    @Expose
    private long createdAt;

    public LikeModel(String name, String image, int accountType, long createdAt) {
        this.name = name;
        this.image = image;
        this.accountType = accountType;
        this.createdAt = createdAt;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getName() {
        return name;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getImage() {
        return image;
    }

    public int getAccountType() {
        return accountType;
    }
}
