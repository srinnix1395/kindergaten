package com.srinnix.kindergarten.exception;

/**
 * Created by Administrator on 2/28/2017.
 */

public class MessageNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "There's no message in database";
    }
}
