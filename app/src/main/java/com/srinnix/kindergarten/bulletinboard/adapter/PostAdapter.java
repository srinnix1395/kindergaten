package com.srinnix.kindergarten.bulletinboard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.bulletinboard.adapter.viewholder.LoadingViewHolder;
import com.srinnix.kindergarten.bulletinboard.adapter.viewholder.PostViewHolder;
import com.srinnix.kindergarten.bulletinboard.adapter.viewholder.PostedViewHolder;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.util.SharedPreUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by DELL on 2/11/2017.
 */

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_POST = 0;
    public static final int VIEW_TYPE_POSTED_1 = 1;
    public static final int VIEW_TYPE_POSTED_2_1 = 2;
    public static final int VIEW_TYPE_POSTED_2_2 = 3;
    public static final int VIEW_TYPE_POSTED_3_1 = 4;
    public static final int VIEW_TYPE_POSTED_3_2 = 5;
    public static final int VIEW_TYPE_POSTED_4_1 = 6;
    public static final int VIEW_TYPE_POSTED_4_2 = 7;
    public static final int VIEW_TYPE_POSTED_4_3 = 8;
    public static final int VIEW_TYPE_POSTED_5_1 = 9;
    public static final int VIEW_TYPE_POSTED_5_2 = 10;
    public static final int VIEW_TYPE_POSTED_0 = 11;

    private static final int VIEW_TYPE_LOADING = 12;

    private ArrayList<Object> arrPost;
    private int accountType;
    private RetryListener mRetryListener;
    private PostListener mPostListener;
    private Random random;

    public PostAdapter(Context context, ArrayList<Object> arrPost, RetryListener mRetryListener, PostListener mPostListener) {
        this.arrPost = arrPost;
        accountType = SharedPreUtils.getInstance(context).getAccountType();
        this.mRetryListener = mRetryListener;
        this.mPostListener = mPostListener;
        random = new Random();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_POST: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
                return new PostViewHolder(view);
            }
//            case VIEW_TYPE_POSTED_1:
//            case VIEW_TYPE_POSTED_2_1:
//            case VIEW_TYPE_POSTED_2_2:
//            case VIEW_TYPE_POSTED_3_1:
//            case VIEW_TYPE_POSTED_3_2:
//            case VIEW_TYPE_POSTED_4_1:
//            case VIEW_TYPE_POSTED_4_2:
//            case VIEW_TYPE_POSTED_4_3:
//            case VIEW_TYPE_POSTED_5_1:
            case VIEW_TYPE_POSTED_0: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_posted, parent, false);
                return new PostedViewHolder(view, mPostListener, viewType);
            }
            case VIEW_TYPE_LOADING: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
                return new LoadingViewHolder(view, mRetryListener);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
            return;
        }

        if (payloads.get(0) instanceof ArrayList) {
            ArrayList arrayListPayloads = (ArrayList) payloads.get(0);
            ((PostedViewHolder) holder).bindImageLike((Boolean) arrayListPayloads.get(0), (Integer) arrayListPayloads.get(1));
            return;
        }

        if (payloads.get(0) instanceof Integer) {
            ((PostedViewHolder) holder).bindComment((Integer) payloads.get(0));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == arrPost.size() - 1) {
            ((LoadingViewHolder) holder).bindData(((LoadingItem) arrPost.get(position)));
        } else {
            ((PostedViewHolder) holder).bindData(((Post) arrPost.get(position)), position);
        }
    }

    @Override
    public int getItemCount() {
        return arrPost.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (arrPost.get(position) instanceof Post) {
            return VIEW_TYPE_POSTED_0;
        }

        return VIEW_TYPE_LOADING;

//        if (((Post) arrPost.get(position)).getListImage() == null) {
//            return VIEW_TYPE_POSTED_0;
//        }
//        switch (((Post) arrPost.get(position)).getListImage().size()) {
//            case 0: {
//                return VIEW_TYPE_POSTED_0;
//            }
//            case 1:
//                return VIEW_TYPE_POSTED_1;
//            case 2: {
//                int i = random.nextInt(3);
//                if (i == 1) {
//                    return VIEW_TYPE_POSTED_2_1;
//                }
//                return VIEW_TYPE_POSTED_2_2;
//            }
//            case 3: {
//                int i = random.nextInt(3);
//                if (i == 1) {
//                    return VIEW_TYPE_POSTED_3_1;
//                }
//                return VIEW_TYPE_POSTED_3_2;
//            }
//            case 4: {
//                int i = random.nextInt(4);
//                if (i == 1) {
//                    return VIEW_TYPE_POSTED_4_1;
//                }
//                if (i == 2) {
//                    return VIEW_TYPE_POSTED_4_2;
//                }
//                return VIEW_TYPE_POSTED_4_3;
//            }
//            default: {
//                int i = random.nextInt(3);
//                if (i == 1) {
//                    return VIEW_TYPE_POSTED_5_1;
//                }
//                return VIEW_TYPE_POSTED_5_2;
//            }
    }

    public interface RetryListener {
        void onClickRetry();
    }

    public interface PostListener {
        void onClickLike(int position);

        void onClickNumberLike(int position);

        void onClickImage(int position);

        void onClickComment(int position, boolean isShowKeyboard);
    }
}
