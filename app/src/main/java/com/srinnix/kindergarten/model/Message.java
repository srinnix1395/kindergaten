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
    private boolean showTime;

    public Message() {
    }

    public Message(String id, String idSender, String idReceiver, String message, long createdAt, int status, int layoutType) {
        this.id = id;
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.message = message;
        this.createdAt = createdAt;
        this.status = status;
        this.layoutType = layoutType;
    }

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

    public String getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }

    public boolean isShowTime() {
        return showTime;
    }

    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }
}
