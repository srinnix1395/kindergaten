package com.srinnix.kindergarten.base.presenter;

import android.content.Context;
import android.os.Bundle;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.login.fragment.LoginFragment;
import com.srinnix.kindergarten.util.ViewManager;

/**
 * Created by DELL on 2/3/2017.
 */

public class BasePresenter {
	protected Context mContext;
	protected BaseDelegate mDelegate;
	
	public BasePresenter(BaseDelegate mDelegate) {
		this.mDelegate = mDelegate;
	}
	
	public void setContext(Context mContext) {
		this.mContext = mContext;
	}
	
	public void getData(Bundle bundle) {
		
	}

    public void onDestroy() {

    }

    public void onStart(boolean isFirst) {

    }

    public void addFragmentLogin() {
        ViewManager.getInstance().addFragment(new LoginFragment(), null,
                R.anim.translate_right_to_left, R.anim.translate_left_to_right);
    }

    public void onHiddenChanged(boolean hidden) {

    }
}
