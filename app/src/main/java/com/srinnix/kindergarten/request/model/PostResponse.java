package com.srinnix.kindergarten.request.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.srinnix.kindergarten.model.Post;

import java.util.ArrayList;

/**
 * Created by Administrator on 2/15/2017.
 */

public class PostResponse extends BaseResponse {

    @SerializedName("data")
    @Expose
    ArrayList<Post> mPostArrayList;

    public ArrayList<Post> getPostArrayList() {
        return mPostArrayList;
    }

    public void setPostArrayList(ArrayList<Post> postArrayList) {
        mPostArrayList = postArrayList;
    }
}
