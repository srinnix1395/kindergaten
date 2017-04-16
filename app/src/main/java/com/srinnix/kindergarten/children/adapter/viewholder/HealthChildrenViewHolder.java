package com.srinnix.kindergarten.children.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.HealthTotalChildren;
import com.srinnix.kindergarten.util.UiUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by anhtu on 3/8/2017.
 */

public class HealthChildrenViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imageview_mark)
    ImageView imvMark;

    @BindView(R.id.textview_time)
    TextView tvTime;

    @BindView(R.id.textview_weight)
    TextView tvWeight;

    @BindView(R.id.textview_height)
    TextView tvHeight;

    @BindView(R.id.layout_health)
    LinearLayout layoutHealth;

    @BindView(R.id.textview_weight_state)
    TextView tvWeightState;

    @BindView(R.id.textview_height_state)
    TextView tvHeightState;

    @BindView(R.id.view_divider)
    View viewDivider;

    @BindView(R.id.layout_health_content)
    RelativeLayout layoutHealthContent;

    @BindView(R.id.imageview_show_more)
    ImageView imvShowMore;

    private OnClickViewHolderListener listener;
    private int position;
    private boolean isDisplayHealthContent;

    public HealthChildrenViewHolder(View itemView, OnClickViewHolderListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
    }

    public void bindData(HealthTotalChildren healthTotalChildren, int position) {
        this.position = position;

        tvTime.setText(healthTotalChildren.getMeasureTime());

        tvWeight.setText(String.format(Locale.getDefault(), "%.1f kg", healthTotalChildren.getWeight()));
        if (healthTotalChildren.getWeightState() == AppConstant.STATE_WEIGHT_NORMAL) {
            tvWeightState.setVisibility(View.GONE);
        } else {
            tvWeightState.setVisibility(View.VISIBLE);
            tvWeightState.setText(healthTotalChildren.getWeightState() == AppConstant.STATE_WEIGHT_OBESE ?
                    R.string.obese : R.string.malnutrition);

        }

        tvHeight.setText(String.format(Locale.getDefault(), "%d cm", healthTotalChildren.getHeight()));
        if (healthTotalChildren.getHeightState() == AppConstant.STATE_HEIGHT_NORMAL) {
            tvHeightState.setVisibility(View.GONE);
        } else {
            tvHeightState.setVisibility(View.VISIBLE);
            tvHeightState.setText(R.string.stunted);

        }

//        if (healthTotalChildren.getHealth() == null) {
//            layoutHealth.setVisibility(View.GONE);
//        } else {
//            layoutHealth.setVisibility(View.VISIBLE);
//        }

        bindViewDivider(healthTotalChildren.isDisplayLine());
    }

    public void bindViewDivider(Boolean isShowDivider) {
        if (isShowDivider) {
            viewDivider.setVisibility(View.VISIBLE);
        } else {
            viewDivider.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.layout_weight, R.id.layout_height})
    public void onClickIndex() {
        if (listener != null) {
            listener.onClickIndex(position);
        }
    }

    @OnClick(R.id.layout_health)
    public void onClickHealth() {
        if (listener != null) {
            listener.onClickHealth(position);
        }

        if (isDisplayHealthContent) {
            isDisplayHealthContent = false;
            UiUtils.collapse(layoutHealthContent);
            imvShowMore.setImageLevel(1);
        } else {
            UiUtils.expand(layoutHealthContent);
            isDisplayHealthContent = true;
            imvShowMore.setImageLevel(2);
        }
    }

    public interface OnClickViewHolderListener {
        void onClickIndex(int position);

        void onClickHealth(int position);
    }
}
