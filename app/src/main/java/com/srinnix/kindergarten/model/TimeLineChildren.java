package com.srinnix.kindergarten.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anhtu on 3/8/2017.
 */

public class TimeLineChildren {
    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("weight")
    @Expose
    private int weight;

    @SerializedName("height")
    @Expose
    private int height;


}
