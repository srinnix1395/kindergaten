package com.srinnix.kindergarten.clazz.presenter;

import android.os.Bundle;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.fragment.MediaPickerFragment;
import com.srinnix.kindergarten.clazz.delegate.PostImageDelegate;
import com.srinnix.kindergarten.clazz.helper.ClassHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.MediaLocal;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.ViewManager;

import java.util.ArrayList;

/**
 * Created by anhtu on 5/11/2017.
 */

public class PostImagePresenter extends BasePresenter {
    private ClassHelper mHelper;
    private PostImageDelegate mPostImageDelegate;

    public PostImagePresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mPostImageDelegate = (PostImageDelegate) mDelegate;

        mHelper = new ClassHelper(mDisposable);
    }

    public void onClickPost(ArrayList<MediaLocal> mListImage) {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            AlertUtils.showToast(mContext, R.string.noInternetConnection);
            mPostImageDelegate.onFail();
            return;
        }

        String token = SharedPreUtils.getInstance(mContext).getToken();
        String classId = SharedPreUtils.getInstance(mContext).getClassId();

        mDisposable.add(mHelper.postImage(token, classId, mListImage)
                .subscribe(response -> {
                    if (response == null) {
                        mPostImageDelegate.onFail();
                        ErrorUtil.handleException(new NullPointerException());
                        return;
                    }

                    if (response.result == ApiResponse.RESULT_NG) {
                        ErrorUtil.handleErrorApi(mContext, response.error);
                        mPostImageDelegate.onFail();
                    }

                    mPostImageDelegate.onSuccess(response.getData());
                }, throwable -> {
                    ErrorUtil.handleException(mContext, throwable);
                    mPostImageDelegate.onFail();
                }));
    }

    public void onClickImage(ArrayList<MediaLocal> mListImage) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AppConstant.KEY_MEDIA, mListImage);
        bundle.putInt(AppConstant.KEY_LIMIT, 10);
        bundle.putInt(AppConstant.KEY_FRAGMENT, AppConstant.FRAGMENT_POST_IMAGE);

        ViewManager.getInstance().addFragment(new MediaPickerFragment(), bundle,
                R.anim.translate_right_to_left, R.anim.translate_left_to_right);
    }

    public void onClickVideo() {

    }

    public void onClickFacebook() {

    }
}
