package com.srinnix.kindergarten.children.presenter;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.ResponseListener;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.children.delegate.ChildrenDelegate;
import com.srinnix.kindergarten.children.helper.ChildrenHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.HealthTotalChildren;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.util.AlertUtils;
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
    private String idChild;

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

    public void setIdChild(String id) {
        idChild = id;
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
                        idChild = element.get(0).getId();
                        getInfoChildren(element.get(0).getId());
                        getTimelineChildren(System.currentTimeMillis());
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

    public void getTimelineChildren(long time) {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            AlertUtils.showToast(mContext, R.string.noInternetConnection);
            return;
        }

        String token = SharedPreUtils.getInstance(mContext).getToken();
        mHelper.getTimelineChildren(token, idChild, time, new ResponseListener<ArrayList<HealthTotalChildren>>() {
            @Override
            public void onSuccess(ApiResponse<ArrayList<HealthTotalChildren>> response) {
                if (response == null) {
                    onFail(new NullPointerException());
                    return;
                }

                if (response.result == ApiResponse.RESULT_NG) {
                    ErrorUtil.handleErrorApi(mContext, response.error);
                    return;
                }

                mChildrenDelegate.onLoadChildrenTimeLine(response.getData());
            }

            @Override
            public void onFail(Throwable throwable) {

            }
        });
    }

    public void onClickRetry() {
        // TODO: 4/1/2017 retry
    }

    public void onClickIndex(ArrayList<Object> mListChildrenHealth, int position) {
        // TODO: 4/15/2017 index
    }

    public void onClickHealth(HealthTotalChildren health) {
        // TODO: 4/15/2017 health
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }


}
