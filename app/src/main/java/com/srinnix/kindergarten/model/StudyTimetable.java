package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by anhtu on 5/5/2017.
 */
public class StudyTimetable {

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("subject")
    @Expose
    private String subject;

    @SerializedName("schedule")
    @Expose
    private ArrayList<StudySchedule> schedule = null;

    public String getTime() {
        return time;
    }

    public String getSubject() {
        return subject;
    }

    public ArrayList<StudySchedule> getSchedule() {
        return schedule;
    }
}