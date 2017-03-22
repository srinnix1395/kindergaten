package com.srinnix.kindergarten.request.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.Class;
import com.srinnix.kindergarten.model.Teacher;

import java.util.ArrayList;

/**
 * Created by anhtu on 3/1/2017.
 */

public class ClassResponse {
    @SerializedName("info")
    @Expose
    private Class aClass;

    @SerializedName("teachers")
    @Expose
    private ArrayList<Teacher> teacherArrayList;

    @SerializedName("children")
    @Expose
    private ArrayList<Child> children;

    public Class getaClass() {
        return aClass;
    }

    public ArrayList<Teacher> getTeacherArrayList() {
        return teacherArrayList;
    }

    public ArrayList<Child> getChildren() {
        return children;
    }
}
