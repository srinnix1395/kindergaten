package com.srinnix.kindergarten.children.presenter;

import android.content.Intent;
import android.os.Bundle;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.activity.HorizontalActivity;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.children.delegate.ChildrenDelegate;
import com.srinnix.kindergarten.children.helper.ChildrenHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.HealthCompact;
import com.srinnix.kindergarten.model.HealthTotal;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;

import java.util.ArrayList;

/**
 * Created by anhtu on 2/21/2017.
 */

public class InfoChildrenPresenter extends BasePresenter {

    private ChildrenDelegate mChildrenDelegate;
    private ChildrenHelper mHelper;
    private String idChild;
    private boolean isLoadTimelineFirst = true;
    private Child infoChild;

    public InfoChildrenPresenter(BaseDelegate mChildrenDelegate) {
        super(mChildrenDelegate);
        this.mChildrenDelegate = (ChildrenDelegate) mChildrenDelegate;

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
            mChildrenDelegate.onLoadFail(R.string.cant_connect);
            return;
        }

        String token = SharedPreUtils.getInstance(mContext).getToken();

        mDisposable.add(mHelper.getInfoChildren(token, idChild)
                .subscribe(response -> {
                    if (response == null) {
                        throw new NullPointerException();
                    }

                    if (response.result == ApiResponse.RESULT_NG) {
                        ErrorUtil.handleErrorApi(mContext, response.error);
                        return;
                    }

                    infoChild = response.getData();
                    mChildrenDelegate.onLoadChildren(infoChild);
                }, throwable -> {
                    ErrorUtil.handleException(mContext, throwable);
                    mChildrenDelegate.onLoadFail(R.string.error_common);
                }));
    }

    public void getTimelineChildren(long time) {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            AlertUtils.showToast(mContext, R.string.noInternetConnection);
            return;
        }

        String token = SharedPreUtils.getInstance(mContext).getToken();

        mDisposable.add(mHelper.getTimelineChildren(token, idChild, time)
                .subscribe(response -> {
                    if (response == null) {
                        throw new NullPointerException();
                    }

                    if (response.result == ApiResponse.RESULT_NG) {
                        ErrorUtil.handleErrorApi(mContext, response.error);
                        return;
                    }

                    mChildrenDelegate.onLoadChildrenTimeLine(response.getData(), isLoadTimelineFirst);
                    if (isLoadTimelineFirst) {
                        isLoadTimelineFirst = false;
                    }
                }, throwable -> ErrorUtil.handleException(mContext, throwable)));
    }

    public void onClickIndex(ArrayList<Object> mListChildrenHealth, int type) {
        if (infoChild == null) {
            return;
        }

        ArrayList<HealthCompact> listHealth = new ArrayList<>();

        if (type == AppConstant.TYPE_HEIGHT) {
            for (Object o : mListChildrenHealth) {
                if (o instanceof HealthTotal && ((HealthTotal) o).getWeight() != AppConstant.UNSPECIFIED) {
                    listHealth.add(0, new HealthCompact(((HealthTotal) o).getHeight(),
                            ((HealthTotal) o).getHeightState(),
                            ((HealthTotal) o).getMeasureTime()));

                    if (listHealth.size() == 12) {
                        break;
                    }
                }
            }
        } else {
            for (Object o : mListChildrenHealth) {
                if (o instanceof HealthTotal && ((HealthTotal) o).getWeight() != AppConstant.UNSPECIFIED) {
                    listHealth.add(0, new HealthCompact(((HealthTotal) o).getWeight(),
                            ((HealthTotal) o).getWeightState(),
                            ((HealthTotal) o).getMeasureTime()));
                    if (listHealth.size() == 12) {
                        break;
                    }
                }
            }
        }

        Intent intent = new Intent(mContext, HorizontalActivity.class);
        intent.putExtra(AppConstant.KEY_FRAGMENT, AppConstant.FRAGMENT_HEALTH_INDEX);

        Bundle bundle = new Bundle();
        bundle.putBoolean(AppConstant.KEY_GENDER, infoChild.getGender().equalsIgnoreCase("Nam"));
        bundle.putString(AppConstant.KEY_DOB, infoChild.getDOB());
        bundle.putParcelableArrayList(AppConstant.KEY_HEALTH, listHealth);
        bundle.putInt(AppConstant.KEY_HEALTH_TYPE, type);

        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
}
