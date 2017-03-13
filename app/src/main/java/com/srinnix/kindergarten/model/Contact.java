package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.srinnix.kindergarten.constant.ChatConstant;

/**
 * Created by anhtu on 2/23/2017.
 */

public class Contact {

    @SerializedName("_id")
    @Expose
    protected String id;

    @SerializedName("name")
    @Expose
    protected String name;

    @SerializedName("gender")
    @Expose
    protected String gender;

    protected int status = ChatConstant.UNDEFINED;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}