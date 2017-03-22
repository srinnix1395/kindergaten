package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anhtu on 3/1/2017.
 */

public class Teacher {
    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("DOB")
    @Expose
    private String DOB;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("achievement")
    @Expose
    private String achievement;

    @SerializedName("_id_class")
    @Expose
    private String classId;

    @SerializedName("class_name")
    @Expose
    private String className;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDOB() {
        return DOB;
    }

    public String getImage() {
        return image;
    }

    public String getAchievement() {
        return achievement;
    }

    public String getClassId() {
        return classId;
    }

    public String getGender() {
        return gender;
    }

    public String getClassName() {
        return className;
    }
}
