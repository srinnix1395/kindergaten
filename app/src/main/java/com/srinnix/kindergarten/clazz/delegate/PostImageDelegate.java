package com.srinnix.kindergarten.clazz.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.model.Image;

import java.util.ArrayList;

/**
 * Created by anhtu on 5/11/2017.
 */

public interface PostImageDelegate extends BaseDelegate {

    void onSuccess(ArrayList<Image> data);

    void onFail();

}
