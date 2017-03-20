package com.srinnix.kindergarten.chat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.bulletinboard.adapter.PostAdapter;
import com.srinnix.kindergarten.bulletinboard.adapter.viewholder.LoadingViewHolder;
import com.srinnix.kindergarten.chat.adapter.payload.ImagePayload;
import com.srinnix.kindergarten.chat.adapter.payload.StatusMessagePayload;
import com.srinnix.kindergarten.chat.adapter.viewholder.ItemChatLeftViewHolder;
import com.srinnix.kindergarten.chat.adapter.viewholder.ItemChatRightViewHolder;
import com.srinnix.kindergarten.chat.adapter.viewholder.ItemChatTimeViewHolder;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.Message;
import com.srinnix.kindergarten.util.SharedPreUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2/9/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ShowTimeListener {

    private static final int ITEM_LOADING = 0;
    private static final int ITEM_LEFT = 1;
    private static final int ITEM_RIGHT = 2;
    private static final int ITEM_TIME = 3;

    private final Context context;
    private ArrayList<Object> arrayList;
    private PostAdapter.RetryListener mRetryListener;
    private int positionShowTime = -1;

    private final String currentUserID;
    private final String urlImage;
    private final int accountType;

    public ChatAdapter(Context context, ArrayList<Object> arrayList, String urlImage
            , int accountType, PostAdapter.RetryListener mRetryListener) {
        this.context = context;
        this.arrayList = arrayList;
        currentUserID = SharedPreUtils.getInstance(context).getUserID();
        this.urlImage = urlImage;
        this.accountType = accountType;
        this.mRetryListener = mRetryListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_LEFT: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chat_left, parent, false);
                return new ItemChatLeftViewHolder(view, urlImage, accountType, this);
            }
            case ITEM_RIGHT: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_chat_right, parent, false);
                return new ItemChatRightViewHolder(view, this);
            }
            case ITEM_TIME: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_time_chat, parent, false);
                return new ItemChatTimeViewHolder(view);
            }
            case ITEM_LOADING: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_loading, parent, false);
                return new LoadingViewHolder(view, mRetryListener);
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
            return;
        }

        if (holder instanceof ItemChatLeftViewHolder && payloads.get(0) instanceof ImagePayload) {
            ((ItemChatLeftViewHolder) holder).bindImage(((ImagePayload) payloads.get(0)).isDisplayIcon);
            return;
        }

        if (holder instanceof ItemChatRightViewHolder && payloads.get(0) instanceof StatusMessagePayload) {
            ((ItemChatRightViewHolder) holder).bindStatusMessage(((StatusMessagePayload) payloads.get(0)).status);
            return;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder == null) {
            return;
        }

        switch (getItemViewType(position)) {
            case ITEM_LOADING: {
                ((LoadingViewHolder) holder).bindData((LoadingItem) arrayList.get(position));
                break;
            }
            case ITEM_LEFT: {
                ((ItemChatLeftViewHolder) holder).bindData((Message) arrayList.get(position), position);
                break;
            }
            case ITEM_RIGHT: {
                ((ItemChatRightViewHolder) holder).bindData((Message) arrayList.get(position), position);
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
        Object object = arrayList.get(position);

        if (object instanceof LoadingItem) {
            return ITEM_LOADING;
        }

        if (object instanceof Message) {
            if (((Message) object).getIdSender().equals(currentUserID)) {
                return ITEM_RIGHT;
            }

            return ITEM_LEFT;
        }

        return ITEM_TIME;
    }

    @Override
    public void onClickMessage(int position) {
//        if (positionShowTime == -1) {
//            ((Message) arrayList.get(position)).setShowTime(true);
//            notifyItemChanged(position);
//
//            positionShowTime = position;
//            return;
//        }
//
//        if (positionShowTime == position) {
//            ((Message) arrayList.get(positionShowTime)).setShowTime(false);
//            notifyItemChanged(positionShowTime);
//
//            positionShowTime = -1;
//            return;
//        }
//
//        ((Message) arrayList.get(positionShowTime)).setShowTime(false);
//        notifyItemChanged(positionShowTime);
//        new Handler().postDelayed(() -> {
//            ((Message) arrayList.get(position)).setShowTime(true);
//            notifyItemChanged(position);
//        }, 300);
//
//        positionShowTime = position;
    }


}
