package com.srinnix.kindergarten.base.presenter;

import android.content.Context;
import android.os.Bundle;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.login.fragment.LoginFragment;
import com.srinnix.kindergarten.util.ViewManager;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by DELL on 2/3/2017.
 */

public class BasePresenter {
	protected Context mContext;
	protected BaseDelegate mDelegate;
	protected CompositeDisposable mDisposable;

	public BasePresenter(BaseDelegate mDelegate) {
        this.mDelegate = mDelegate;
        mDisposable = new CompositeDisposable();
    }
	
	public void setContext(Context mContext) {
		this.mContext = mContext;
	}
	
	public void getData(Bundle bundle) {
		
	}

    public void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }

    public void onStart() {

    }

    public void onResume() {

    }

    public void addFragmentLogin() {
        ViewManager.getInstance().addFragment(new LoginFragment(), null,
                R.anim.translate_right_to_left, R.anim.translate_left_to_right);
    }

    public void onHiddenChanged(boolean hidden) {

    }

    public CompositeDisposable getDisposable() {
        return mDisposable;
    }


}
