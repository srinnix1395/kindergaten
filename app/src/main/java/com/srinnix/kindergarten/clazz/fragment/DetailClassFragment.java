package com.srinnix.kindergarten.clazz.fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.children.adapter.ChildrenAdapter;
import com.srinnix.kindergarten.clazz.adapter.ImageAdapter;
import com.srinnix.kindergarten.clazz.delegate.ClassDelegate;
import com.srinnix.kindergarten.clazz.presenter.DetailClassPresenter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.custom.EndlessScrollDownListener;
import com.srinnix.kindergarten.custom.SpacesItemDecoration;
import com.srinnix.kindergarten.messageeventbus.MessageLoginSuccessfully;
import com.srinnix.kindergarten.messageeventbus.MessageLogout;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.Image;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.MessageImagePostSuccessfully;
import com.srinnix.kindergarten.model.Teacher;
import com.srinnix.kindergarten.request.model.ClassResponse;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.UiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhtu on 2/16/2017.
 */

public class DetailClassFragment extends BaseFragment implements ClassDelegate, View.OnClickListener {
    @BindView(R.id.progressbar_loading)
    ProgressBar pbClass;

    @BindView(R.id.textview_retry)
    TextView tvRetry;

    @BindView(R.id.layout_retry)
    RelativeLayout relRetry;

    @BindView(R.id.recyclerview_member_class)
    RecyclerView rvMember;

    @BindView(R.id.imageview_icon)
    ImageView imvIconClass;

    @BindView(R.id.textview_class_name)
    TextView tvClassName;

    @BindView(R.id.recyclerview_image_class)
    RecyclerView rvListImage;

    @BindView(R.id.view_line_member)
    View viewLineMember;

    @BindView(R.id.textview_member)
    TextView tvMember;

    @BindView(R.id.layout_info)
    CoordinatorLayout layoutInfo;

    @BindView(R.id.image_children)
    ImageView imvMember;

    @BindView(R.id.textview_learn_schedule)
    TextView tvLearnSchedule;

    @BindView(R.id.floatbutton_post)
    FloatingActionButton fabPost;

    ImageView imvIcon1;
    TextView tvName1;
    ImageView imvChat1;

    ImageView imvIcon2;
    TextView tvName2;
    ImageView imvChat2;

    ImageView imvIcon3;
    TextView tvName3;
    ImageView imvChat3;

    private DetailClassPresenter mPresenter;

    private ArrayList<Object> mListImage;
    private ImageAdapter mImageAdapter;

    private ArrayList<Child> childArrayList;
    private ChildrenAdapter childrenAdapter;

    public static DetailClassFragment newInstance(Bundle args) {
        DetailClassFragment fragment = new DetailClassFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail_class;
    }

    @Override
    protected void initData() {
        childArrayList = new ArrayList<>();
        childrenAdapter = new ChildrenAdapter(childArrayList, ChildrenAdapter.TYPE_GRID
                , position -> mPresenter.onClickChildViewHolder(childArrayList.get(position).getId()));

        mListImage = new ArrayList<>();
        mListImage.add(new LoadingItem());
        mImageAdapter = new ImageAdapter(mListImage,
                (position, sharedTransitionView) -> mPresenter.onClickImage(DetailClassFragment.this, sharedTransitionView, ((Image) mListImage.get(position))));
    }

