package com.srinnix.kindergarten.clazz.presenter;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.clazz.delegate.ClassListDelegate;

/**
 * Created by Administrator on 2/21/2017.
 */

public class ClassListPresenter extends BasePresenter{
    private ClassListDelegate mDelegate;

    public ClassListPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        this.mDelegate = (ClassListDelegate) mDelegate;
    }


}
