package com.srinnix.kindergarten.custom;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.srinnix.kindergarten.util.StringUtil;

import java.util.ArrayList;

/**
 * Created by anhtu on 4/21/2017.
 */

/**
 * Created by philipp on 02/06/16.
 */
public class DayAxisValueFormatter implements IAxisValueFormatter {

    private final ArrayList<String> times;

    public DayAxisValueFormatter(ArrayList<String> times) {
        this.times = times;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if (value - Math.floor(value) > 0) {
            return "";
        }

        return StringUtil.getTimeHealthIndex(times.get((int) value));
    }
}
