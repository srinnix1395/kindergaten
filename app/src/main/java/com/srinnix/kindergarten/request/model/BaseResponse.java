package com.srinnix.kindergarten.request.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anhtu on 2/13/2017.
 */

public abstract class BaseResponse {
    public static final int RESULT_NG = 0;
    public static final int RESULT_OK = 1;

    @SerializedName("result")
    @Expose
    public int result;

    @SerializedName("error")
    @Expose
    public Error error;
}