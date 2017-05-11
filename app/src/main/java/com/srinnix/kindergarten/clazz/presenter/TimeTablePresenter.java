package com.srinnix.kindergarten.clazz.presenter;

import android.os.Bundle;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.callback.ResponseListener;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.clazz.delegate.TimeTableDelegate;
import com.srinnix.kindergarten.clazz.helper.ClassHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Timetable;
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

        mHelper.getTimeTable(time, new ResponseListener<Timetable>() {
            @Override
            public void onSuccess(ApiResponse<Timetable> response) {
                if (response == null) {
                    onFail(new NullPointerException());
                    return;
                }

                if (response.result == ApiResponse.RESULT_NG) {
                    ErrorUtil.handleErrorApi(mContext, response.error);
                    return;
                }

                mTimeTableDelegate.onSuccessTimetable(response.getData());
            }

            @Override
            public void onFail(Throwable throwable) {
                ErrorUtil.handleException(throwable);

            }

            @Override
            public void onFinally() {

            }
        });
    }

    public String getTime() {
        return time;
    }
}
