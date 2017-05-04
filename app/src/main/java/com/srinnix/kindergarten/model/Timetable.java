package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by anhtu on 5/4/2017.
 */
public class Timetable {

    @SerializedName("event")
    @Expose
    private String event;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("time_table")
    @Expose
    private ArrayList<ActionTimetable> timeTable = null;

    public String getEvent() {
        return event;
    }

    public String getSubject() {
        return subject;
    }

    public String getTime() {
        return time;
    }

    public ArrayList<ActionTimetable> getTimeTable() {
        return timeTable;
    }

}