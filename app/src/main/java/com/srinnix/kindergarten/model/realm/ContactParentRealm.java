package com.srinnix.kindergarten.model.realm;

import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.ContactParent;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2/27/2017.
 */

public class ContactParentRealm extends RealmObject {

    @PrimaryKey
    private String id;
    private String name;
    private String gender;
    private RealmList<Child> children;

    public ContactParentRealm() {
    }

    public ContactParentRealm(ContactParent contact) {
        id = contact.getId();
        name = contact.getName();
        gender = contact.getGender();

        children = new RealmList<>();
        children.addAll(contact.getChildren());
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public RealmList<Child> getChildren() {
        return children;
    }
}
