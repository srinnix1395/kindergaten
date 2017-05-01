package com.srinnix.kindergarten.bulletinboard.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.model.LikeModel;

import java.util.ArrayList;

/**
 * Created by anhtu on 4/4/2017.
 */

public interface LikeDelegate extends BaseDelegate{
    void onLoadFail(int resError, boolean isLoadFirst);

    void onLoadSuccess(ArrayList<LikeModel> data, boolean isLoadFirst);
}
