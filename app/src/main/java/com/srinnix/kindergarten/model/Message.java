package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;

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

    @Index
    @SerializedName("_id_conversation")
    @Expose
    private String conversationId;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("created_at")
    @Expose
    private long createdAt;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("is_seen")
    @Expose
    private boolean isSeen;

    @Ignore
    private boolean isTypingMessage;

//    @Ignore
//    private boolean isDisplayIcon;


    public Message() {
    }

//    public Message(String id, String idSender, String idReceiver, String message, long createdAt, int status
//            , boolean isTypingMessage, boolean isDisplayIcon) {
//        this.id = id;
//        this.idSender = idSender;
//        this.idReceiver = idReceiver;
//        this.message = message;
//        this.createdAt = createdAt;
//        this.status = status;
//        this.isTypingMessage = isTypingMessage;
//        this.isDisplayIcon = isDisplayIcon;
//    }
    public Message(String id, String idSender, String idReceiver, String message, long createdAt, int status
            , boolean isTypingMessage) {
        this.id = id;
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.message = message;
        this.createdAt = createdAt;
        this.status = status;
        this.isTypingMessage = isTypingMessage;
    }

    public Message(Message messageSample) {
        this.id = messageSample.getId();
        this.idSender = messageSample.getIdSender();
        this.idReceiver = messageSample.getIdReceiver();
        this.message = messageSample.getMessage();
        this.createdAt = messageSample.getCreatedAt();
        this.status = messageSample.getStatus();
        this.isTypingMessage = messageSample.isTypingMessage();
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

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

//    public boolean isDisplayIcon() {
//        return isDisplayIcon;
//    }
//
//    public void setDisplayIcon(boolean displayIcon) {
//        isDisplayIcon = displayIcon;
//    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}
