package com.srinnix.kindergarten.request.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.User;

import java.util.ArrayList;

/**
 * Created by anhtu on 2/23/2017.
 */

public class DataLogin {

    @SerializedName("info")
    @Expose
    private User user;

    @SerializedName("contact")
    @Expose
    private ArrayList<Contact> contacts = null;

    public User getUser() {
        return user;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

}