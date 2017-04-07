package com.srinnix.kindergarten.clazz.fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
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
import com.srinnix.kindergarten.custom.EndlessScrollDownListener;
import com.srinnix.kindergarten.custom.SpacesItemDecoration;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.Image;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.model.Teacher;
import com.srinnix.kindergarten.request.model.ClassResponse;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.UiUtils;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhtu on 2/16/2017.
 */

public class DetailClassFragment extends BaseFragment implements ClassDelegate, View.OnClickListener {
    @BindView(R.id.cardview_member_class)
    CardView cardViewMemberClass;

    @BindView(R.id.cardview_class_name)
    CardView cardViewClassName;

    @BindView(R.id.cardview_teachers)
    CardView cardViewTeachers;

    @BindView(R.id.cardview_image)
    CardView cardViewImage;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbClass;

    @BindView(R.id.imageview_retry)
    ImageView imvRetry;

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

    @BindView(R.id.textview_see_all)
    TextView tvSeeAll;

    @BindView(R.id.scrollview_detail_class)
    NestedScrollView scrollView;

    @BindView(R.id.recyclerview_image_class)
    RecyclerView rvImageClass;

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

    private ArrayList<Object> listImage;
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
    protected void initChildView() {
        cardViewClassName.setVisibility(View.GONE);
        cardViewTeachers.setVisibility(View.GONE);
        cardViewMemberClass.setVisibility(View.GONE);
        cardViewImage.setVisibility(View.GONE);

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

        if (!SharedPreUtils.getInstance(mContext).isUserSignedIn()) {
            imvChat1.setVisibility(View.GONE);
            imvChat2.setVisibility(View.GONE);
            imvChat3.setVisibility(View.GONE);
        }

        childArrayList = new ArrayList<>();
        childrenAdapter = new ChildrenAdapter(childArrayList, ChildrenAdapter.TYPE_GRID
                , id -> mPresenter.onClickChildViewHolder(id));
        rvMember.setLayoutManager(new GridLayoutManager(mContext, 4));
        rvMember.setAdapter(childrenAdapter);

        listImage = new ArrayList<>();
        listImage.add(new LoadingItem());
        mImageAdapter = new ImageAdapter(listImage, position -> {
            mPresenter.onClickImage(((Image) listImage.get(position)));
        });
        rvImageClass.setAdapter(mImageAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (listImage.get(position) instanceof LoadingItem) {
                    return 3;
                }
                return 1;
            }
        });
        rvImageClass.setLayoutManager(layoutManager);
        rvImageClass.addOnScrollListener(new EndlessScrollDownListener(layoutManager) {
            @Override
            public void onLoadMore() {
                DebugLog.i("onLoadMore() called");
                mPresenter.getImage(listImage);
            }
        });
        SpacesItemDecoration decoration = new SpacesItemDecoration(mImageAdapter, UiUtils.dpToPixel(mContext, 2), 3);
        rvImageClass.addItemDecoration(decoration);

        pbClass.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary)
                , PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new DetailClassPresenter(this);
        return mPresenter;
    }

    @OnClick({R.id.rel_teacher_1, R.id.rel_teacher_2, R.id.rel_teacher_3,
            R.id.textview_see_all})
    void onClickTeachers(View view) {
        switch (view.getId()) {
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
            case R.id.textview_see_all: {
                mPresenter.onClickSeeAll();
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        mPresenter.onClickChat(((int) v.getTag()));
    }

    @Override
    public void onLoadSuccess(ClassResponse classInfo) {
        UiUtils.hideProgressBar(pbClass);
        relRetry.setVisibility(View.GONE);

        if (classInfo == null) {
            imvRetry.setVisibility(View.VISIBLE);
            AlertUtils.showToast(mContext, R.string.error_common);
            return;
        }

        Glide.with(mContext)
                .load(R.drawable.logo_school)
                .placeholder(R.drawable.dummy_image)
                .error(R.drawable.dummy_image)
                .into(imvIconClass);

        tvClassName.setText(classInfo.getaClass().getName());

        ArrayList<Teacher> teacherArrayList = classInfo.getTeacherArrayList();
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

        if (mPresenter.isTeacher()) {
            if (!childArrayList.isEmpty()) {
                childArrayList.clear();
            }
            childArrayList.addAll(classInfo.getChildren());
            childrenAdapter.notifyItemRangeInserted(0, classInfo.getChildren().size());

            tvSeeAll.setText(String.format(Locale.getDefault(),
                    getString(R.string.see_all), classInfo.getaClass().getNumberMember()));
            cardViewMemberClass.setVisibility(View.VISIBLE);
        } else {
            cardViewMemberClass.setVisibility(View.GONE);
        }

        scrollView.smoothScrollTo(0, 0);

        cardViewClassName.setVisibility(View.VISIBLE);
        cardViewTeachers.setVisibility(View.VISIBLE);
        cardViewImage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadError(int resError) {
        UiUtils.hideProgressBar(pbClass);

        tvRetry.setText(resError);
        tvRetry.setVisibility(View.VISIBLE);
        imvRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadImage(ArrayList<Image> data) {
        int size = listImage.size();
        if (data.isEmpty()) {
            listImage.remove(size - 1);
            mImageAdapter.notifyItemRemoved(size - 1);
            return;
        }

        listImage.addAll(size - 1, data);
        mImageAdapter.notifyItemRangeInserted(size - 1, data.size());

        if (listImage.size() - data.size() == 1) {
            rvImageClass.smoothScrollToPosition(0);
        }
    }
}
