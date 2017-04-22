package com.srinnix.kindergarten.children.fragment;

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
import com.srinnix.kindergarten.model.Health;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by anhtu on 4/17/2017.
 */

public class ChartFragment extends BaseFragment {

    @BindView(R.id.chart_weight)
    LineChart chartWeight;

    @BindView(R.id.chart_height)
    LineChart chartHeight;

    @BindView(R.id.toolbar_chart)
    Toolbar toolbar;

    private ArrayList<Entry> entryWeight;
    private ArrayList<Entry> entryHeight;

    @Override
    protected void getData() {
        super.getData();
        ArrayList<Health> listHealth = getArguments().getParcelableArrayList(AppConstant.KEY_HEALTH);

        entryWeight = new ArrayList<>();
        entryHeight = new ArrayList<>();

        if (listHealth != null) {
            int i = 0;
            for (Health health : listHealth) {
                entryWeight.add(new Entry(i, health.getWeight(), health));
                entryHeight.add(new Entry(i, health.getHeight(), health));
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
        toolbar.setTitle("Biểu đồ");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });

        initChart(chartHeight, entryHeight, R.string.height, R.color.colorAccent, AppConstant.TYPE_HEIGHT);
        initChart(chartWeight, entryWeight, R.string.weight, R.color.colorOrange, AppConstant.TYPE_WEIGHT);
    }

    private void initChart(LineChart chart, ArrayList<Entry> entries, int resLabel,
                           int resColorHighlight, int typeData) {
        if (entries.isEmpty()) {
            return;
        }

        LineDataSet dataSet = new LineDataSet(entries, mContext.getString(resLabel));
        dataSet.setLineWidth(1.75f);
        dataSet.setCircleRadius(3f);
        dataSet.setCircleColorHole(ContextCompat.getColor(mContext, R.color.colorPrimary));
        dataSet.setCircleColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        dataSet.setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        dataSet.setValueTextColor(ContextCompat.getColor(mContext, R.color.colorOrange));
        dataSet.setValueTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryText));
        dataSet.setValueTextSize(10);
        dataSet.setHighLightColor(ContextCompat.getColor(mContext, resColorHighlight));
        dataSet.setDrawHighlightIndicators(false);

        LineData lineData = new LineData(dataSet);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineWidth(1.5f);
        xAxis.setAxisLineColor(ContextCompat.getColor(mContext, R.color.colorGridChart));
        xAxis.setTextSize(9);
        xAxis.setValueFormatter(new DayAxisValueFormatter(entries));

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
        chart.setMarker(new MarkerViewChart(mContext, R.layout.view_marker, typeData));
        chart.setNoDataTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryText));
        chart.setNoDataText("Không có dữ liệu");
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);
        chart.setData(lineData);
        chart.animateX(375, Easing.EasingOption.EaseInBack);

        chart.setVisibleXRangeMaximum(6);
//        chart.moveViewToX(entries.size() - 1);
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
