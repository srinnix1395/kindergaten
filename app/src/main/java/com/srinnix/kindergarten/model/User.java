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

    @SerializedName("account_type")
    @Expose
    private int accountType;

    @SerializedName("_id_class")
    @Expose
    private String idClass;

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

}
