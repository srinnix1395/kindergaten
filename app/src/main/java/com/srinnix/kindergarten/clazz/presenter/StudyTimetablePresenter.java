package com.srinnix.kindergarten.clazz.presenter;

import android.os.Bundle;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.clazz.delegate.StudyTimetableDelegate;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;

/**
 * Created by anhtu on 5/5/2017.
 */

public class StudyTimetablePresenter extends TimeTablePresenter {
    private StudyTimetableDelegate mStudyTimetableDelegate;

    private String group;
    private boolean hasError = false;

    public StudyTimetablePresenter(BaseDelegate mDelegate) {
        super(mDelegate);
    }

    @Override
    protected void initDelegate() {
        mStudyTimetableDelegate = (StudyTimetableDelegate) mDelegate;
    }

    @Override
    public void getData(Bundle bundle) {
        super.getData(bundle);

        group = bundle.getString(AppConstant.KEY_GROUP);
    }

    @Override
    public void onStart() {
        getStudyTimetable();
    }

    public void getStudyTimetable() {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            hasError = true;
            mStudyTimetableDelegate.onFail(R.string.cant_connect);
            return;
        }

        mDisposable.add(mHelper.getStudyTimeTable(time, group)
                .subscribe(response -> {
                    if (response == null) {
                        hasError = true;
                        ErrorUtil.handleException(new NullPointerException());
                        return;
                    }

                    if (response.result == ApiResponse.RESULT_NG) {
                        hasError = true;
                        ErrorUtil.handleErrorApi(mContext, response.error);
                        return;
                    }

                    hasError = false;
                    mStudyTimetableDelegate.onSuccessStudyTimetable(response.getData());
                }, throwable -> {
                    hasError = true;
                    ErrorUtil.handleException(throwable);
                    mStudyTimetableDelegate.onFail(R.string.error_common);
                }));
    }

    public String getGroup() {
        return group;
    }

    public boolean isHasError() {
        return hasError;
    }
}
