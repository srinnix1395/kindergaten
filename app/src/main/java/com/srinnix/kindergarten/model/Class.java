package com.srinnix.kindergarten.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2/21/2017.
 */

public class Class implements Parcelable {

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("number_member")
    @Expose
    private int numberMember;

    @SerializedName("_id_school")
    @Expose
    private String idSchool;

    protected Class(Parcel in) {
        id = in.readString();
        name = in.readString();
        numberMember = in.readInt();
        idSchool = in.readString();
    }

    public static final Creator<Class> CREATOR = new Creator<Class>() {
        @Override
        public Class createFromParcel(Parcel in) {
            return new Class(in);
        }

        @Override
        public Class[] newArray(int size) {
            return new Class[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumberMember() {
        return numberMember;
    }

    public String getIdSchool() {
        return idSchool;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeInt(numberMember);
        dest.writeString(idSchool);
    }
}
