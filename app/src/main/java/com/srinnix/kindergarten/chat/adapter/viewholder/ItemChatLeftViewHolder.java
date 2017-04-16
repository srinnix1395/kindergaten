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

//    private final String urlImage;
//    private final int accountType;

    public ItemChatLeftViewHolder(View itemView, String urlImage, int accountType) {
        super(itemView);
        ButterKnife.bind(this, itemView);

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
        tvTime.setText(StringUtil.getTimeAgoComment(itemView.getContext(), message.getCreatedAt()));

        if (message.getMessage().equals(ChatConstant.ICON_HEART)) {
            tvMessage.setText("");

            tvMessage.setVisibility(View.GONE);
            imvHeart.setVisibility(View.VISIBLE);
        } else {
            tvMessage.setText(message.getMessage());

            tvMessage.setVisibility(View.VISIBLE);
            imvHeart.setVisibility(View.GONE);
        }

        imvTyping.setVisibility(View.GONE);

    }

    private void bindDataMessageTyping() {
        tvMessage.setText("");
        tvTime.setText("");

        imvTyping.setVisibility(View.VISIBLE);
        tvMessage.setVisibility(View.GONE);
        imvHeart.setVisibility(View.GONE);
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
        if (isShowTime) {
            UiUtils.collapse(tvTime);
            isShowTime = false;
        } else {
            UiUtils.expand(tvTime);
            isShowTime = true;
        }
    }
}
