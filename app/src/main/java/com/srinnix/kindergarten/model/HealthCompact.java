package com.srinnix.kindergarten.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anhtu on 4/24/2017.
 */

public class HealthCompact implements Parcelable {
    private float value;
    private int state;
    private String time;

    public HealthCompact(float value, int state, String time) {

        this.value = value;
        this.state = state;
        this.time = time;
    }

    protected HealthCompact(Parcel in) {
        value = in.readFloat();
        state = in.readInt();
        time = in.readString();
    }

    public static final Creator<HealthCompact> CREATOR = new Creator<HealthCompact>() {
        @Override
        public HealthCompact createFromParcel(Parcel in) {
            return new HealthCompact(in);
        }

        @Override
        public HealthCompact[] newArray(int size) {
            return new HealthCompact[size];
        }
    };

    public float getValue() {
        return value;
    }

    public int getState() {
        return state;
    }

    public String getTime() {
        return time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(value);
        dest.writeInt(state);
        dest.writeString(time);
    }
}
