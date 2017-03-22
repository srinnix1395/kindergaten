package com.srinnix.kindergarten.clazz.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.model.Class;

import java.util.ArrayList;

/**
 * Created by Administrator on 2/21/2017.
 */

public interface ClassListDelegate extends BaseDelegate {
    void onLoadSuccess(ArrayList<Class> arrayList);

    void onLoadError(int resError);

}
