package com.srinnix.kindergarten.chat.adapter.viewholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

    @BindView(R.id.imageview_image)
    ImageView imvImage;

    @BindView(R.id.cardview_image)
    CardView cardViewImage;

    private boolean isShowTime;
    private final ItemChatLeftViewHolder.AdapterListener adapterListener;

    public ItemChatRightViewHolder(View itemView, ItemChatLeftViewHolder.AdapterListener chatAdapter) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.adapterListener = chatAdapter;
    }

    public void bindData(Message message) {
        tvTime.setText(StringUtil.getTimeAgoMessage(message.getCreatedAt()));

        switch (message.getMessageType()) {
            case ChatConstant.MSG_TYPE_TEXT: {
                tvMessage.setText(message.getMessage());

                UiUtils.showView(tvMessage);
                UiUtils.hideView(imvHeart);
                UiUtils.hideView(cardViewImage);
                imvImage.setImageDrawable(null);
                break;
            }
            case ChatConstant.MSG_TYPE_ICON_HEART: {
                tvMessage.setText("");

                tvMessage.setVisibility(View.INVISIBLE);
                UiUtils.showView(imvHeart);
                UiUtils.hideView(cardViewImage);
                imvImage.setImageDrawable(null);
                break;
            }
            case ChatConstant.MSG_TYPE_MEDIA: {
                tvMessage.setText("");
                tvMessage.setVisibility(View.INVISIBLE);
                UiUtils.hideView(imvHeart);

                UiUtils.showView(cardViewImage);
                Glide.with(itemView.getContext())
                        .load(message.getMessage())
                        .thumbnail(0.5f)
                        .placeholder(R.drawable.dummy_image)
                        .error(R.drawable.dummy_image)
                        .into(imvImage);
                break;
            }
        }

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

    @OnClick({R.id.textview_itemchatright_message, R.id.cardview_image, R.id.imageview_heart})
    void onClickMessage() {
        if (adapterListener != null && !adapterListener.isValidToShowTime(getAdapterPosition())) {
            return;
        }
        if (isShowTime) {
            UiUtils.collapse(tvTime);
            isShowTime = false;
        } else {
            UiUtils.expand(tvTime);
            isShowTime = true;

        }
    }
}
