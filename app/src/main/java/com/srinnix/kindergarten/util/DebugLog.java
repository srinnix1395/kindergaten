package com.srinnix.kindergarten.util;

import android.util.Log;

/**
 * Created by anhtu on 2/13/2017.
 */

public class DebugLog {
    private static String className;
    private static String methodName;
    private static int lineNumber;

    private static void getMethodName(StackTraceElement[] sElement) {
        className = sElement[1].getFileName();
        methodName = sElement[1].getMethodName();
        lineNumber = sElement[1].getLineNumber();
    }

    private static String createLog(String log) {

        StringBuilder builder = new StringBuilder();
        builder.append("[function ");
        builder.append(methodName);
        builder.append(" - line ");
        builder.append(lineNumber);
        builder.append("] ");
        builder.append(log);

        return builder.toString();
    }

    public static void i(String message) {
        getMethodName(new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }

    public static void e(String message) {
        getMethodName(new Throwable().getStackTrace());
        Log.e(className, createLog(message));
    }
}
