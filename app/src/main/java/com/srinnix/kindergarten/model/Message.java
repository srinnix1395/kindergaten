package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by DELL on 2/9/2017.
 */

public class Message extends RealmObject {

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("_id_sender")
    @Expose
    private String idSender;

    @SerializedName("_id_receiver")
    @Expose
    private String idReceiver;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("created_at")
    @Expose
    private long createdAt;

    @SerializedName("status")
    @Expose
    private int status;

    @Ignore
    private int layoutType;

    @Ignore
    private boolean isTypingMessage;

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

    public Message(String id, String idSender, String idReceiver, String message, long createdAt, int status, int layoutType, boolean isTypingMessage) {
        this.id = id;
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.message = message;
        this.createdAt = createdAt;
        this.status = status;
        this.layoutType = layoutType;
        this.isTypingMessage = isTypingMessage;
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

    public boolean isTypingMessage() {
        return isTypingMessage;
    }

    public void setTypingMessage(boolean typingMessage) {
        isTypingMessage = typingMessage;
    }

}
