package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by anhtu on 2/23/2017.
 */

public class DataLogin {

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("contact")
    @Expose
    private ArrayList<Contact> contacts = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

}