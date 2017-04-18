package com.srinnix.kindergarten.children.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Health;
import com.srinnix.kindergarten.util.UiUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by anhtu on 3/8/2017.
 */

public class HealthChildrenViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.textview_time)
    TextView tvTime;

    @BindView(R.id.textview_weight)
    TextView tvWeight;

    @BindView(R.id.layout_weight)
    RelativeLayout layoutWeight;

    @BindView(R.id.layout_height)
    RelativeLayout layoutHeight;

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

    @BindView(R.id.view_time_line)
    View viewTimeline;

    @BindView(R.id.textview_result_content)
    TextView tvResult;

    @BindView(R.id.textview_eyes_title)
    TextView tvEyesTitle;

    @BindView(R.id.textview_eyes_content)
    TextView tvEyes;

    @BindView(R.id.textview_ent_title)
    TextView tvEntTitle;

    @BindView(R.id.textview_ent_content)
    TextView tvEnt;

    @BindView(R.id.textview_tooth_title)
    TextView tvToothTitle;

    @BindView(R.id.textview_tooth_content)
    TextView tvTooth;

    @BindView(R.id.textview_others_title)
    TextView tvOtherTitle;

    @BindView(R.id.textview_others_content)
    TextView tvOthers;

    private OnClickViewHolderListener listener;
    private int position;
    private boolean isDisplayHealthContent;
    private boolean canExpand;

    public HealthChildrenViewHolder(View itemView, OnClickViewHolderListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
    }

    public void setHeightViewTimeLine() {
        itemView.post(() -> {
            viewTimeline.getLayoutParams().height = itemView.getHeight();
            viewTimeline.requestLayout();
        });
    }

    public void bindData(Health health, int position) {
        this.position = position;

        tvTime.setText(health.getMeasureTime());

        if (health.getWeight() == AppConstant.UNSPECIFIED ||
                health.getHeight() == AppConstant.UNSPECIFIED) {
            layoutWeight.setVisibility(View.GONE);
            layoutHeight.setVisibility(View.GONE);
        } else {
            layoutWeight.setVisibility(View.VISIBLE);

            tvWeight.setText(String.format(Locale.getDefault(), "%.1f kg", health.getWeight()));
            if (health.getWeightState() == AppConstant.STATE_WEIGHT_NORMAL) {
                tvWeightState.setVisibility(View.GONE);
            } else {
                tvWeightState.setVisibility(View.VISIBLE);
                tvWeightState.setText(health.getWeightState() == AppConstant.STATE_WEIGHT_OBESE ?
                        R.string.obese : R.string.malnutrition);
            }

            layoutHeight.setVisibility(View.VISIBLE);

            tvHeight.setText(String.format(Locale.getDefault(), "%d cm", health.getHeight()));
            if (health.getHeightState() == AppConstant.STATE_HEIGHT_NORMAL) {
                tvHeightState.setVisibility(View.GONE);
            } else {
                tvHeightState.setVisibility(View.VISIBLE);
                tvHeightState.setText(R.string.stunted);
            }
        }

        if (health.getResult() == AppConstant.UNSPECIFIED) {
            layoutHealth.setVisibility(View.GONE);
        } else {
            layoutHealth.setVisibility(View.VISIBLE);
            tvResult.setText(String.format(Locale.getDefault(), itemView.getContext().getString(R.string.type_result)
                    , health.getResult()));

            if (health.getEyes() != null) {
                tvEyes.setText(health.getEyes());

                tvEyes.setVisibility(View.VISIBLE);
                tvEyesTitle.setVisibility(View.VISIBLE);
            } else {
                tvEyes.setVisibility(View.GONE);
                tvEyesTitle.setVisibility(View.GONE);
            }

            if (health.getEnt() != null) {
                tvEnt.setText(health.getEnt());

                tvEnt.setVisibility(View.VISIBLE);
                tvEntTitle.setVisibility(View.VISIBLE);
            } else {
                tvEnt.setVisibility(View.GONE);
                tvEntTitle.setVisibility(View.GONE);
            }

            if (health.getTooth() != null) {
                tvTooth.setText(health.getTooth());

                tvTooth.setVisibility(View.VISIBLE);
                tvToothTitle.setVisibility(View.VISIBLE);
            } else {
                tvTooth.setVisibility(View.GONE);
                tvToothTitle.setVisibility(View.GONE);
            }

            if (health.getOthers() != null) {
                tvOthers.setText(health.getOthers());

                tvOthers.setVisibility(View.VISIBLE);
                tvOtherTitle.setVisibility(View.VISIBLE);
            } else {
                tvOthers.setVisibility(View.GONE);
                tvOtherTitle.setVisibility(View.GONE);
            }
        }

        if (health.getResult() != AppConstant.UNSPECIFIED && health.getWeight() == AppConstant.UNSPECIFIED) {
            imvShowMore.setVisibility(View.GONE);
            layoutHealthContent.setVisibility(View.VISIBLE);
            canExpand = false;
        } else {
            imvShowMore.setVisibility(View.VISIBLE);
            layoutHealthContent.setVisibility(View.GONE);
            canExpand = true;
        }

        bindViewDivider(health.isDisplayLine());
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
        if (!canExpand) {
            return;
        }

        if (isDisplayHealthContent) {
            isDisplayHealthContent = false;
            UiUtils.collapse(layoutHealthContent, viewTimeline);
            imvShowMore.setImageLevel(1);
        } else {
            isDisplayHealthContent = true;
            UiUtils.expand(layoutHealthContent, viewTimeline);
            imvShowMore.setImageLevel(2);
        }
    }


    public interface OnClickViewHolderListener {
        void onClickIndex(int position);
    }
}
