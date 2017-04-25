package com.srinnix.kindergarten.children.fragment;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.custom.DayAxisValueFormatter;
import com.srinnix.kindergarten.custom.MarkerViewChart;
import com.srinnix.kindergarten.model.HealthCompact;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;

/**
 * Created by anhtu on 4/17/2017.
 */

public class ChartFragment extends BaseFragment {

    @BindView(R.id.chart)
    LineChart chart;

    @BindView(R.id.toolbar_chart)
    Toolbar toolbar;

    private ArrayList<Entry> entries;
    private int type;

    @Override
    protected void getData() {
        super.getData();
        ArrayList<HealthCompact> listHealth = getArguments().getParcelableArrayList(AppConstant.KEY_HEALTH);
        type = getArguments().getInt(AppConstant.KEY_HEALTH_TYPE);

        entries = new ArrayList<>();

        if (listHealth != null) {
            int i = 0;
            for (HealthCompact health : listHealth) {
                entries.add(new Entry(i, health.getValue(), health));
                i++;
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chart_health_index;
    }

    @Override
    protected void initChildView() {
        toolbar.setTitleTextColor(Color.WHITE);
        if (type == AppConstant.TYPE_HEIGHT) {
            toolbar.setTitle("Biểu đồ " + mContext.getString(R.string.height));
        } else {
            toolbar.setTitle("Biểu đồ " + mContext.getString(R.string.weight));
        }
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });

        initChart();
    }

    private void initChart() {
        if (entries.isEmpty()) {
            return;
        }

        LineDataSet dataSet;
        if (type == AppConstant.TYPE_HEIGHT) {
            dataSet = new LineDataSet(entries, mContext.getString(R.string.height));
        } else {
            dataSet = new LineDataSet(entries, mContext.getString(R.string.weight));
        }

        dataSet.setLineWidth(1.75f);
        dataSet.setCircleRadius(3f);
        dataSet.setCircleColorHole(ContextCompat.getColor(mContext, R.color.colorPrimary));
        dataSet.setCircleColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        dataSet.setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        dataSet.setValueTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryText));
        dataSet.setValueTextSize(10);
        if (type == AppConstant.TYPE_HEIGHT) {
            dataSet.setHighLightColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        } else {
            dataSet.setHighLightColor(ContextCompat.getColor(mContext, R.color.colorOrange));
        }
        dataSet.setDrawHighlightIndicators(false);

        if (type == AppConstant.TYPE_WEIGHT) {
            dataSet.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> String.format(Locale.getDefault(), "%.1f", value));
        } else {
            dataSet.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> String.valueOf(((int) value)));
        }

        LineData lineData = new LineData(dataSet);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineWidth(1.5f);
        xAxis.setAxisLineColor(ContextCompat.getColor(mContext, R.color.colorGridChart));
        xAxis.setTextSize(9);
        xAxis.setValueFormatter(new DayAxisValueFormatter(entries));
        xAxis.setGranularity(1f);

        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setDrawAxisLine(false);
        axisLeft.setGridColor(ContextCompat.getColor(mContext, R.color.colorGridChart));
        axisLeft.setGridLineWidth(1.5f);
        axisLeft.setTextSize(9);

        YAxis axisRight = chart.getAxisRight();
        axisRight.setDrawAxisLine(false);
        axisRight.setDrawLabels(false);
        axisRight.setGridColor(ContextCompat.getColor(mContext, R.color.colorGridChart));
        axisRight.setGridLineWidth(1.5f);

        chart.setDoubleTapToZoomEnabled(false);
        chart.setScaleYEnabled(false);
        chart.setScaleXEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setMarker(new MarkerViewChart(mContext, R.layout.view_marker, type));
        chart.setNoDataTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryText));
        chart.setNoDataText("Không có dữ liệu");
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);
        chart.setData(lineData);
        chart.animateX(375, Easing.EasingOption.EaseInBack);
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
