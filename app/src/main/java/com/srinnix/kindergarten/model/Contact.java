package com.srinnix.kindergarten.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.srinnix.kindergarten.constant.ChatConstant;

/**
 * Created by anhtu on 2/23/2017.
 */

public class Contact implements Parcelable {

    @SerializedName("_id")
    @Expose
    protected String id;

    @SerializedName("name")
    @Expose
    protected String name;

    @SerializedName("gender")
    @Expose
    protected String gender;

    protected int status = ChatConstant.STATUS_UNDEFINED;

    public Contact() {
    }

    protected Contact(Parcel in) {
        id = in.readString();
        name = in.readString();
        gender = in.readString();
        status = in.readInt();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(gender);
        dest.writeInt(status);
    }
}