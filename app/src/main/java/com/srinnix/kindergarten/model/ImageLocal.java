package com.srinnix.kindergarten.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anhtu on 4/25/2017.
 */

public class ImageLocal implements Parcelable{

    private long id;
    private String name;
    private String path;
    private boolean isSelected;

    public ImageLocal(long id, String name, String path, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.isSelected = isSelected;
    }

    protected ImageLocal(Parcel in) {
        id = in.readLong();
        name = in.readString();
        path = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<ImageLocal> CREATOR = new Creator<ImageLocal>() {
        @Override
        public ImageLocal createFromParcel(Parcel in) {
            return new ImageLocal(in);
        }

        @Override
        public ImageLocal[] newArray(int size) {
            return new ImageLocal[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(path);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
