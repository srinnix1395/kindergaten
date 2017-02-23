package com.srinnix.kindergarten.schoolboard.presenter;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.Error;
import com.srinnix.kindergarten.request.remote.ApiService;
import com.srinnix.kindergarten.schoolboard.delegate.SchoolBoardDelegate;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.SharedPreUtils;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DELL on 2/3/2017.
 */

public class SchoolBoardPresenter extends BasePresenter {

    private ApiService apiService;
    private String token;
    private SchoolBoardDelegate mDelegate;

    public SchoolBoardPresenter(BaseDelegate delegate) {
        super(delegate);
        mDelegate = (SchoolBoardDelegate) delegate;
        token = SharedPreUtils.getInstance(mContext).getToken();
        apiService = RetrofitClient.getApiService();
    }

    public void onLoadMore(CompositeDisposable mDisposable, int page, int totalItemCount) {
        mDisposable.add(
                apiService.getListPost(token, page)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleSuccessResponse, this::handleException)
        );
    }

    private void handleSuccessResponse(ApiResponse<ArrayList<Post>> response) {
        if (response == null) {
            AlertUtils.showToast(mContext, R.string.commonError);
        } else if (response.result == ApiResponse.RESULT_OK) {
            if (mDelegate != null) {
                mDelegate.updateSchoolBoard(response.getData());
            }
        } else {
            handleError(response.error);
        }
    }

    private void handleError(Error error) {

    }

    private void handleException(Throwable throwable) {
        AlertUtils.showToast(mContext, R.string.commonError);
        DebugLog.e(throwable.getMessage());
    }


}
