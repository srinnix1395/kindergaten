package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2/14/2017.
 */

public class User {
    @SerializedName("_id_user")
    @Expose
    private String id;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("DOB")
    @Expose
    private String dob;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("account_type")
    @Expose
    private int accountType;

    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;

    @SerializedName("_id_class")
    @Expose
    private String idClass;

    @SerializedName("class_name")
    @Expose
    private String className;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("token")
    @Expose
    private String token;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public int getAccountType() {
        return accountType;
    }

    public String getToken() {
        return token;
    }

    public String getIdClass() {
        return idClass;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getClassName() {
        return className;
    }

}
