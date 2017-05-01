package com.srinnix.kindergarten.model.realm;

import com.srinnix.kindergarten.model.ContactTeacher;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2/27/2017.
 */

public class ContactTeacherRealm extends RealmObject {

    @PrimaryKey
    private String id;
    private String name;
    private String gender;
    private String image;
    private String className;
    private boolean isMyClass;

    public ContactTeacherRealm() {
    }

    public ContactTeacherRealm(ContactTeacher contact) {
        id = contact.getId();
        name = contact.getName();
        gender = contact.getGender();
        image = contact.getImage();
        className = contact.getClassName();
        isMyClass = contact.isMyClass();
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

    public String getImage() {
        return image;
    }

    public String getClassName() {
        return className;
    }

}
