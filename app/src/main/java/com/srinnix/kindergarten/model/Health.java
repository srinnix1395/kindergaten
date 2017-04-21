package com.srinnix.kindergarten.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.srinnix.kindergarten.constant.AppConstant;

/**
 * Created by anhtu on 4/21/2017.
 */

public class Health implements Parcelable{
    @SerializedName("_id")
    @Expose
    protected String id;

    @SerializedName("measurement_time")
    @Expose
    protected String measureTime;

    @SerializedName("weight")
    @Expose
    protected float weight;

    @SerializedName("weight_state")
    @Expose
    protected int weightState;

    @SerializedName("height")
    @Expose
    protected int height;

    @SerializedName("height_state")
    @Expose
    protected int heightState;

    @SerializedName("created_at")
    @Expose
    protected long createdAt;

    public Health() {
        weight = AppConstant.UNSPECIFIED;
        weightState = AppConstant.UNSPECIFIED;
        height = AppConstant.UNSPECIFIED;
        heightState = AppConstant.UNSPECIFIED;
    }

    protected Health(Parcel in) {
        id = in.readString();
        measureTime = in.readString();
        weight = in.readFloat();
        weightState = in.readInt();
        height = in.readInt();
        heightState = in.readInt();
        createdAt = in.readLong();
    }

    public static final Creator<Health> CREATOR = new Creator<Health>() {
        @Override
        public Health createFromParcel(Parcel in) {
            return new Health(in);
        }

        @Override
        public Health[] newArray(int size) {
            return new Health[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(measureTime);
        dest.writeFloat(weight);
        dest.writeInt(weightState);
        dest.writeInt(height);
        dest.writeInt(heightState);
        dest.writeLong(createdAt);
    }
}
