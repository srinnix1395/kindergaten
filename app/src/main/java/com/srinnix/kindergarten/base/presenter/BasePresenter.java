package com.srinnix.kindergarten.base.presenter;

import android.content.Context;
import android.os.Bundle;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;

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

    public void onStart() {

    }

    public void onResume() {

    }
}
