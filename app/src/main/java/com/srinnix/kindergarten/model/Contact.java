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

    protected boolean isMyClass;

    protected int status = ChatConstant.STATUS_UNDEFINED;

    public Contact() {
        isMyClass = false;
    }

    protected Contact(Parcel in) {
        id = in.readString();
        name = in.readString();
        gender = in.readString();
        isMyClass = in.readByte() != 0;
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

    public boolean isMyClass() {
        return isMyClass;
    }

    public void setMyClass(boolean myClass) {
        isMyClass = myClass;
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
        dest.writeByte((byte) (isMyClass ? 1 : 0));
        dest.writeInt(status);
    }
}