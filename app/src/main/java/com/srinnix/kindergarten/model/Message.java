package com.srinnix.kindergarten.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by DELL on 2/9/2017.
 */

public class Message extends RealmObject {

    @PrimaryKey
    private String id;

    private String idSender;

    private String idReceiver;

    private String message;

    private long createdAt;

    private int status;

    @Ignore
    private int layoutType;

    @Ignore
    private boolean displayTime ;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLayoutType() {
        return layoutType;
    }


    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(boolean displayTime) {
        this.displayTime = displayTime;
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }
}
