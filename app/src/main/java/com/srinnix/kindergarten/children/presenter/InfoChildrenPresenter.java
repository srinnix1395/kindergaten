package com.srinnix.kindergarten.children.presenter;

import android.os.Bundle;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.ResponseListener;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.children.delegate.ChildrenDelegate;
import com.srinnix.kindergarten.children.fragment.ChartFragment;
import com.srinnix.kindergarten.children.helper.ChildrenHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.Health;
import com.srinnix.kindergarten.model.HealthTotal;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.ViewManager;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by anhtu on 2/21/2017.
 */

public class InfoChildrenPresenter extends BasePresenter {

    private CompositeDisposable mDisposable;
    private ChildrenDelegate mChildrenDelegate;
    private ChildrenHelper mHelper;
    private String idChild;
    private boolean isLoadTimelineFirst = true;

    public InfoChildrenPresenter(BaseDelegate mChildrenDelegate) {
        super(mChildrenDelegate);
        this.mChildrenDelegate = (ChildrenDelegate) mChildrenDelegate;
        mDisposable = new CompositeDisposable();
        mHelper = new ChildrenHelper(mDisposable);
    }

    @Override
    public void getData(Bundle bundle) {
        super.getData(bundle);
        idChild = bundle.getString(AppConstant.KEY_ID);
        getInfoChildren();
        getTimelineChildren(System.currentTimeMillis());
    }

    public void getInfoChildren() {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            mChildrenDelegate.onLoadFail(R.string.noInternetConnection);
            return;
        }

        String token = SharedPreUtils.getInstance(mContext).getToken();

        mHelper.getInfoChildren(token, idChild, new ResponseListener<Child>() {
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

    public void getTimelineChildren(long time) {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            AlertUtils.showToast(mContext, R.string.noInternetConnection);
            return;
        }

        String token = SharedPreUtils.getInstance(mContext).getToken();
        mHelper.getTimelineChildren(token, idChild, time, new ResponseListener<ArrayList<HealthTotal>>() {
            @Override
            public void onSuccess(ApiResponse<ArrayList<HealthTotal>> response) {
                if (response == null) {
                    onFail(new NullPointerException());
                    return;
                }

                if (response.result == ApiResponse.RESULT_NG) {
                    ErrorUtil.handleErrorApi(mContext, response.error);
                    return;
                }

                mChildrenDelegate.onLoadChildrenTimeLine(response.getData(), isLoadTimelineFirst);
                if (isLoadTimelineFirst) {
                    isLoadTimelineFirst = false;
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                ErrorUtil.handleException(mContext, throwable);
            }
        });
    }

    public void onClickIndex(ArrayList<Object> mListChildrenHealth) {
        ArrayList<Health> listHealth = new ArrayList<>();
        for (Object o : mListChildrenHealth) {
            if (o instanceof HealthTotal && ((HealthTotal) o).getWeight() != AppConstant.UNSPECIFIED) {
                listHealth.add(0, (Health) o);
                if (listHealth.size() == 12) {
                    break;
                }
            }
        }

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AppConstant.KEY_HEALTH, listHealth);

        ViewManager.getInstance().addFragment(new ChartFragment(), bundle,
                R.anim.translate_right_to_left, R.anim.translate_left_to_right);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }


}
