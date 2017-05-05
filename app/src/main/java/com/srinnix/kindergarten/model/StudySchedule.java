package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anhtu on 5/4/2017.
 */

public class StudySchedule {

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("subject")
    @Expose
    private String subject;

    @SerializedName("content")
    @Expose
    private String content;

    private boolean isColor;

    public String getTime() {
        return time;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public boolean isColor() {
        return isColor;
    }

    public void setColor(boolean color) {
        isColor = color;
    }
}