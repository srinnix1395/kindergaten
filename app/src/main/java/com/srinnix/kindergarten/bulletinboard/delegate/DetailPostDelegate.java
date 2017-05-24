package com.srinnix.kindergarten.bulletinboard.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.request.model.LikeResponse;

/**
 * Created by anhtu on 5/2/2017.
 */

public interface DetailPostDelegate extends BaseDelegate{
    void onSuccess(Post data);

    void onFail(int resError);

    void likeSuccess(Post post, LikeResponse data);
}
