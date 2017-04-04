package com.srinnix.kindergarten.bulletinboard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.bulletinboard.adapter.viewholder.CommentViewHolder;
import com.srinnix.kindergarten.bulletinboard.adapter.viewholder.Loading3StateViewHolder;
import com.srinnix.kindergarten.model.Comment;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.LoadingItem3State;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anhtu on 3/28/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_COMMENT = 1;

    private ArrayList<Object> listComments;
    private PostAdapter.RetryListener retryListener;
    private CommentViewHolder.CommentListener commentListener;

    public CommentAdapter(ArrayList<Object> listComments, PostAdapter.RetryListener retryListener,
                          CommentViewHolder.CommentListener listener) {
        this.listComments = listComments;
        this.retryListener = retryListener;
        commentListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_LOADING) {
            view = inflater.inflate(R.layout.item_loading_comment, parent, false);
            return new Loading3StateViewHolder(view, retryListener);
        }

        view = inflater.inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view, commentListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
            return;
        }

        int size = payloads.size();

        if (payloads.get(size - 1) instanceof Long) {
            ((CommentViewHolder) holder).bindTime((Long) payloads.get(0));
            return;
        }

        if (payloads.get(size - 1) instanceof Boolean) {
            ((CommentViewHolder) holder).bindStatus((Boolean) payloads.get(size - 1));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentViewHolder) {
            ((CommentViewHolder) holder).bindData(((Comment) listComments.get(position)), position);
        } else {
            ((Loading3StateViewHolder) holder).bindData((LoadingItem3State) listComments.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return listComments.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (listComments.get(position) instanceof LoadingItem) {
            return VIEW_TYPE_LOADING;
        }
        return VIEW_TYPE_COMMENT;
    }


}
