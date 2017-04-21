package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.srinnix.kindergarten.constant.AppConstant;

/**
 * Created by anhtu on 4/11/2017.
 */

public class HealthTotal extends Health{
    @SerializedName("eyes")
    @Expose
    private String eyes;

    @SerializedName("ent")
    @Expose
    private String ent;

    @SerializedName("tooth")
    @Expose
    private String tooth;

    @SerializedName("others")
    @Expose
    private String others;

    @SerializedName("result")
    @Expose
    private int result;

    private boolean isDisplayLine = true;

    public HealthTotal() {
        super();
        result = AppConstant.UNSPECIFIED;
    }

    public String getEyes() {
        return eyes;
    }

    public String getEnt() {
        return ent;
    }

    public String getTooth() {
        return tooth;
    }

    public String getOthers() {
        return others;
    }

    public int getResult() {
        return result;
    }

    public boolean isDisplayLine() {
        return isDisplayLine;
    }

    public void setDisplayLine(boolean displayLine) {
        isDisplayLine = displayLine;
    }
}
