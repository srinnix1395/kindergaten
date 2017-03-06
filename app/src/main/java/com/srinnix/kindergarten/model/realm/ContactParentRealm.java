package com.srinnix.kindergarten.model.realm;

import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.model.ContactParent;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by Administrator on 2/27/2017.
 */

public class ContactParentRealm extends RealmObject {
    private String id;
    private String name;
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

    public void bindData(ContactParent contactParent) {
        id = contactParent.getId();
        name = contactParent.getName();
    }
}
