package com.srinnix.kindergarten.request.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.srinnix.kindergarten.model.Post;

/**
 * Created by anhtu on 5/15/2017.
 */

public class PostResponse {
    @SerializedName("post")
    @Expose
    private Post post;

    @SerializedName("is_schedule")
    @Expose
    private boolean isSchedule;

    @SerializedName("time_schedule")
    @Expose
    private String time;

    public Post getPost() {
        return post;
    }

    public boolean isSchedule() {
        return isSchedule;
    }

    public String getTime() {
        return time;
    }
}
