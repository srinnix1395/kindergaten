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
    private int id;

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

    public Post(int id, String content, ArrayList<String> listImage, int type, long createdAt) {
        this.id = id;
        this.content = content;
        this.listImage = listImage;
        this.type = type;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getListImage() {
        return listImage;
    }

    public void setListImage(ArrayList<String> listImage) {
        this.listImage = listImage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

}
