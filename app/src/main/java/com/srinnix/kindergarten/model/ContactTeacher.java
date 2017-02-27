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

    public ContactTeacher(String id, String name, String image, String className) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.className = className;
    }


    public String getImage() {
        return image;
    }

    public String getClassName() {
        return className;
    }

}
