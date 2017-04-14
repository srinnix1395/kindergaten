package com.srinnix.kindergarten.children.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.HealthTotalChildren;

import java.util.ArrayList;

/**
 * Created by anhtu on 2/21/2017.
 */

public interface ChildrenDelegate extends BaseDelegate {

    void onLoadListChildren(ArrayList<Child> childArrayList);

    void onLoadFail(int resError);

    void onLoadChildren(Child child);

    void onLoadChildrenTimeLine(ArrayList<HealthTotalChildren> data);
}
