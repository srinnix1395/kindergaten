package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by anhtu on 5/4/2017.
 */

public class StudyTimetable {

    @SerializedName("group")
    @Expose
    private String group;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("time_table")
    @Expose
    private ArrayList<StudyTimetable> timeTable = null;

    public String getGroup() {
        return group;
    }

    public String getTime() {
        return time;
    }

    public ArrayList<StudyTimetable> getStudyTimetable() {
        return timeTable;
    }

}