package com.srinnix.kindergarten.clazz.fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Teacher;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.request.remote.ApiService;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhtu on 3/21/2017.
 */

public class TeacherInfoDialogFragment extends BottomSheetDialogFragment {
    @BindView(R.id.layout_info)
    RelativeLayout relInfo;

    @BindView(R.id.imageview_icon)
    ImageView imvIcon;

    @BindView(R.id.textview_name)
    TextView tvName;

    @BindView(R.id.textview_class_name)
    TextView tvClassName;

    @BindView(R.id.textview_achievement)
    TextView tvAchievement;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.textview_retry)
    TextView tvRetry;

    @BindView(R.id.layout_retry)
    RelativeLayout relRetry;

    @BindView(R.id.textview_DOB)
    TextView tvDOB;

    private String teacherId;
    private Disposable mDisposable;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_info, container, false);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initData();
        initView();
    }

    private void initView() {
        relInfo.setVisibility(View.GONE);

        pbLoading.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary)
                , PorterDuff.Mode.SRC_ATOP);
    }

    private void initData() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            teacherId = bundle.getString(AppConstant.KEY_ID);
            getTeacherInfo();
        }
    }

    private void getTeacherInfo(){
        if (!ServiceUtils.isNetworkAvailable(getContext())) {
            handleException(R.string.cant_connect);
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        mDisposable = apiService.getTeacherInfo(teacherId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response == null) {
                        throw new NullPointerException();
                    }

                    if (response.result == ApiResponse.RESULT_NG) {
                        ErrorUtil.handleErrorApi(getContext(), response.error);
                        return;
                    }

                    displayInfo(response.getData());
                }, throwable -> {
                    DebugLog.e(throwable.getMessage());

                    handleException(R.string.error_common);
                });
    }

    private void handleException(int resError) {
        UiUtils.hideProgressBar(pbLoading);

        tvRetry.setText(resError);
        UiUtils.showView(relRetry);
    }

    @OnClick(R.id.layout_retry)
    public void onClickRetry() {
        UiUtils.hideView(relRetry);
        UiUtils.showProgressBar(pbLoading);

        getTeacherInfo();
    }

    private void displayInfo(Teacher teacher) {
        UiUtils.hideProgressBar(pbLoading);
        relRetry.setVisibility(View.GONE);

        relInfo.setVisibility(View.VISIBLE);
        Glide.with(getContext())
                .load(teacher.getImage())
                .crossFade()
                .placeholder(R.drawable.dummy_image)
                .error(R.drawable.image_teacher)
                .into(imvIcon);

        tvName.setText(teacher.getName());
        tvDOB.setText(teacher.getDOB());
        tvClassName.setText(teacher.getClassName());
        tvAchievement.setText(teacher.getAchievement());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
}
