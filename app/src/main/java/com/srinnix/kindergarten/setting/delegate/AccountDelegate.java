package com.srinnix.kindergarten.setting.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.User;

import java.util.ArrayList;

/**
 * Created by anhtu on 5/11/2017.
 */

public interface AccountDelegate extends BaseDelegate{
    void onFailGetData(int resError);

    void onSuccessGetData(User response, ArrayList<Child> children);

    void onStartCallAPIUpdateData();

    void onFailUpdateData();

    void onSuccessUpdateData(User data);

    void backToStateView();
}