    @Override
    protected void initChildView() {
        RelativeLayout relTeacher1 = (RelativeLayout) mView.findViewById(R.id.rel_teacher_1);
        imvIcon1 = (ImageView) relTeacher1.findViewById(R.id.imageview_icon);
        tvName1 = (TextView) relTeacher1.findViewById(R.id.textview_teacher_name);
        imvChat1 = (ImageView) relTeacher1.findViewById(R.id.imageview_chat);
        imvChat1.setTag(0);

        RelativeLayout relTeacher2 = (RelativeLayout) mView.findViewById(R.id.rel_teacher_2);
        imvIcon2 = (ImageView) relTeacher2.findViewById(R.id.imageview_icon);
        tvName2 = (TextView) relTeacher2.findViewById(R.id.textview_teacher_name);
        imvChat2 = (ImageView) relTeacher2.findViewById(R.id.imageview_chat);
        imvChat2.setTag(1);

        RelativeLayout relTeacher3 = (RelativeLayout) mView.findViewById(R.id.rel_teacher_3);
        imvIcon3 = (ImageView) relTeacher3.findViewById(R.id.imageview_icon);
        tvName3 = (TextView) relTeacher3.findViewById(R.id.textview_teacher_name);
        imvChat3 = (ImageView) relTeacher3.findViewById(R.id.imageview_chat);
        imvChat3.setTag(2);

        imvChat1.setOnClickListener(this);
        imvChat2.setOnClickListener(this);
        imvChat3.setOnClickListener(this);

        if (!SharedPreUtils.getInstance(mContext).isUserSignedIn() ||
                SharedPreUtils.getInstance(mContext).getAccountType() != AppConstant.ACCOUNT_PARENTS) {
            UiUtils.hideView(imvChat1);
            UiUtils.hideView(imvChat2);
            UiUtils.hideView(imvChat3);
        }

        if (SharedPreUtils.getInstance(mContext).isUserSignedIn()) {
            tvLearnSchedule.setVisibility(View.VISIBLE);
        } else {
            tvLearnSchedule.setVisibility(View.GONE);
        }

        rvMember.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        rvMember.setAdapter(childrenAdapter);

        rvListImage.setAdapter(mImageAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mListImage.get(position) instanceof LoadingItem) {
                    return 3;
                }
                return 1;
            }
        });
        rvListImage.setLayoutManager(layoutManager);
        rvListImage.addOnScrollListener(new EndlessScrollDownListener(layoutManager) {
            Handler handlerFab = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    fabPost.animate()
                            .alpha(1f)
                            .translationY(0)
                            .setInterpolator(new FastOutSlowInInterpolator())
                            .setDuration(200)
                            .start();
                }
            };

            @Override
            public void onStateChanged(int newState) {
                super.onStateChanged(newState);
                if (fabPost.getVisibility() != View.VISIBLE) {
                    return;
                }
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    handlerFab.removeCallbacks(runnable);
                    fabPost.animate()
                            .translationY(fabPost.getHeight() / 2)
                            .alpha(0f)
                            .setInterpolator(new FastOutSlowInInterpolator())
                            .setDuration(200)
                            .start();
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    handlerFab.postDelayed(runnable, 1000);
                }
            }

            @Override
            public void onLoadMore() {
                DebugLog.i("onLoadMore() called");
                int size = mListImage.size();
                if (mListImage.get(size - 1) instanceof Image) {
                    return;
                }

                mPresenter.getImage(mListImage);
            }
        });
        SpacesItemDecoration decoration = new SpacesItemDecoration(mContext, mImageAdapter, 2, 3, false);
        rvListImage.addItemDecoration(decoration);

        pbClass.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary)
                , PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new DetailClassPresenter(this);
        return mPresenter;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void onEventLoginSuccesfully(MessageLoginSuccessfully message) {
        if (SharedPreUtils.getInstance(mContext).getAccountType() == AppConstant.ACCOUNT_PARENTS) {
            UiUtils.showView(imvChat1);
            UiUtils.showView(imvChat2);
            UiUtils.showView(imvChat3);
        }

        UiUtils.showView(tvLearnSchedule);

        // TODO: 4/20/2017 get list children
    }

    @Subscribe
    public void onEventLogOut(MessageLogout message) {
        tvLearnSchedule.setVisibility(View.GONE);

        UiUtils.hideView(imvChat1);
        UiUtils.hideView(imvChat2);
        UiUtils.hideView(imvChat3);

        if (rvMember.getVisibility() == View.VISIBLE) {
            UiUtils.hideView(imvMember);
            UiUtils.hideView(tvMember);
            UiUtils.hideView(rvMember);
            UiUtils.hideView(viewLineMember);

            childArrayList.clear();
            childrenAdapter.notifyDataSetChanged();
        }

        UiUtils.hideView(fabPost);
    }

    @Subscribe
    public void onEventPostSuccessfully(MessageImagePostSuccessfully message) {
        mListImage.addAll(0, message.data);
        mImageAdapter.notifyItemRangeInserted(0, message.data.size());

        rvListImage.scrollToPosition(0);
    }

    @OnClick({R.id.rel_teacher_1, R.id.rel_teacher_2, R.id.rel_teacher_3,
            R.id.textview_timetable1, R.id.textview_study_timetable,
            R.id.layout_retry, R.id.floatbutton_post, R.id.textview_learn_schedule})
    void onClickTeachers(View view) {
        switch (view.getId()) {
            case R.id.layout_retry: {
                UiUtils.hideView(relRetry);
                UiUtils.showProgressBar(pbClass);
                mPresenter.onClickRetry();
                break;
            }
            case R.id.rel_teacher_1: {
                mPresenter.onClickTeacher(getChildFragmentManager(), 0);
                break;
            }
            case R.id.rel_teacher_2: {
                mPresenter.onClickTeacher(getChildFragmentManager(), 1);
                break;
            }
            case R.id.rel_teacher_3: {
                mPresenter.onClickTeacher(getChildFragmentManager(), 2);
                break;
            }
            case R.id.textview_timetable1: {
                mPresenter.onClickTimeTable();
                break;
            }
            case R.id.textview_study_timetable: {
                mPresenter.onClickStudyTimeTable();
                break;
            }
            case R.id.textview_learn_schedule: {
                mPresenter.onClickLearnSchedule();
                break;
            }
            case R.id.floatbutton_post: {
                mPresenter.onClickPost();
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        mPresenter.onClickChat(((int) v.getTag()));
    }

    @Override
    public void onLoadSuccess(ClassResponse data) {
        UiUtils.hideProgressBar(pbClass);

        if (data == null) {
            tvRetry.setText(R.string.error_common);
            UiUtils.showView(relRetry);
            return;
        }

        UiUtils.hideView(relRetry);
        UiUtils.showView(layoutInfo);

        Glide.with(mContext)
                .load(R.drawable.logo_school)
                .placeholder(R.drawable.dummy_image)
                .error(R.drawable.dummy_image)
                .into(imvIconClass);

        if (data.getaClass() == null) {
            return;
        }
        tvClassName.setText(data.getaClass().getName());

        ArrayList<Teacher> teacherArrayList = data.getTeacherArrayList();
        if (teacherArrayList != null && !teacherArrayList.isEmpty()) {
            tvName1.setText(teacherArrayList.get(0).getName());
            Glide.with(mContext)
                    .load(teacherArrayList.get(0).getImage())
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.dummy_image)
                    .error(R.drawable.image_teacher)
                    .into(imvIcon1);

            tvName2.setText(teacherArrayList.get(1).getName());
            Glide.with(mContext)
                    .load(teacherArrayList.get(1).getImage())
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.dummy_image)
                    .error(R.drawable.image_teacher)
                    .into(imvIcon2);

            tvName3.setText(teacherArrayList.get(2).getName());
            Glide.with(mContext)
                    .load(teacherArrayList.get(2).getImage())
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.dummy_image)
                    .error(R.drawable.image_teacher)
                    .into(imvIcon3);
        } else {
            UiUtils.hideView(imvChat1);
            UiUtils.hideView(imvChat2);
            UiUtils.hideView(imvChat3);
        }

        if (mPresenter.isTeacher()) {
            if (!data.getChildren().isEmpty()) {
                childArrayList.addAll(data.getChildren());
                childrenAdapter.notifyItemRangeInserted(0, data.getChildren().size());
            }

            tvMember.setText(String.format(Locale.getDefault(),
                    getString(R.string.list_member), data.getaClass().getNumberMember()));
            UiUtils.showView(imvMember);
            UiUtils.showView(tvMember);
            UiUtils.showView(rvMember);
            UiUtils.showView(viewLineMember);
        } else {
            UiUtils.hideView(imvMember);
            UiUtils.hideView(tvMember);
            UiUtils.hideView(rvMember);
            UiUtils.hideView(viewLineMember);
        }

        if (SharedPreUtils.getInstance(mContext).getAccountType() == AppConstant.ACCOUNT_TEACHERS &&
                SharedPreUtils.getInstance(mContext).getClassId().equals(data.getaClass().getId())) {
            UiUtils.showView(fabPost);
        }
    }

    @Override
    public void onLoadError(int resError) {
        UiUtils.hideProgressBar(pbClass);

        tvRetry.setText(resError);
        UiUtils.showView(relRetry);
    }

    @Override
    public void onLoadImage(ArrayList<Image> data, boolean isLoadImageFirst) {
        int sizeNewData = data.size();
        int sizeTotal = mListImage.size();

        if (sizeNewData < AppConstant.ITEM_IMAGE_PER_PAGE) {
            if (!mListImage.isEmpty() && mListImage.get(sizeTotal - 1) instanceof LoadingItem) {
                mListImage.remove(sizeTotal - 1);
                mImageAdapter.notifyItemRemoved(sizeTotal - 1);
            }
            if (sizeNewData > 0) {
                sizeTotal = mListImage.size();

                mListImage.addAll(data);
                mImageAdapter.notifyItemRangeInserted(sizeTotal, data.size());
            }
        } else {
            mListImage.addAll(sizeTotal - 1, data);
            mImageAdapter.notifyItemRangeInserted(sizeTotal - 1, data.size());
        }

        if (isLoadImageFirst && !mListImage.isEmpty()) {
            rvListImage.scrollToPosition(0);
        }
    }
}
