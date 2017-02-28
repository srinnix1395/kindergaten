package com.srinnix.kindergarten.schoolboard.presenter;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.constant.ErrorConstant;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.model.Error;
import com.srinnix.kindergarten.request.remote.ApiService;
import com.srinnix.kindergarten.schoolboard.adapter.PostAdapter;
import com.srinnix.kindergarten.schoolboard.delegate.SchoolBoardDelegate;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.ServiceUtils;
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

    public void onLoadMore(CompositeDisposable mDisposable, ArrayList<Object> arrayList, PostAdapter postAdapter, int totalItemCount) {
        long timePrevPost;
        if (arrayList.isEmpty()) {
            timePrevPost = System.currentTimeMillis();
        } else {
            timePrevPost = ((Post) arrayList.get(totalItemCount - 2)).getCreatedAt();
        }

        if (ServiceUtils.isNetworkAvailable(mContext)) {
            ((LoadingItem) arrayList.get(arrayList.size() - 1)).setLoadingState(LoadingItem.STATE_ERROR);
            postAdapter.notifyItemChanged(arrayList.size() - 1);
            return;
        }

        mDisposable.clear();
        mDisposable.add(
                apiService.getListPost(token, timePrevPost)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleSuccessResponse
                                , throwable -> handleException(throwable, arrayList, postAdapter))
        );


    }

    private void handleSuccessResponse(ApiResponse<ArrayList<Post>> response) {
        if (response == null) {
            DebugLog.i(ErrorConstant.RESPONSE_NULL);
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
        //// TODO: 2/28/2017 handle error
    }

    private void handleException(Throwable throwable, ArrayList<Object> arrayList, PostAdapter postAdapter) {
        DebugLog.i(throwable.getMessage());

        int size = arrayList.size();
        ((LoadingItem) arrayList.get(size - 1)).setLoadingState(LoadingItem.STATE_ERROR);
        postAdapter.notifyItemChanged(size - 1);

        AlertUtils.showToast(mContext, R.string.errorPost);
    }


}
