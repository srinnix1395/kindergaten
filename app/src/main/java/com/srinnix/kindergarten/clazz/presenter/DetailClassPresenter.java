package com.srinnix.kindergarten.clazz.presenter;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.chat.fragment.DetailChatFragment;
import com.srinnix.kindergarten.clazz.delegate.ClassDelegate;
import com.srinnix.kindergarten.clazz.fragment.MemberClassFragment;
import com.srinnix.kindergarten.clazz.fragment.TeacherInfoDialogFragment;
import com.srinnix.kindergarten.clazz.helper.ClassHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.messageeventbus.MessageContactStatus;
import com.srinnix.kindergarten.model.ContactTeacher;
import com.srinnix.kindergarten.model.Teacher;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.ClassResponse;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.StringUtil;
import com.srinnix.kindergarten.util.ViewManager;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by anhtu on 2/16/2017.
 */

public class DetailClassPresenter extends BasePresenter {
    private ClassDelegate mDelegate;
    private CompositeDisposable mDisposable;
    private boolean isTeacher;
    private ClassResponse classResponse;
    private ClassHelper mHelper;
    private String classId;

    public DetailClassPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        this.mDelegate = (ClassDelegate) mDelegate;

        mDisposable = new CompositeDisposable();
        mHelper = new ClassHelper(mDisposable);
        isTeacher = SharedPreUtils.getInstance(mContext).getAccountType() == AppConstant.ACCOUNT_TEACHERS;
    }

    @Override
    public void getData(Bundle bundle) {
        super.getData(bundle);
        classId = bundle.getString(AppConstant.KEY_CLASS);
    }

    @Override
    public void onStart(boolean isFirst) {
        super.onStart(isFirst);
        if (isFirst) {
            getClassInfo(classId);
        }
    }

    public void getClassInfo(String classId) {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            mDelegate.onLoadError(R.string.noInternetConnection);
            return;
        }

        mHelper.getClassInfo(classId, isTeacher, new ClassHelper.ClassInfoListener() {
            @Override
            public void onResponseSuccess(ApiResponse<ClassResponse> response) {
                handleResponseClassInfo(response);
            }

            @Override
            public void onResponseFail(Throwable throwable) {
                ErrorUtil.handleException(mContext, throwable);
            }
        });
    }

    private void handleResponseClassInfo(ApiResponse<ClassResponse> response) {
        if (response == null) {
            ErrorUtil.handleException(mContext, new NullPointerException());
            return;
        }

        if (response.result == ApiResponse.RESULT_NG) {
            ErrorUtil.handleErrorApi(mContext, response.error);
            return;
        }

        classResponse = response.getData();
        if (mDelegate != null) {
            mDelegate.onLoadSuccess(classResponse);
        }
    }

    public void onClickChat(int teacherPosition) {
        if (classResponse != null) {
            Teacher teacher = classResponse.getTeacherArrayList().get(teacherPosition);

            Bundle bundle = new Bundle();
            bundle.putString(AppConstant.KEY_ID, teacher.getId());
            bundle.putString(AppConstant.KEY_NAME,
                    StringUtil.getNameContactTeacher(mContext, new ContactTeacher(teacher.getName(), teacher.getGender(), teacher.getClassName())));
            MessageContactStatus message = EventBus.getDefault().getStickyEvent(MessageContactStatus.class);
            int status = ChatConstant.STATUS_UNDEFINED;
            if (message != null) {
                if (message.arrayList.contains(teacher.getId())) {
                    status = ChatConstant.STATUS_ONLINE;
                } else {
                    status = ChatConstant.STATUS_OFFLINE;
                }
            }
            bundle.putInt(AppConstant.KEY_STATUS, status);
            bundle.putString(AppConstant.KEY_IMAGE, teacher.getImage());
            bundle.putInt(AppConstant.KEY_ACCOUNT_TYPE, AppConstant.ACCOUNT_TEACHERS);

            ViewManager.getInstance().addFragment(new DetailChatFragment(), bundle,
                    R.anim.translate_right_to_left, R.anim.translate_left_to_right);
        }
    }

    public void onClickTeacher(FragmentManager fragmentManager, int position) {
        if (classResponse == null) {
            return;
        }

        TeacherInfoDialogFragment dialog = new TeacherInfoDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_ID, classResponse.getTeacherArrayList().get(position).getId());
        dialog.setArguments(bundle);

        dialog.show(fragmentManager, "dialog");
    }

    public void onClickChildViewHolder(String id) {
        //// TODO: 3/2/2017 onclick child
    }



    public void onClickSeeAll() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AppConstant.KEY_MEMBER, classResponse.getChildren());

        ViewManager.getInstance().addFragment(new MemberClassFragment(), bundle);
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }
}
