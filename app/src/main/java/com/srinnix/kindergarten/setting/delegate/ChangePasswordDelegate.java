package com.srinnix.kindergarten.setting.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;

/**
 * Created by anhtu on 5/12/2017.
 */

public interface ChangePasswordDelegate extends BaseDelegate{

    void onStartCallAPI();

    void onFinally(boolean result);
}
