package com.srinnix.kindergarten.chat.adapter.viewholder;

import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.model.Message;

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

    private ObjectAnimator animTimeIn;
    private ObjectAnimator animTimeOut;

    public ItemChatRightViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        animTimeIn = ObjectAnimator.ofInt(tvTime, "height", 0, ViewGroup.LayoutParams.WRAP_CONTENT);
        animTimeIn.setDuration(500);

        animTimeOut = ObjectAnimator.ofInt(tvTime, "height", ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        animTimeOut.setDuration(500);
    }

    public void bindData(Message message) {
        tvMessage.setText(message.getMessage());
        tvTime.setText(String.valueOf(message.getCreatedAt()));

        if (message.isDisplayTime()) {

        } else {

        }
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
        if (tvTime.getHeight() == 0) {

        }
    }
}
