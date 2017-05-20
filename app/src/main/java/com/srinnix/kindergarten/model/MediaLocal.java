package com.srinnix.kindergarten.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anhtu on 4/25/2017.
 */

public class MediaLocal implements Parcelable {
    private long id;
    private String name;
    private String path;
    private boolean isGIF;

    private boolean isVideo;
    private int duration;
    private String urlThumbnail;
    private int size;

    private boolean isSelected = false;

    public MediaLocal(long id, String name, String path, boolean isGIF) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.isVideo = false;
        this.isGIF = isGIF;
    }

    public MediaLocal(long id, String name, String path, boolean isVideo, int duration, String urlThumbnail, int size) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.isVideo = isVideo;
        this.duration = duration;
        this.urlThumbnail = urlThumbnail;
        this.size = size;
    }

    protected MediaLocal(Parcel in) {
        id = in.readLong();
        name = in.readString();
        path = in.readString();
        isGIF = in.readByte() != 0;
        isVideo = in.readByte() != 0;
        duration = in.readInt();
        urlThumbnail = in.readString();
        size = in.readInt();
        isSelected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(path);
        dest.writeByte((byte) (isGIF ? 1 : 0));
        dest.writeByte((byte) (isVideo ? 1 : 0));
        dest.writeInt(duration);
        dest.writeString(urlThumbnail);
        dest.writeInt(size);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MediaLocal> CREATOR = new Creator<MediaLocal>() {
        @Override
        public MediaLocal createFromParcel(Parcel in) {
            return new MediaLocal(in);
        }

        @Override
        public MediaLocal[] newArray(int size) {
            return new MediaLocal[size];
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

    public boolean isGIF() {
        return isGIF;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public int getDuration() {
        return duration;
    }

    public String getUrlThumbnail() {
        return urlThumbnail;
    }

    public int getSize() {
        return size;
    }
}
