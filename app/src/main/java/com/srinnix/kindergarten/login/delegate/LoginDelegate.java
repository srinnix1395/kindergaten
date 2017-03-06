package com.srinnix.kindergarten.login.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;

/**
 * Created by anhtu on 2/13/2017.
 */

public interface LoginDelegate extends BaseDelegate{
    void loginSuccessfully();

    void loginFail();
}
