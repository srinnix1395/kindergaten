package com.srinnix.kindergarten.setting.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.User;

import java.util.ArrayList;

/**
 * Created by anhtu on 5/11/2017.
 */

public interface AccountDelegate extends BaseDelegate{
    void onFail(int resError);

    void onSuccess(User response, ArrayList<Child> children);
}
