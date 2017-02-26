package com.srinnix.kindergarten.chat.adapter.viewholder;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.chat.adapter.ChatListAdapter;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.model.Contact;
import com.srinnix.kindergarten.model.ContactTeacher;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by DELL on 2/6/2017.
 */

public class ChatViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.imageview_icon)
    CircleImageView imvIcon;

    @BindView(R.id.text_view_name)
    TextView tvName;

    @BindView(R.id.image_view_status)
    ImageView imvStatus;

    private int position;
    private int colorPrimary;

    public ChatViewHolder(View itemView, ChatListAdapter.OnClickItemChatListener onClickItemChatListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(view -> {
            if (onClickItemChatListener != null) {
                onClickItemChatListener.onClick(position);
            }
        });
        colorPrimary = ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary);
    }

    public void bindData(Contact contact, int position) {
        this.position = position;

        switch (contact.getStatus()) {
            case ChatConstant.ONLINE: {
                if (imvStatus.getVisibility() != View.VISIBLE) {
                    imvStatus.setVisibility(View.VISIBLE);
                }
                Glide.with(itemView.getContext())
                        .load(R.drawable.ic_state_online)
                        .into(imvStatus);
                break;
            }
            case ChatConstant.OFFLINE: {
                if (imvStatus.getVisibility() != View.VISIBLE) {
                    imvStatus.setVisibility(View.VISIBLE);
                }
                Glide.with(itemView.getContext())
                        .load(R.drawable.ic_state_offline)
                        .into(imvStatus);
                break;
            }
            case ChatConstant.UNDEFINED: {
                imvStatus.setVisibility(View.INVISIBLE);
                break;
            }
        }

        if (contact instanceof ContactTeacher) {
            Glide.with(itemView.getContext())
                    .load(((ContactTeacher) contact).getImage())
                    .error(R.drawable.image_teacher)
                    .into(imvIcon);
            tvName.setText(contact.getName() + " - " + ((ContactTeacher) contact).getClassName());
        } else {
            imvIcon.setImageResource(R.drawable.image_parent);
            tvName.setText(contact.getName());
        }

    }
}
