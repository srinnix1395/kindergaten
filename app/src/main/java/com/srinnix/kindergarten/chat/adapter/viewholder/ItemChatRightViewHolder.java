package com.srinnix.kindergarten.chat.adapter.viewholder;

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

    private boolean isShowTime;

    public ItemChatRightViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
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
            UiUtils.collapse(tvTime);
            isShowTime = false;
        } else {
            UiUtils.expand(tvTime);
            isShowTime = true;

        }
    }
}
