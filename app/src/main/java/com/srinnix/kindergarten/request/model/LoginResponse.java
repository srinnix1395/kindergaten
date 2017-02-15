package com.srinnix.kindergarten.request.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.srinnix.kindergarten.model.User;

/**
 * Created by anhtu on 2/13/2017.
 */

public class LoginResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    private User mUser;

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }
}
