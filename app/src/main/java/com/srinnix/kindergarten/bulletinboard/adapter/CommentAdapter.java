package com.srinnix.kindergarten.bulletinboard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.bulletinboard.adapter.viewholder.CommentViewHolder;
import com.srinnix.kindergarten.bulletinboard.adapter.viewholder.LoadingViewHolder;
import com.srinnix.kindergarten.model.Comment;
import com.srinnix.kindergarten.model.LoadingItem;

import java.util.ArrayList;

/**
 * Created by anhtu on 3/28/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_COMMENT = 1;

    private ArrayList<Object> listComments;
    private PostAdapter.RetryListener retryListener;

    public CommentAdapter(ArrayList<Object> listComments, PostAdapter.RetryListener retryListener) {
        this.listComments = listComments;
        this.retryListener = retryListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_LOADING) {
            view = inflater.inflate(R.layout.item_loading_comment, parent, false);
            return new LoadingViewHolder(view, retryListener);
        }

        view = inflater.inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentViewHolder) {
            ((CommentViewHolder) holder).bindData(((Comment) listComments.get(position)));
        } else {
            ((LoadingViewHolder) holder).bindData((LoadingItem) listComments.get(position));
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
