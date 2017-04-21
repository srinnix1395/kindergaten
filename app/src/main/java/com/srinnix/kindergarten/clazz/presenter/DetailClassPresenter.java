package com.srinnix.kindergarten.clazz.presenter;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.widget.ImageView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.ResponseListener;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.fragment.PreviewImageFragment;
import com.srinnix.kindergarten.chat.fragment.DetailChatFragment;
import com.srinnix.kindergarten.children.fragment.InfoChildrenFragment;
import com.srinnix.kindergarten.clazz.delegate.ClassDelegate;
import com.srinnix.kindergarten.clazz.fragment.DetailClassFragment;
import com.srinnix.kindergarten.clazz.fragment.TeacherInfoDialogFragment;
import com.srinnix.kindergarten.clazz.helper.ClassHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.constant.ChatConstant;
import com.srinnix.kindergarten.messageeventbus.MessageContactStatus;
import com.srinnix.kindergarten.model.ContactTeacher;
import com.srinnix.kindergarten.model.Image;
import com.srinnix.kindergarten.model.Teacher;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.ClassResponse;
import com.srinnix.kindergarten.request.model.ImageResponse;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.StringUtil;
import com.srinnix.kindergarten.util.ViewManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by anhtu on 2/16/2017.
 */

public class DetailClassPresenter extends BasePresenter {
    private ClassDelegate mClassDelegate;
    private CompositeDisposable mDisposable;
    private boolean isTeacher;
    private ClassResponse classResponse;
    private ClassHelper mHelper;
    private String classId;

    public DetailClassPresenter(BaseDelegate mClassDelegate) {
        super(mClassDelegate);
        this.mClassDelegate = (ClassDelegate) mClassDelegate;

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
    public void onStart() {
        super.onStart();
        getClassInfo(classId);
    }

    private void getClassInfo(String classId) {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            mClassDelegate.onLoadError(R.string.noInternetConnection);
            return;
        }

        mHelper.getClassInfo(classId, isTeacher, new ResponseListener<ClassResponse>() {
            @Override
            public void onSuccess(ApiResponse<ClassResponse> response) {
                handleResponseClassInfo(response);
            }

            @Override
            public void onFail(Throwable throwable) {
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
        if (mClassDelegate != null) {
            mClassDelegate.onLoadSuccess(classResponse);
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
        InfoChildrenFragment childrenFragment = new InfoChildrenFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_ID, id);
        childrenFragment.setArguments(bundle);

        ViewManager.getInstance().addFragment(childrenFragment, bundle,
                R.anim.translate_right_to_left, R.anim.translate_left_to_right);
    }

    public void getImage(ArrayList<Object> arrayList) {
        long time;
        if (arrayList.size() == 1) {
            time = System.currentTimeMillis();
        } else {
            time = ((Image) arrayList.get(arrayList.size() - 2)).getCreatedAt();
        }
        mHelper.getClassImage(classId, time, new ResponseListener<ImageResponse>() {
            @Override
            public void onSuccess(ApiResponse<ImageResponse> response) {
                if (response == null) {
                    onFail(new NullPointerException());
                    return;
                }

                if (response.result == ApiResponse.RESULT_NG) {
                    ErrorUtil.handleErrorApi(mContext, response.error);
                    return;
                }

                if (response.getData() != null) {
                    mClassDelegate.onLoadImage(response.getData().getArrayList());
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                ErrorUtil.handleException(mContext, throwable);
            }
        });
    }

    public boolean isTeacher() {
        return isTeacher;
    }


    public void onClickImage(DetailClassFragment fragmentOne, ImageView sharedTransitionView, Image image) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            PreviewImageFragment fragmentTwo = PreviewImageFragment.newInstance(image, ViewCompat.getTransitionName(sharedTransitionView));

            fragmentOne.getFragmentManager().beginTransaction()
                    .addSharedElement(sharedTransitionView, ViewCompat.getTransitionName(sharedTransitionView))
                    .addToBackStack(fragmentTwo.getClass().getName())
                    .add(R.id.layout_content, fragmentTwo)
                    .commit();

        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppConstant.KEY_IMAGE, image);

            ViewManager.getInstance().addFragment(new PreviewImageFragment(), bundle);
        }
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }

    public void onClickTimeTable() {

    }

    public void onClickPlaySchedule() {

    }
}
