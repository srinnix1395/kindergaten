package com.srinnix.kindergarten.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DELL on 2/9/2017.
 */

public class Post implements Parcelable{
    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("image")
    @Expose
    private ArrayList<Image> listImage;

    @SerializedName("notification_type")
    @Expose
    private int type;

    @SerializedName("created_at")
    @Expose
    private long createdAt;

    @SerializedName("number_of_likes")
    @Expose
    private int numberOfLikes;

    @SerializedName("number_of_comments")
    @Expose
    private int numberOfComments;

    private boolean isUserLike;

    protected Post(Parcel in) {
        id = in.readString();
        content = in.readString();
        listImage = in.createTypedArrayList(Image.CREATOR);
        type = in.readInt();
        createdAt = in.readLong();
        numberOfLikes = in.readInt();
        numberOfComments = in.readInt();
        isUserLike = in.readByte() != 0;
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public ArrayList<Image> getListImage() {
        return listImage;
    }

    public int getType() {
        return type;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public boolean isUserLike() {
        return isUserLike;
    }

    public void setUserLike(boolean userLike) {
        isUserLike = userLike;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(content);
        dest.writeTypedList(listImage);
        dest.writeInt(type);
        dest.writeLong(createdAt);
        dest.writeInt(numberOfLikes);
        dest.writeInt(numberOfComments);
        dest.writeByte((byte) (isUserLike ? 1 : 0));
    }
}
