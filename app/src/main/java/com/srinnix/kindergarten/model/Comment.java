package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anhtu on 3/28/2017.
 */

public class Comment {
    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("comment")
    @Expose
    private String comment;

    @SerializedName("created_at")
    @Expose
    private long createdAt;

    @SerializedName("account_type")
    @Expose
    private int accountType;

    private boolean isSuccess = true;

    public Comment(String id, String name, String image, String comment, long createdAt, int accountType) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.comment = comment;
        this.createdAt = createdAt;
        this.accountType = accountType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getComment() {
        return comment;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getAccountType() {
        return accountType;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
