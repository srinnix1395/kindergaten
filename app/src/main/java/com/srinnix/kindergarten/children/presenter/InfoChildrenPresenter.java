package com.srinnix.kindergarten.children.presenter;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.children.delegate.InfoChildrenDelegate;

/**
 * Created by anhtu on 2/21/2017.
 */

public class InfoChildrenPresenter extends BasePresenter {
    private InfoChildrenDelegate mDelegate;

    public InfoChildrenPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        this.mDelegate = (InfoChildrenDelegate) mDelegate;
    }

    public void getInfoChildren(){

    }
}
