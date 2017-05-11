package com.srinnix.kindergarten.clazz.presenter;

import android.os.Bundle;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.callback.ResponseListener;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.fragment.ImagePickerFragment;
import com.srinnix.kindergarten.clazz.delegate.PostImageDelegate;
import com.srinnix.kindergarten.clazz.helper.ClassHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Image;
import com.srinnix.kindergarten.model.ImageLocal;
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

    public void onClickPost(ArrayList<ImageLocal> mListImage) {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            AlertUtils.showToast(mContext, R.string.noInternetConnection);
            mPostImageDelegate.onFail();
            return;
        }

        String token = SharedPreUtils.getInstance(mContext).getToken();
        String classId = SharedPreUtils.getInstance(mContext).getClassId();

        mHelper.postImage(token, classId, mListImage, new ResponseListener<ArrayList<Image>>() {
            @Override
            public void onSuccess(ApiResponse<ArrayList<Image>> response) {
                if (response == null) {
                    onFail(new NullPointerException());
                    return;
                }

                if (response.result == ApiResponse.RESULT_NG) {
                    ErrorUtil.handleErrorApi(mContext, response.error);
                    mPostImageDelegate.onFail();
                }

                mPostImageDelegate.onSuccess(response.getData());
            }

            @Override
            public void onFail(Throwable throwable) {
                ErrorUtil.handleException(mContext, throwable);
                mPostImageDelegate.onFail();
            }

            @Override
            public void onFinally() {

            }
        });
    }

    public void onClickImage(ArrayList<ImageLocal> mListImage) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AppConstant.KEY_IMAGE, mListImage);

        ViewManager.getInstance().addFragment(new ImagePickerFragment(), bundle,
                R.anim.translate_right_to_left, R.anim.translate_left_to_right);
    }

    public void onClickVideo() {

    }

    public void onClickFacebook() {

    }
}
