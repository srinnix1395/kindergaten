package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    public int getNumberMember() {
        return numberMember;
    }

    public String getIdSchool() {
        return idSchool;
    }
}
