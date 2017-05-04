package com.srinnix.kindergarten.clazz.presenter;

import android.os.Bundle;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.callback.ResponseListener;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.clazz.delegate.TimeTableDelegate;
import com.srinnix.kindergarten.clazz.helper.ClassHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.StudyTimetable;
import com.srinnix.kindergarten.model.Timetable;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;

import java.util.Calendar;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by anhtu on 5/4/2017.
 */

public class TimeTablePresenter extends BasePresenter {
    private CompositeDisposable mDisposable;
    private ClassHelper mHelper;
    private TimeTableDelegate mTimeTableDelegate;

    private String group;
    private int type;

    public TimeTablePresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mTimeTableDelegate = (TimeTableDelegate) mDelegate;
        mDisposable = new CompositeDisposable();
        mHelper = new ClassHelper(mDisposable);
    }

    @Override
    public void getData(Bundle bundle) {
        super.getData(bundle);

        group = bundle.getString(AppConstant.KEY_GROUP);
        type = bundle.getInt(AppConstant.KEY_TIMETABLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        switch (type) {
            case AppConstant.TIME_TABLE: {
                getTimetable();
                break;
            }
            case AppConstant.STUDY_TIME_TABLE: {
                getStudyTimetable();
                break;
            }
            case AppConstant.STUDY_SCHEDULE: {
                getStudySchedule();
                break;
            }
        }
    }

    public void getTimetable() {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            mTimeTableDelegate.onFail(R.string.noInternetConnection);
            return;
        }

        Calendar calendar = Calendar.getInstance();

        String time = calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);

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

    public void getStudyTimetable() {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            mTimeTableDelegate.onFail(R.string.noInternetConnection);
            return;
        }

        Calendar calendar = Calendar.getInstance();

        String time = calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);

        mHelper.getStudyTimeTable(time, group, new ResponseListener<StudyTimetable>() {
            @Override
            public void onSuccess(ApiResponse<StudyTimetable> response) {
                if (response == null) {
                    onFail(new NullPointerException());
                    return;
                }

                if (response.result == ApiResponse.RESULT_NG) {
                    ErrorUtil.handleErrorApi(mContext, response.error);
                    return;
                }

                mTimeTableDelegate.onSuccessStudyTimetable(response.getData());
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

    public void getStudySchedule() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }
}
