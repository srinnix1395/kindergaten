package com.srinnix.kindergarten.clazz.presenter;

import android.os.Bundle;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.callback.ResponseListener;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.clazz.delegate.StudyTimetableDelegate;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.StudyTimetable;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;

import java.util.ArrayList;

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

        mHelper.getStudyTimeTable(time, group, new ResponseListener<ArrayList<StudyTimetable>>() {
            @Override
            public void onSuccess(ApiResponse<ArrayList<StudyTimetable>> response) {
                if (response == null) {
                    hasError = true;
                    onFail(new NullPointerException());
                    return;
                }

                if (response.result == ApiResponse.RESULT_NG) {
                    hasError = true;
                    ErrorUtil.handleErrorApi(mContext, response.error);
                    return;
                }

                hasError = false;
                mStudyTimetableDelegate.onSuccessStudyTimetable(response.getData());
            }

            @Override
            public void onFail(Throwable throwable) {
                hasError = true;
                ErrorUtil.handleException(throwable);
                mStudyTimetableDelegate.onFail(R.string.error_common);
            }

            @Override
            public void onFinally() {

            }
        });
    }

    public String getGroup() {
        return group;
    }

    public boolean isHasError() {
        return hasError;
    }
}
