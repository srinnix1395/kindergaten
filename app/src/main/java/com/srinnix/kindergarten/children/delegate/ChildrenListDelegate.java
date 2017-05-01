package com.srinnix.kindergarten.children.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.model.Child;

import java.util.ArrayList;

/**
 * Created by anhtu on 4/20/2017.
 */

public interface ChildrenListDelegate extends BaseDelegate {

    void onLoadListChildren(ArrayList<Child> childArrayList);

    void onLoadFail(int resError);
}
