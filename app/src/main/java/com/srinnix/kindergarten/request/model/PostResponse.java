package com.srinnix.kindergarten.request.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.srinnix.kindergarten.model.Post;

import java.util.ArrayList;

/**
 * Created by anhtu on 2/28/2017.
 */

public class PostResponse {

    @SerializedName("posts")
    @Expose
    private ArrayList<Post> listPost;

    @SerializedName("likes")
    @Expose
    private ArrayList<String> listLikes;

    public ArrayList<Post> getListPost() {
        return listPost;
    }

    public ArrayList<String> getListLikes() {
        return listLikes;
    }
}
