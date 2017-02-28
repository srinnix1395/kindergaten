package com.srinnix.kindergarten.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.chat.adapter.viewholder.ItemChatLeftViewHolder;
import com.srinnix.kindergarten.chat.adapter.viewholder.ItemChatRightViewHolder;
import com.srinnix.kindergarten.chat.adapter.viewholder.ItemChatTimeViewHolder;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.Message;
import com.srinnix.kindergarten.schoolboard.adapter.viewholder.LoadingViewHolder;
import com.srinnix.kindergarten.util.SharedPreUtils;

import java.util.ArrayList;

/**
 * Created by DELL on 2/9/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_LOADING = 0;
    private static final int ITEM_LEFT = 1;
    private static final int ITEM_RIGHT = 2;
    private static final int ITEM_TIME = 3;

    private ArrayList<Object> arrayList;
    private String currentUserID;

    public ChatAdapter(Context context, ArrayList<Object> arrayList) {
        this.arrayList = arrayList;
        currentUserID = SharedPreUtils.getInstance(context).getCurrentUserID();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_LEFT: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chat_left, parent, false);
                return new ItemChatLeftViewHolder(view);
            }
            case ITEM_RIGHT: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chat_right, parent, false);
                return new ItemChatRightViewHolder(view);
            }
            case ITEM_TIME: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_time_chat, parent, false);
                return new ItemChatTimeViewHolder(view);
            }
            case ITEM_LOADING: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_loading, parent, false);
                return new LoadingViewHolder(view, mMRetryListener);
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }

        switch (getItemViewType(position)) {
            case ITEM_LOADING:{
                ((LoadingViewHolder) holder).bindData((LoadingItem) arrayList.get(position));
                break;
            }
            case ITEM_LEFT: {
                ((ItemChatLeftViewHolder) holder).bindData((Message) arrayList.get(position));
                break;
            }
            case ITEM_RIGHT: {
                ((ItemChatRightViewHolder) holder).bindData((Message) arrayList.get(position));
                break;
            }
            case ITEM_TIME: {
                ((ItemChatTimeViewHolder) holder).bindData(arrayList.get(position).toString());
            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_LOADING;
        }

        Object object = arrayList.get(position);
        if (object instanceof Message) {
            if (((Message) object).getIdSender().equals(currentUserID)) {
                return ITEM_RIGHT;
            }

            return ITEM_LEFT;
        }

        return ITEM_TIME;
    }
}
