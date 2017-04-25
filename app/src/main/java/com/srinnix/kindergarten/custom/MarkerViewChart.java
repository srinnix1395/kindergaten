package com.srinnix.kindergarten.custom;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.HealthTotal;

/**
 * Created by anhtu on 4/22/2017.
 */

public class MarkerViewChart extends MarkerView implements IMarker {

    private final int typeData;
    private TextView tvState;
    private MPPointF mOffset;

    public MarkerViewChart(Context context, int layoutResource, int typeData) {
        super(context, layoutResource);
        this.typeData = typeData;

        tvState = (TextView) findViewById(R.id.textview_state);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        int state;
        if (typeData == AppConstant.TYPE_WEIGHT) {
            state = ((HealthTotal) e.getData()).getWeightState();
            switch (state) {
                case AppConstant.STATE_WEIGHT_NORMAL: {
                    tvState.setText(R.string.normal);
                    break;
                }
                case AppConstant.STATE_WEIGHT_OBESE: {
                    tvState.setText(R.string.obese);
                    break;
                }
                case AppConstant.STATE_WEIGHT_MALNUTRITION: {
                    tvState.setText(R.string.malnutrition);
                    break;
                }
            }
        } else {
            state = ((HealthTotal) e.getData()).getHeightState();
            switch (state) {
                case AppConstant.STATE_HEIGHT_NORMAL: {
                    tvState.setText(R.string.normal);
                    break;
                }
                case AppConstant.STATE_HEIGHT_STUNTED: {
                    tvState.setText(R.string.stunted);
                    break;
                }
            }
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {

        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight() - 40);
        }

        return mOffset;
    }
}
