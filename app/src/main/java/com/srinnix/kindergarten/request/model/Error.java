package com.srinnix.kindergarten.request.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anhtu on 2/13/2017.
 */

public class Error{
    @SerializedName("error_code")
    @Expose
    public int code;

    @SerializedName("error_message")
    @Expose
    public String message;

    public Error(int code, String message) {
        this.code = code;
        this.message = message;
    }
}