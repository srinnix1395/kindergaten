package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anhtu on 4/11/2017.
 */

public class HealthTotalChildren {
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

    @SerializedName("health")
    @Expose
    private Health health;

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

    public Health getHealth() {
        return health;
    }
}
