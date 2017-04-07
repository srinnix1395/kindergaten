package com.srinnix.kindergarten.chat.adapter.viewholder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.model.Message;
import com.srinnix.kindergarten.util.StringUtil;
import com.srinnix.kindergarten.util.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by DELL on 2/9/2017.
 */

public class ItemChatRightViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textview_itemchatright_message)
    TextView tvMessage;

    @BindView(R.id.textview_chatright_time)
    TextView tvTime;

    @BindView(R.id.imageview_seen)
    ImageView imvSeen;

    @BindView(R.id.imageview_heart)
    ImageView imvHeart;

    private ValueAnimator mAnimatorShow;
    private ValueAnimator mAnimatorHide;
    private boolean isShowTime;

    public ItemChatRightViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        int heightTimeExpand = UiUtils.dpToPixel(itemView.getContext(), 18f);

        mAnimatorShow = ValueAnimator.ofInt(0, heightTimeExpand);
        mAnimatorShow.addUpdateListener(valueAnimator1 -> {
            tvTime.getLayoutParams().height = (int) valueAnimator1.getAnimatedValue();
            tvTime.requestLayout();
        });
        mAnimatorShow.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (tvMessage.getVisibility() == View.VISIBLE) {
                    tvMessage.getBackground().setLevel(2);
                }
            }
        });
        mAnimatorShow.setDuration(250);

        mAnimatorHide = ValueAnimator.ofInt(heightTimeExpand, 0);
        mAnimatorHide.addUpdateListener(valueAnimator1 -> {
            tvTime.getLayoutParams().height = (int) valueAnimator1.getAnimatedValue();
            tvTime.requestLayout();
        });
        mAnimatorHide.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (tvMessage.getVisibility() == View.VISIBLE) {
                    tvMessage.getBackground().setLevel(1);
                }
            }
        });
        mAnimatorHide.setDuration(250);
    }

    public void bindData(Message message) {

        if (message.getMessage().equals(ChatConstant.ICON_HEART)) {
            tvMessage.setText("");

            tvMessage.setVisibility(View.INVISIBLE);
            imvHeart.setVisibility(View.VISIBLE);
        } else {
            tvMessage.setText(message.getMessage());

            tvMessage.setVisibility(View.VISIBLE);
            imvHeart.setVisibility(View.INVISIBLE);
        }
        tvTime.setText(StringUtil.getTimeAgoComment(itemView.getContext(), message.getCreatedAt()));

        bindStatusMessage(message.getStatus());
    }

    public void bindStatusMessage(int status) {
        switch (status) {
            case ChatConstant.PENDING: {
                imvSeen.setImageResource(R.drawable.ic_circle);
                break;
            }
            case ChatConstant.SERVER_RECEIVED: {
                imvSeen.setImageResource(R.drawable.ic_check_outline);
                break;
            }
            case ChatConstant.FRIEND_RECEIVED: {
                imvSeen.setImageResource(R.drawable.ic_check_fill);
                break;
            }
            case ChatConstant.HANDLE_COMPLETE: {
                if (imvSeen.getDrawable() != null) {
                    imvSeen.setImageDrawable(null);
                }
                break;
            }
        }
    }

    @OnClick(R.id.textview_itemchatright_message)
    void onClickMessage() {
        if (isShowTime) {
            if (!mAnimatorShow.isRunning()) {
                mAnimatorHide.start();
                isShowTime = false;
            }
        } else {
            if (!mAnimatorHide.isRunning()) {
                mAnimatorShow.start();
            }
        }
    }
}
