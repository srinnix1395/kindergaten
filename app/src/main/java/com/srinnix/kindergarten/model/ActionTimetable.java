package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anhtu on 5/4/2017.
 */

public class ActionTimetable {

    @SerializedName("action")
    @Expose
    private String action;

    @SerializedName("content")
    @Expose
    private String content;

    public String getAction() {
        return action;
    }

    public String getContent() {
        return content;
    }


}
