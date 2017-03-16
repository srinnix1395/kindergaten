package com.srinnix.kindergarten.clazz.presenter;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.clazz.delegate.ClassDelegate;
import com.srinnix.kindergarten.clazz.helper.ClassHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.ClassResponse;
import com.srinnix.kindergarten.request.model.Error;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.SharedPreUtils;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by anhtu on 2/16/2017.
 */

public class ClassPresenter extends BasePresenter {
    private ClassDelegate mDelegate;
    private CompositeDisposable mDisposable;
    private ClassResponse classInfo;
    private boolean isTeacher;
    private ClassHelper mHelper;

    public ClassPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        this.mDelegate = (ClassDelegate) mDelegate;
        mDisposable = new CompositeDisposable();
        isTeacher = SharedPreUtils.getInstance(mContext).getAccountType() == AppConstant.ACCOUNT_TEACHERS;
    }

    public void getClassInfo(String classId) {
        mHelper.getClassInfo(mDisposable, classId, isTeacher, new ClassHelper.ClassInfoListener() {
            @Override
            public void onResponseSuccess(ApiResponse<ClassResponse> response) {
                handleResponseClassInfo(response);
            }

            @Override
            public void onResponseFail(Throwable throwable) {
                handleException(throwable);
            }
        });
    }

    private void handleResponseClassInfo(ApiResponse<ClassResponse> response) {
        if (response == null) {
            handleException(new NullPointerException());
            return;
        }

        if (response.result == ApiResponse.RESULT_NG) {
            handleError(response.error);
            return;
        }

        classInfo = response.getData();
        if (mDelegate != null) {
            mDelegate.displayInfo(classInfo);
        }
    }

    private void handleError(Error error) {
        //// TODO: 3/2/2017 handle error
    }

    private void handleException(Throwable throwable) {
        DebugLog.e(throwable.getMessage());

        AlertUtils.showToast(mContext, R.string.commonError);
    }

    public void onClickChat(int teacherPosition) {
        if (classInfo != null) {
            classInfo.getTeacherArrayList().get(teacherPosition);
            //// TODO: 3/2/2017  
        }
    }

    public void onClickTeacher(int i) {
        // TODO: 3/2/2017 onclick teacher
    }

    public void onClickChildViewHolder(String id) {
        //// TODO: 3/2/2017 onclick child
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }

    public void onClickSeeAll() {
        // TODO: 3/2/2017 onclick see all
    }

    public boolean isTeacher() {
        return isTeacher;
    }
}
