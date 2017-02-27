package com.srinnix.kindergarten.database.mode;

import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.model.ContactTeacher;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2/27/2017.
 */

public class ContactTeacherRealm extends RealmObject {
    @PrimaryKey
    private String id;
    private String name;
    private String image;
    private String className;
    @Ignore
    private int status = ChatConstant.UNDEFINED;


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

    public String getImage() {
        return image;
    }

    public String getClassName() {
        return className;
    }

    public void bindData(ContactTeacher contact) {
        id = contact.getId();
        name = contact.getName();
        image = contact.getImage();
        className = contact.getClassName();
    }
}
