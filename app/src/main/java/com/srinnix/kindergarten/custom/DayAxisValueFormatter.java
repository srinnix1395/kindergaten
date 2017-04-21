package com.srinnix.kindergarten.custom;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.srinnix.kindergarten.model.Health;

import java.util.ArrayList;

/**
 * Created by anhtu on 4/21/2017.
 */

/**
 * Created by philipp on 02/06/16.
 */
public class DayAxisValueFormatter implements IAxisValueFormatter {

    private final ArrayList<Entry> entryWeight;

    public DayAxisValueFormatter(ArrayList<Entry> entryWeight) {

        this.entryWeight = entryWeight;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        String measureTime = ((Health) entryWeight.get((int) value).getData()).getMeasureTime();

        String[] split = measureTime.split("/");

        return "Thg " + split[1];
    }
}
