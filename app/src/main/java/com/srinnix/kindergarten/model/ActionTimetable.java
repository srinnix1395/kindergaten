package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anhtu on 5/4/2017.
 */

public class ActionTimetable {

    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("content")
    @Expose
    private String content;

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

}
