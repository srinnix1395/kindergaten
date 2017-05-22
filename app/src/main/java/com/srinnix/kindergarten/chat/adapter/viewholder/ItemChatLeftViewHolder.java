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

public class ItemChatLeftViewHolder extends RecyclerView.ViewHolder {

//    @BindView(R.id.imageview_chatleft_icon)
//    ImageView imvIcon;

    @BindView(R.id.textview_chatleft_time)
    TextView tvTime;

    @BindView(R.id.textview_itemchatleft_message)
    TextView tvMessage;

    @BindView(R.id.imageview_typing)
    ImageView imvTyping;

//    @BindView(R.id.cardview_icon)
//    CardView cardViewIcon;

    @BindView(R.id.imageview_heart)
    ImageView imvHeart;

    private boolean isShowTime;
    private final AdapterListener adapterListener;

//    private final String urlImage;
//    private final int accountType;

    public ItemChatLeftViewHolder(View itemView, String urlImage, int accountType,
                                  AdapterListener adapterListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.adapterListener = adapterListener;

//        this.urlImage = urlImage;
//        this.accountType = accountType;
    }

    public void bindData(Message message) {

//        bindImage(message.isDisplayIcon());

        if (message.isTypingMessage()) {
            bindDataMessageTyping();
        } else {
            bindDataMessage(message);
        }
    }

    private void bindDataMessage(Message message) {
        tvTime.setText(StringUtil.getTimeAgoMessage(message.getCreatedAt()));
        UiUtils.hideView(imvTyping);

        switch (message.getMessageType()) {
            case ChatConstant.MSG_TYPE_TEXT: {
                tvMessage.setText(message.getMessage());

                UiUtils.showView(tvMessage);
                UiUtils.hideView(imvHeart);
                break;
            }
            case ChatConstant.MSG_TYPE_ICON_HEART: {
                tvMessage.setText("");

                UiUtils.hideView(tvMessage);
                UiUtils.showView(imvHeart);
                break;
            }
            case ChatConstant.MSG_TYPE_MEDIA: {

                break;
            }
        }
    }

    private void bindDataMessageTyping() {
        tvMessage.setText("");
        tvTime.setText("");

        UiUtils.showView(imvTyping);
        UiUtils.hideView(tvMessage);
        UiUtils.hideView(imvHeart);
    }

//    public void bindImage(boolean isDisplayIcon) {
//        if (isDisplayIcon) {
//            cardViewIcon.setVisibility(View.VISIBLE);
//            if (accountType == AppConstant.ACCOUNT_PARENTS) {
//                Glide.with(itemView.getContext())
//                        .load(urlImage)
//                        .placeholder(R.drawable.dummy_image)
//                        .error(R.drawable.image_parent)
//                        .into(imvIcon);
//            } else {
//                Glide.with(itemView.getContext())
//                        .load(urlImage)
//                        .placeholder(R.drawable.dummy_image)
//                        .error(R.drawable.image_teacher)
//                        .into(imvIcon);
//            }
//        } else {
//            cardViewIcon.setVisibility(View.INVISIBLE);
//            imvIcon.setImageDrawable(null);
//        }
//    }

    @OnClick(R.id.textview_itemchatleft_message)
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

    public interface AdapterListener {

        boolean isValidToShowTime(int position);
    }
}
