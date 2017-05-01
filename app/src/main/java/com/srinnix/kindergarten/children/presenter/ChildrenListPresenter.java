package com.srinnix.kindergarten.children.presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.ProgressBar;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.ResponseListener;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.children.delegate.ChildrenListDelegate;
import com.srinnix.kindergarten.children.fragment.InfoChildrenFragment;
import com.srinnix.kindergarten.children.helper.ChildrenHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.UiUtils;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import io.realm.Realm;

/**
 * Created by anhtu on 4/20/2017.
 */

public class ChildrenListPresenter extends BasePresenter {

    private ChildrenHelper mHelper;
    private CompositeDisposable mDisposable;
    private ChildrenListDelegate mChildrenDelegate;

    public ChildrenListPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mChildrenDelegate = (ChildrenListDelegate) mDelegate;
        mDisposable = new CompositeDisposable();
        mHelper = new ChildrenHelper(mDisposable);
    }

    public void getListChildren(Fragment fragment, ProgressBar pbLoading) {
        int accountType = SharedPreUtils.getInstance(mContext).getAccountType();
        if (accountType == AppConstant.ACCOUNT_PARENTS) {
            getListChildrenParent(fragment, pbLoading);
        } else {
            getListChildrenTeacher();
        }
    }

    private void getListChildrenParent(Fragment fragment, ProgressBar pbLoading) {
        if (mChildrenDelegate == null) {
            return;
        }
        Realm.getDefaultInstance().where(Child.class)
                .findAllAsync()
                .addChangeListener(element -> {
                    if (element.size() == 1) {
                        UiUtils.hideProgressBar(pbLoading);
                        openFragmentInfoChild(fragment, element.get(0).getId());
                    } else {
                        ArrayList<Child> childArrayList = new ArrayList<>();
                        childArrayList.addAll(element);
                        mChildrenDelegate.onLoadListChildren(childArrayList);
                    }
                });
    }

    public void openFragmentInfoChild(Fragment fragment, String id) {
        InfoChildrenFragment childrenFragment = new InfoChildrenFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_ID, id);
        childrenFragment.setArguments(bundle);

        FragmentTransaction transaction = fragment.getChildFragmentManager().beginTransaction();
        transaction.add(R.id.layout_list_child, childrenFragment, String.valueOf(AppConstant.FRAGMENT_INFO_CHILDREN));
        transaction.show(childrenFragment);
        transaction.commit();
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

            @Override
            public void onFinally() {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }


}
