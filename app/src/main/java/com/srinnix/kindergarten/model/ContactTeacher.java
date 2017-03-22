package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anhtu on 2/25/2017.
 */

public class ContactTeacher extends Contact {

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("class_name")
    @Expose
    private String className;

    public ContactTeacher(String id, String name, String gender, String image, String className) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.image = image;
        this.className = className;
    }

    public ContactTeacher() {

    }

    public ContactTeacher(String name, String gender, String className) {
        this.name = name;
        this.gender = gender;
        this.className = className;
    }

    public String getImage() {
        return image;
    }

    public String getClassName() {
        return className;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
