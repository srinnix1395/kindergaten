package com.srinnix.kindergarten.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by anhtu on 2/21/2017.
 */

public class Child extends RealmObject implements Parcelable {

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("aka")
    @Expose
    private String aka;

    @SerializedName("DOB")
    @Expose
    private String DOB;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("hobby")
    @Expose
    private String hobby;

    @SerializedName("characteristic")
    @Expose
    private String characteristic;

    @SerializedName("_id_class")
    @Expose
    private String idClass;

    public Child() {
//        do nothing
    }

    public Child(String id, String image, String name, String aka, String DOB, String hobby, String characteristic) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.aka = aka;
        this.DOB = DOB;
        this.hobby = hobby;
        this.characteristic = characteristic;
    }

    public Child(String id, String name, String image, String idClass) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.idClass = idClass;
    }

    protected Child(Parcel in) {
        id = in.readString();
        image = in.readString();
        name = in.readString();
        aka = in.readString();
        DOB = in.readString();
        gender = in.readString();
        hobby = in.readString();
        characteristic = in.readString();
        idClass = in.readString();
    }

    public static final Creator<Child> CREATOR = new Creator<Child>() {
        @Override
        public Child createFromParcel(Parcel in) {
            return new Child(in);
        }

        @Override
        public Child[] newArray(int size) {
            return new Child[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAka() {
        return aka;
    }

    public void setAka(String aka) {
        this.aka = aka;
    }

    public String getIdClass() {
        return idClass;
    }

    public void setIdClass(String idClass) {
        this.idClass = idClass;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(image);
        dest.writeString(name);
        dest.writeString(aka);
        dest.writeString(DOB);
        dest.writeString(gender);
        dest.writeString(hobby);
        dest.writeString(characteristic);
        dest.writeString(idClass);
    }
}
