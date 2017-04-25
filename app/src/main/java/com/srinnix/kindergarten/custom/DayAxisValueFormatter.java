package com.srinnix.kindergarten.custom;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.srinnix.kindergarten.model.HealthCompact;
import com.srinnix.kindergarten.util.StringUtil;

import java.util.ArrayList;

/**
 * Created by anhtu on 4/21/2017.
 */

/**
 * Created by philipp on 02/06/16.
 */
public class DayAxisValueFormatter implements IAxisValueFormatter {

    private final ArrayList<Entry> entries;

    public DayAxisValueFormatter(ArrayList<Entry> entries) {
        this.entries = entries;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if (value - Math.floor(value) > 0) {
            return "";
        }

        String measureTime = ((HealthCompact) entries.get((int) value).getData()).getTime();
        return StringUtil.getTimeHealthIndex(measureTime);
    }
}
