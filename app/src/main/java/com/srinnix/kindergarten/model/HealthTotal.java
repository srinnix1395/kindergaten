package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.srinnix.kindergarten.constant.AppConstant;

/**
 * Created by anhtu on 4/11/2017.
 */

public class HealthTotal {

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("measurement_time")
    @Expose
    private String measureTime;

    @SerializedName("weight")
    @Expose
    private float weight;

    @SerializedName("weight_state")
    @Expose
    private int weightState;

    @SerializedName("height")
    @Expose
    private int height;

    @SerializedName("height_state")
    @Expose
    private int heightState;

    @SerializedName("created_at")
    @Expose
    private long createdAt;

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
        weight = AppConstant.UNSPECIFIED;
        weightState = AppConstant.UNSPECIFIED;
        height = AppConstant.UNSPECIFIED;
        heightState = AppConstant.UNSPECIFIED;
        result = AppConstant.UNSPECIFIED;
    }

    public String getId() {
        return id;
    }

    public String getMeasureTime() {
        return measureTime;
    }

    public float getWeight() {
        return weight;
    }

    public int getWeightState() {
        return weightState;
    }

    public int getHeight() {
        return height;
    }

    public int getHeightState() {
        return heightState;
    }

    public long getCreatedAt() {
        return createdAt;
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
