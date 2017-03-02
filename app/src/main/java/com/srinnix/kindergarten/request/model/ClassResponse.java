package com.srinnix.kindergarten.request.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.Class;

import java.util.ArrayList;

/**
 * Created by anhtu on 3/1/2017.
 */

public class ClassResponse {
    @SerializedName("info")
    @Expose
    private Class aClass;

    @SerializedName("children")
    @Expose
    private ArrayList<Child> children;

    public Class getaClass() {
        return aClass;
    }

    public ArrayList<Child> getChildren() {
        return children;
    }
}
