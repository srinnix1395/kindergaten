package com.srinnix.kindergarten.chat.adapter.viewholder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.chat.adapter.ShowTimeListener;
import com.srinnix.kindergarten.constant.AppConstant;
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

    @BindView(R.id.imageview_typing)
    ImageView imvTyping;

    @BindView(R.id.cardview_icon)
    CardView cardViewIcon;

    private ValueAnimator mAnimatorIn;
    private ValueAnimator mAnimatorOut;
    private int position;
    private ShowTimeListener mShowTimeListener;
    private int heightTimeExpand;

    private final String urlImage;
    private final int accountType;

    public ItemChatLeftViewHolder(View itemView, String urlImage, int accountType, ShowTimeListener mShowTimeListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.urlImage = urlImage;
        this.accountType = accountType;

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

        bindImage(message.isDisplayIcon());


        if (message.isTypingMessage()) {
            bindDataMessageTyping();
        } else {
            bindDataMessage(message);
        }


//        if (message.isShowTime()) {
//            mAnimatorIn.start();
//        } else {
//            mAnimatorOut.start();
//        }


    }

    private void bindDataMessage(Message message) {
        tvMessage.setText(message.getMessage());
        tvTime.setText(UiUtils.convertDateTime(message.getCreatedAt()));

        imvTyping.setVisibility(View.GONE);
        tvMessage.setVisibility(View.VISIBLE);
    }

    private void bindDataMessageTyping() {
        tvMessage.setText("");
        tvTime.setText("");

        imvTyping.setVisibility(View.VISIBLE);
        tvMessage.setVisibility(View.GONE);
    }

    public void bindImage(boolean isDisplayIcon) {
        if (isDisplayIcon) {
            cardViewIcon.setVisibility(View.VISIBLE);
            if (accountType == AppConstant.ACCOUNT_PARENTS) {
                Glide.with(itemView.getContext())
                        .load(urlImage)
                        .placeholder(R.drawable.dummy_image)
                        .error(R.drawable.image_parent)
                        .into(imvIcon);
            } else {
                Glide.with(itemView.getContext())
                        .load(urlImage)
                        .placeholder(R.drawable.dummy_image)
                        .error(R.drawable.image_teacher)
                        .into(imvIcon);
            }
        } else {
            cardViewIcon.setVisibility(View.INVISIBLE);
            imvIcon.setImageDrawable(null);
        }
    }

    @OnClick(R.id.textview_itemchatleft_message)
    void onClickMessage() {
        if (mShowTimeListener != null) {
            mShowTimeListener.onClickMessage(position);
        }
    }
}
