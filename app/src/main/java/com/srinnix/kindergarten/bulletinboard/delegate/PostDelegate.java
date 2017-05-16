package com.srinnix.kindergarten.bulletinboard.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.request.model.PostResponse;

/**
 * Created by anhtu on 4/25/2017.
 */

public interface PostDelegate extends BaseDelegate{
    void onFail();

    void onSuccess(PostResponse data);
}
