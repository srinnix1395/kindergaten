package com.srinnix.kindergarten.clazz.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.request.model.ClassResponse;

/**
 * Created by anhtu on 2/16/2017.
 */

public interface ClassDelegate extends BaseDelegate {
    void displayInfo(ClassResponse classInfo);
}
