package com.srinnix.kindergarten.clazz.presenter;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.clazz.delegate.ClassDelegate;
import com.srinnix.kindergarten.request.remote.ApiService;

/**
 * Created by anhtu on 2/16/2017.
 */

public class ClassPresenter extends BasePresenter {
    private ClassDelegate mDelegate;
    private ApiService mApi;

    public ClassPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        this.mDelegate = (ClassDelegate) mDelegate;
    }

    public void onClickChat() {

    }

    public void onClickTeacher(int i) {

    }
}
