package com.srinnix.kindergarten.chat.adapter.viewholder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.chat.adapter.ShowTimeListener;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.model.Message;
import com.srinnix.kindergarten.util.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DELL on 2/9/2017.
 */

public class ItemChatLeftViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imageview_chatleft_icon)
    ImageView imvIcon;

    @BindView(R.id.textview_chatleft_time)
    TextView tvTime;

    @BindView(R.id.textview_itemchatleft_message)
    TextView tvMessage;

    private ValueAnimator mAnimatorIn;
    private ValueAnimator mAnimatorOut;
    private int position;
    private ShowTimeListener mShowTimeListener;
    private int heightTimeExpand;

    public ItemChatLeftViewHolder(View itemView, ShowTimeListener mShowTimeListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        heightTimeExpand = UiUtils.dpToPixel(itemView.getContext(), 18f);

        mAnimatorIn = ValueAnimator.ofInt(0, heightTimeExpand);
        mAnimatorIn.addUpdateListener(valueAnimator1 -> {
            tvTime.getLayoutParams().height = (int) valueAnimator1.getAnimatedValue();
            tvTime.requestLayout();
        });
        mAnimatorIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                tvMessage.getBackground().setLevel(2);
            }
        });
        mAnimatorIn.setDuration(300);

        mAnimatorOut = ValueAnimator.ofInt(heightTimeExpand, 0);
        mAnimatorOut.addUpdateListener(valueAnimator1 -> {
            tvTime.getLayoutParams().height = (int) valueAnimator1.getAnimatedValue();
            tvTime.requestLayout();
        });
        mAnimatorOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                tvMessage.getBackground().setLevel(1);
            }
        });
        mAnimatorOut.setDuration(300);

        this.mShowTimeListener = mShowTimeListener;
    }

    public void bindData(Message message, int position) {
        this.position = position;

        tvMessage.setText(message.getMessage());
        tvTime.setText(UiUtils.convertDateTime(message.getCreatedAt()));

        if (message.isShowTime()) {
            mAnimatorIn.start();
        } else {
            mAnimatorOut.start();
        }

        switch (message.getLayoutType()) {
            case ChatConstant.FIRST: {
                tvMessage.setBackgroundResource(R.drawable.background_itemchatleft_first);
                break;
            }
            case ChatConstant.MIDDLE: {
                tvMessage.setBackgroundResource(R.drawable.background_itemchatleft_middle);
                break;
            }
            case ChatConstant.LAST: {
                tvMessage.setBackgroundResource(R.drawable.background_itemchatleft_last);
                break;
            }
            case ChatConstant.SINGLE: {
                tvMessage.setBackgroundResource(R.drawable.background_itemchatleft_single);
                break;
            }
        }
    }

    @OnClick(R.id.textview_itemchatleft_message)
    void onClickMessage() {
        if (mShowTimeListener != null) {
            mShowTimeListener.onClickMessage(position);
        }
    }
}
