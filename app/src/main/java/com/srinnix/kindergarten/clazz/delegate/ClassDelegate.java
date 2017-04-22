package com.srinnix.kindergarten.clazz.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.model.Image;
import com.srinnix.kindergarten.request.model.ClassResponse;

import java.util.ArrayList;

/**
 * Created by anhtu on 2/16/2017.
 */

public interface ClassDelegate extends BaseDelegate {
    void onLoadSuccess(ClassResponse classInfo);

    void onLoadError(int resError);

    void onLoadImage(ArrayList<Image> data, boolean isLoadImageFirst);
}
