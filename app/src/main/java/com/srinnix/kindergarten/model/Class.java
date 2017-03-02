package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2/21/2017.
 */

public class Class {
    @SerializedName("_id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("list_teachers")
    @Expose
    private ArrayList<Teacher> teacherArrayList;

    @SerializedName("number_member")
    @Expose
    private int numberMember;

    @SerializedName("_id_school")
    @Expose
    private String idSchool;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Teacher> getTeacherArrayList() {
        return teacherArrayList;
    }

    public int getNumberMember() {
        return numberMember;
    }

    public String getIdSchool() {
        return idSchool;
    }
}
