package com.srinnix.kindergarten.request.model;

/**
 * Created by anhtu on 2/13/2017.
 */

public abstract class BaseResponse {
    public static final int RESULT_NG = 0;
    public static final int RESULT_OK = 1;

    public int result;
    public Error error;
}
