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

    @SerializedName("image")
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

    @SerializedName("number_of_comments")
    @Expose
    private int numberOfComments;

    private boolean isUserLike;

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

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public boolean isUserLike() {
        return isUserLike;
    }

    public void setUserLike(boolean userLike) {
        isUserLike = userLike;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }
}
