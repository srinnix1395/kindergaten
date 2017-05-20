package com.srinnix.kindergarten.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by anhtu on 4/5/2017.
 */

public class Image implements Parcelable {
    @SerializedName("caption")
    @Expose
    private String caption;

    @SerializedName("created_at")
    @Expose
    private long createdAt;

    @SerializedName("url")
    @Expose
    private String url;

    private String urlThumbnail;

    @SerializedName("is_video")
    @Expose
    private boolean isVideo;

    public Image(Parcel in) {
        caption = in.readString();
        createdAt = in.readLong();
        url = in.readString();
        urlThumbnail = in.readString();
        isVideo = in.readByte() != 0;
    }

    public Image(String caption, long createdAt, String url, String urlThumbnail, boolean isVideo) {
        this.caption = caption;
        this.createdAt = createdAt;
        this.url = url;
        this.urlThumbnail = urlThumbnail;
        this.isVideo = isVideo;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(caption);
        dest.writeLong(createdAt);
        dest.writeString(url);
        dest.writeString(urlThumbnail);
        dest.writeByte((byte) (isVideo ? 1 : 0));
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public String getCaption() {
        return caption;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getUrl() {
        return url;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public String getThumbnailUrl() {
        return urlThumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
