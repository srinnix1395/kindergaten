package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DELL on 2/9/2017.
 */

public class Post {
    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("list_image")
    @Expose
    private ArrayList<String> listImage;

    @SerializedName("notification_type")
    @Expose
    private int type;

    @SerializedName("created_at")
    @Expose
    private long createdAt;

    @SerializedName("number_of_likes")
    @Expose
    private int numberOfLikes;

    private boolean isUserLike;

    public Post(String id, String content, ArrayList<String> listImage, int type, long createdAt
            , int numberOfLikes, boolean isUserLike) {
        this.id = id;
        this.content = content;
        this.listImage = listImage;
        this.type = type;
        this.createdAt = createdAt;
        this.numberOfLikes = numberOfLikes;
        this.isUserLike = isUserLike;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public ArrayList<String> getListImage() {
        return listImage;
    }

    public int getType() {
        return type;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public boolean isUserLike() {
        return isUserLike;
    }

    public void setUserLike(boolean userLike) {
        isUserLike = userLike;
    }
}
