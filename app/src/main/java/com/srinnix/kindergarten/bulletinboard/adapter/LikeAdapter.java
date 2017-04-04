package com.srinnix.kindergarten.bulletinboard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.bulletinboard.adapter.viewholder.LikeViewHolder;
import com.srinnix.kindergarten.bulletinboard.adapter.viewholder.Loading3StateViewHolder;
import com.srinnix.kindergarten.model.LikeModel;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.LoadingItem3State;

import java.util.ArrayList;

/**
 * Created by anhtu on 4/4/2017.
 */

public class LikeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_LIKE = 1;

    private ArrayList<Object> listLike;
    private PostAdapter.RetryListener retryListener;

    public LikeAdapter(ArrayList<Object> listLike, PostAdapter.RetryListener retryListener) {
        this.listLike = listLike;
        this.retryListener = retryListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_LOADING) {
            view = inflater.inflate(R.layout.item_loading_comment, parent, false);
            return new Loading3StateViewHolder(view, retryListener);
        }

        view = inflater.inflate(R.layout.item_like, parent, false);
        return new LikeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LikeViewHolder) {
            ((LikeViewHolder) holder).bindData(((LikeModel) listLike.get(position)));
        } else {
            ((Loading3StateViewHolder) holder).bindData((LoadingItem3State) listLike.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return listLike.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (listLike.get(position) instanceof LoadingItem) {
            return VIEW_TYPE_LOADING;
        }
        return VIEW_TYPE_LIKE;
    }
}
