package com.srinnix.kindergarten.children.presenter;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.children.delegate.InfoChildrenDelegate;
import com.srinnix.kindergarten.children.helper.InfoChildrenHelper;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by anhtu on 2/21/2017.
 */

public class InfoChildrenPresenter extends BasePresenter {
    private CompositeDisposable mDisposable;
    private InfoChildrenDelegate mDelegate;
    private InfoChildrenHelper mHelper;

    public InfoChildrenPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        this.mDelegate = (InfoChildrenDelegate) mDelegate;
        mDisposable = new CompositeDisposable();
        mHelper = new InfoChildrenHelper(mDisposable);
    }

    public void getInfoChildren(){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }
}
