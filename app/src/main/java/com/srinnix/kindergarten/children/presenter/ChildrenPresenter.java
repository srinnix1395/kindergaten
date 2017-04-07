package com.srinnix.kindergarten.children.presenter;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.ResponseListener;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.children.delegate.ChildrenDelegate;
import com.srinnix.kindergarten.children.helper.ChildrenHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;

/**
 * Created by anhtu on 2/21/2017.
 */

public class ChildrenPresenter extends BasePresenter {

    private CompositeDisposable mDisposable;
    private ChildrenDelegate mChildrenDelegate;
    private ChildrenHelper mHelper;

    public ChildrenPresenter(BaseDelegate mChildrenDelegate) {
        super(mChildrenDelegate);
        this.mChildrenDelegate = (ChildrenDelegate) mChildrenDelegate;
        mDisposable = new CompositeDisposable();
        mHelper = new ChildrenHelper(mDisposable);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (SharedPreUtils.getInstance(mContext).isUserSignedIn()) {
            getListChildren();
        }
    }

    public void getInfoChildren(String idChildren) {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            mChildrenDelegate.onLoadFail(R.string.noInternetConnection);
            return;
        }

        String token = SharedPreUtils.getInstance(mContext).getToken();

        mHelper.getInfoChildren(token, idChildren, new ResponseListener<Child>() {
            @Override
            public void onSuccess(ApiResponse<Child> response) {
                if (response == null) {
                    onFail(new NullPointerException());
                    return;
                }

                if (response.result == ApiResponse.RESULT_NG) {
                    ErrorUtil.handleErrorApi(mContext, response.error);
                    return;
                }

                mChildrenDelegate.onLoadChildren(response.getData());
            }

            @Override
            public void onFail(Throwable throwable) {
                ErrorUtil.handleException(mContext, throwable);
                mChildrenDelegate.onLoadFail(R.string.error_common);
            }
        });
    }

    private void getListChildren() {
        int accountType = SharedPreUtils.getInstance(mContext).getAccountType();
        if (accountType == AppConstant.ACCOUNT_PARENTS) {
            getListChildrenParent();
        } else {
            getListChildrenTeacher();
        }
    }

    private void getListChildrenParent() {
        if (mChildrenDelegate == null) {
            return;
        }
        Realm.getDefaultInstance().where(Child.class)
                .findAllAsync()
                .addChangeListener(element -> {
                    if (element.size() == 1) {
                        getInfoChildren(element.get(0).getId());
                    } else {
                        ArrayList<Child> childArrayList = new ArrayList<>();
                        childArrayList.addAll(element);
                        mChildrenDelegate.onLoadListChildren(childArrayList);
                    }
                });
    }

    private void getListChildrenTeacher() {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            mChildrenDelegate.onLoadFail(R.string.noInternetConnection);
            return;
        }

        String classId = SharedPreUtils.getInstance(mContext).getClassId();
        if (classId == null) {
            return;
        }

        String token = SharedPreUtils.getInstance(mContext).getToken();
        mHelper.getListChildrenTeacher(token, classId, new ResponseListener<ArrayList<Child>>() {
            @Override
            public void onSuccess(ApiResponse<ArrayList<Child>> response) {
                if (response == null) {
                    DebugLog.e("response is null");
                    mChildrenDelegate.onLoadFail(R.string.error_common);
                    return;
                }

                if (response.result == ApiResponse.RESULT_NG) {
                    ErrorUtil.handleErrorApi(mContext, response.error);
                    mChildrenDelegate.onLoadFail(R.string.error_common);
                    return;
                }

                mChildrenDelegate.onLoadListChildren(response.getData());
            }

            @Override
            public void onFail(Throwable throwable) {
                ErrorUtil.handleException(mContext, throwable);
                mChildrenDelegate.onLoadFail(R.string.error_common);
            }
        });
    }

    public void onClickRetry() {
        // TODO: 4/1/2017 retry
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }


}
