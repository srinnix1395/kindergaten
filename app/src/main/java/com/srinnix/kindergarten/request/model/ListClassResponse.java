package com.srinnix.kindergarten.request.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.srinnix.kindergarten.model.Class;

import java.util.ArrayList;

/**
 * Created by anhtu on 2/21/2017.
 */

public class ListClassResponse extends BaseResponse {
    @SerializedName("data")
    @Expose
    ArrayList<Class> arrayClass;

    public ListClassResponse(ArrayList<Class> arrayClass) {
        this.arrayClass = arrayClass;
    }

    public ArrayList<Class> getArrayClass() {
        return arrayClass;
    }

    public void setArrayClass(ArrayList<Class> arrayClass) {
        this.arrayClass = arrayClass;
    }
}
