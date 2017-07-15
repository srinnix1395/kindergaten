package com.srinnix.kindergarten.clazz.presenter;

import android.os.Bundle;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.clazz.delegate.TimeTableDelegate;
import com.srinnix.kindergarten.clazz.helper.ClassHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;

/**
 * Created by anhtu on 5/4/2017.
 */

public class TimeTablePresenter extends BasePresenter {
    protected ClassHelper mHelper;
    private TimeTableDelegate mTimeTableDelegate;

    protected String time;

    public TimeTablePresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        initDelegate();

        mHelper = new ClassHelper(mDisposable);
    }

    protected void initDelegate() {
        mTimeTableDelegate = (TimeTableDelegate) mDelegate;
    }

    @Override
    public void getData(Bundle bundle) {
        super.getData(bundle);

        time = bundle.getString(AppConstant.KEY_TIME);
    }

    @Override
    public void onStart() {
        super.onStart();
        getTimetable();
    }

    public void getTimetable() {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            mTimeTableDelegate.onFail(R.string.cant_connect);
            return;
        }

        mDisposable.add(mHelper.getTimeTable("3/2017")
                .subscribe(response -> {
                    if (response == null) {
                        ErrorUtil.handleException(new NullPointerException());
                        return;
                    }

                    if (response.result == ApiResponse.RESULT_NG) {
                        mTimeTableDelegate.onFail(R.string.error_common);
                        ErrorUtil.handleErrorApi(mContext, response.error);
                        return;
                    }

                    mTimeTableDelegate.onSuccessTimetable(response.getData());
                }, throwable -> {
                    ErrorUtil.handleException(throwable);
                    mTimeTableDelegate.onFail(R.string.error_common);
                }));
    }

    public String getTime() {
        return time;
    }
}
