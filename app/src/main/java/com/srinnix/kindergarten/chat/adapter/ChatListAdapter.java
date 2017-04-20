package com.srinnix.kindergarten.chat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.chat.adapter.payload.StatusPayload;
import com.srinnix.kindergarten.chat.adapter.viewholder.ChatViewHolder;
import com.srinnix.kindergarten.chat.adapter.viewholder.HeaderViewHolder;
import com.srinnix.kindergarten.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2/6/2017.
 */

public class ChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_CONTACT = 0;
    private static final int VIEW_TYPE_HEADER = 1;

    private ArrayList<Object> arrayList;
    private OnClickItemChatListener onClickItemChatListener;

    public ChatListAdapter(ArrayList<Object> arrayList, OnClickItemChatListener onClickItemChatListener) {
        this.arrayList = arrayList;
        this.onClickItemChatListener = onClickItemChatListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CONTACT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_list, parent
                    , false);
            return new ChatViewHolder(view, onClickItemChatListener);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_chat, parent
                , false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        int size = payloads.size();
        if (size == 0) {
            onBindViewHolder(holder, position);
            return;
        }

        if (holder instanceof ChatViewHolder) {
            if (payloads.get(size - 1) instanceof StatusPayload) {
                ((ChatViewHolder) holder).bindStatus(((StatusPayload) payloads.get(size - 1)).status);
                return;
            }

            ((ChatViewHolder) holder).bindStatus(((Contact) arrayList.get(position)).getStatus());
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChatViewHolder) {
            ((ChatViewHolder) holder).bindData((Contact) arrayList.get(position), position);
        } else {
            ((HeaderViewHolder) holder).bindData(arrayList.get(position).toString());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (arrayList.get(position) instanceof Contact) {
            return VIEW_TYPE_CONTACT;
        }

        return VIEW_TYPE_HEADER;
    }

    public interface OnClickItemChatListener {
        void onClick(int position, String name, String urlImage);
    }
}
