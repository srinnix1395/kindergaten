package com.srinnix.kindergarten.bulletinboard.presenter;

import android.os.Bundle;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.ResponseListener;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.delegate.PostDelegate;
import com.srinnix.kindergarten.bulletinboard.fragment.ImagePickerFragment;
import com.srinnix.kindergarten.bulletinboard.helper.PostHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.ImageLocal;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.ViewManager;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by anhtu on 4/24/2017.
 */

public class PostPresenter extends BasePresenter {

    private CompositeDisposable mDisposable;
    private PostHelper mHelper;
    private PostDelegate mPostDelegate;

    public PostPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mPostDelegate = (PostDelegate) mDelegate;
        mDisposable = new CompositeDisposable();
        mHelper = new PostHelper(mDisposable);
    }


    public void onClickPost(String content, ArrayList<ImageLocal> mListImage, boolean normalChecked) {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            AlertUtils.showToast(mContext, R.string.noInternetConnection);
            mPostDelegate.onFail();
            return;
        }

        String token = SharedPreUtils.getInstance(mContext).getToken();

        mHelper.post(token, content, mListImage, normalChecked ? AppConstant.POST_NORMAL : AppConstant.POST_IMPORTANT, new ResponseListener<Post>() {
            @Override
            public void onSuccess(ApiResponse<Post> response) {
                if (response == null) {
                    onFail(new NullPointerException());
                    return;
                }

                if (response.error.code == ApiResponse.RESULT_NG) {
                    ErrorUtil.handleErrorApi(mContext, response.error);
                    mPostDelegate.onFail();
                }

                mPostDelegate.onSuccess(response.getData());
            }

            @Override
            public void onFail(Throwable throwable) {
                ErrorUtil.handleException(mContext, throwable);
                mPostDelegate.onFail();
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

    public void onClickSchedule() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }
}
