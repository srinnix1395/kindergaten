package com.srinnix.kindergarten.request.model;

import com.srinnix.kindergarten.model.Image;

import java.util.ArrayList;

/**
 * Created by anhtu on 4/6/2017.
 */

public class ImageResponse {
    private ArrayList<Image> arrayList;

    public ImageResponse(ArrayList<Image> arrayList) {
        this.arrayList = arrayList;
    }

    public ArrayList<Image> getArrayList() {
        return arrayList;
    }
}
