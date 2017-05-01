package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import io.realm.RealmList;

/**
 * Created by anhtu on 2/25/2017.
 */

public class ContactParent extends Contact {
    @SerializedName("children")
    @Expose
    private ArrayList<Child> children;

    public ContactParent(String id, String name, String gender, ArrayList<Child> children, boolean isMyClass) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.children = children;
        this.isMyClass = isMyClass;
    }

    public ContactParent() {

    }

    public ArrayList<Child> getChildren() {
        return children;
    }

    public void setChildren(RealmList<Child> children) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        for (Child child : children) {
            this.children.add(new Child(child.getName(), child.getImage(), child.getIdClass()));
        }
    }
}
