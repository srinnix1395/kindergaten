package com.srinnix.kindergarten.model;

/**
 * Created by anhtu on 5/5/2017.
 */

public class HeaderTimetable {
    private String subject;
    private String content;
    private boolean isColor;

    public HeaderTimetable(String subject, String content, boolean isColor) {
        this.subject = subject;
        this.content = content;
        this.isColor = isColor;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public boolean isColor() {
        return isColor;
    }

    public void setColor(boolean color) {
        isColor = color;
    }
}
