package com.srinnix.kindergarten.bulletinboard.presenter;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.delegate.PostDelegate;
import com.srinnix.kindergarten.bulletinboard.helper.BulletinBoardHelper;
import com.srinnix.kindergarten.model.MediaLocal;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.StringUtil;

import java.util.ArrayList;

/**
 * Created by anhtu on 5/1/2017.
 */

public class PostPresenter extends BasePresenter {
    private BulletinBoardHelper mHelper;
    private PostDelegate mPostDelegate;

    public PostPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mPostDelegate = (PostDelegate) mDelegate;

        mHelper = new BulletinBoardHelper(mDisposable);
    }

    public void onClickPost(String content, ArrayList<MediaLocal> mListImage,
                            int notificationType, int notificationRange, boolean now,
                            int[] schedule) {

        if (!now && !StringUtil.isScheduleTimeValid(schedule)) {
            AlertUtils.showToast(mContext, R.string.schedule_is_not_valid);
            mPostDelegate.onFail();
            return;
        }

        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            AlertUtils.showToast(mContext, R.string.noInternetConnection);
            mPostDelegate.onFail();
            return;
        }

        String token = SharedPreUtils.getInstance(mContext).getToken();

        mDisposable.add(mHelper.post(token, content, mListImage, notificationType,
                notificationRange, now, schedule)
                .subscribe(response -> {
                    if (response == null) {
                        ErrorUtil.handleException(mContext, new NullPointerException());
                        return;
                    }

                    if (response.result == ApiResponse.RESULT_NG) {
                        ErrorUtil.handleErrorApi(mContext, response.error);
                        mPostDelegate.onFail();
                    }

                    mPostDelegate.onSuccess(response.getData());
                }, throwable -> {
                    ErrorUtil.handleException(mContext, throwable);
                    mPostDelegate.onFail();
                }));
    }
}
