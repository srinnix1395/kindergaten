package com.srinnix.kindergarten.model;

import io.realm.RealmObject;

/**
 * Created by anhtu on 4/27/2017.
 */

public class StandardHealth extends RealmObject{
    private int month;

    private float malnutrition;
    private float normal;
    private float obese;

    public StandardHealth(int month, float malnutrition, float normal, float obese) {
        this.month = month;
        this.malnutrition = malnutrition;
        this.normal = normal;
        this.obese = obese;
    }

    public int getMonth() {
        return month;
    }

    public float getMalnutrition() {
        return malnutrition;
    }

    public float getObese() {
        return obese;
    }

    public float getNormal() {
        return normal;
    }
}
