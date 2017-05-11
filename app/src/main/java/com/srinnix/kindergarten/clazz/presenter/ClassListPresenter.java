package com.srinnix.kindergarten.clazz.presenter;

import android.os.Bundle;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.callback.ResponseListener;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.clazz.delegate.ClassListDelegate;
import com.srinnix.kindergarten.clazz.fragment.DetailClassFragment;
import com.srinnix.kindergarten.clazz.helper.ClassHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Class;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.ViewManager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2/21/2017.
 */

public class ClassListPresenter extends BasePresenter {
    private ClassListDelegate mClassListDelegate;
    private ClassHelper mHelper;

    public ClassListPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mClassListDelegate = (ClassListDelegate) mDelegate;

        mHelper = new ClassHelper(mDisposable);
    }

    @Override
    public void onStart() {
        super.onStart();
        getListClass();
    }

    private void getListClass() {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            mClassListDelegate.onLoadError(R.string.cant_connect);
            return;
        }

        mHelper.getListClass(new ResponseListener<ArrayList<Class>>() {
            @Override
            public void onSuccess(ApiResponse<ArrayList<Class>> response) {
                if (response == null) {
                    onFail(new NullPointerException());
                    return;
                }

                if (response.result == ApiResponse.RESULT_NG) {
                    ErrorUtil.handleErrorApi(mContext, response.error);
                    return;
                }

                if (mClassListDelegate != null) {
                    mClassListDelegate.onLoadSuccess(response.getData());
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                DebugLog.e(throwable.getMessage());
                if (mClassListDelegate != null) {
                    mClassListDelegate.onLoadError(R.string.error_common);
                }
            }

            @Override
            public void onFinally() {

            }
        });
    }

    public void onClickClass(Class aClass) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_CLASS, aClass.getId());

        ViewManager.getInstance().addFragment(new DetailClassFragment(), bundle,
                R.anim.translate_right_to_left, R.anim.translate_left_to_right);
    }

    public void onClickRetry() {
        getListClass();
    }
}
