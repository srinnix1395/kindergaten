package com.srinnix.kindergarten.request.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.User;

import java.util.ArrayList;

/**
 * Created by anhtu on 2/23/2017.
 */

public class LoginResponse {

    @SerializedName("info")
    @Expose
    private User user;

    @SerializedName("children")
    @Expose
    private ArrayList<Child> children;

    @SerializedName("contact")
    @Expose
    private ArrayList<Contact> contacts;

    public User getUser() {
        return user;
    }

    public ArrayList<Child> getChildren() {
        return children;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

}