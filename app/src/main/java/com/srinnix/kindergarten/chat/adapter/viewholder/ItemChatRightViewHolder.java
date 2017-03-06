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

public class ItemChatRightViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.textview_itemchatright_message)
    TextView tvMessage;

    @BindView(R.id.textview_chatright_time)
    TextView tvTime;

    @BindView(R.id.imageview_seen)
    ImageView imvSeen;

    private ValueAnimator mAnimatorIn;
    private ValueAnimator mAnimatorOut;
    private int position;

    private int heightTimeExpand;
    private ShowTimeListener mShowTimeListener;

    public ItemChatRightViewHolder(View itemView, ShowTimeListener mShowTimeListener) {
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

//        if (message.isShowTime()) {
//            mAnimatorIn.start();
//        } else {
//            mAnimatorOut.start();
//        }
        switch (message.getLayoutType()) {
            case ChatConstant.FIRST: {
                tvMessage.setBackgroundResource(R.drawable.background_itemchatright_first);
                break;
            }
            case ChatConstant.MIDDLE: {
                tvMessage.setBackgroundResource(R.drawable.background_itemchatright_middle);
                break;
            }
            case ChatConstant.LAST: {
                tvMessage.setBackgroundResource(R.drawable.background_itemchatright_last);
                break;
            }
            case ChatConstant.SINGLE: {
                tvMessage.setBackgroundResource(R.drawable.background_itemchatright_single);
                break;
            }
        }

        switch (message.getStatus()) {
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
        if (mShowTimeListener != null) {
            mShowTimeListener.onClickMessage(position);
        }
    }
}
