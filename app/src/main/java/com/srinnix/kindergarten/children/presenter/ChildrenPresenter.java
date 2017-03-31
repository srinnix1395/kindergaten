package com.srinnix.kindergarten.children.presenter;

import com.srinnix.kindergarten.R;
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
        getListChildren();
    }

    public void getInfoChildren(String idChildren) {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            mChildrenDelegate.onLoadFail(R.string.noInternetConnection);
            return;
        }

        String token = SharedPreUtils.getInstance(mContext).getToken();

        mHelper.getInfoChildren(token, idChildren, new ChildrenHelper.ChildrenListener() {
            @Override
            public void onSuccess(ApiResponse<Child> response) {

            }

            @Override
            public void onFail(Throwable throwable) {
                ErrorUtil.handleException(mContext, throwable);
                mChildrenDelegate.onLoadFail(R.string.error_common);
            }
        });
    }

    public void getListChildren() {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            mChildrenDelegate.onLoadFail(R.string.noInternetConnection);
            return;
        }
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
                        String token = SharedPreUtils.getInstance(mContext).getToken();
                        mHelper.getInfoChildren(token, element.get(0).getId(), new ChildrenHelper.ChildrenListener() {
                            @Override
                            public void onSuccess(ApiResponse<Child> response) {
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

                                mChildrenDelegate.onLoadChildren(response.getData());
                            }

                            @Override
                            public void onFail(Throwable throwable) {
                                ErrorUtil.handleException(mContext, throwable);
                                mChildrenDelegate.onLoadFail(R.string.error_common);
                            }
                        });
                    } else {
                        ArrayList<Child> childArrayList = new ArrayList<>();
                        childArrayList.addAll(element);
                        mChildrenDelegate.onLoadListChildren(childArrayList);
                    }
                });
    }

    private void getListChildrenTeacher() {
        String classId = SharedPreUtils.getInstance(mContext).getClassId();
        if (classId == null) {
            return;
        }

        String token = SharedPreUtils.getInstance(mContext).getToken();
        mHelper.getListChildrenTeacher(token, classId, new ChildrenHelper.ListChildrenListener() {
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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }


}
