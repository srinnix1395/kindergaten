package com.srinnix.kindergarten.clazz.fragment;

import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
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
import com.srinnix.kindergarten.clazz.presenter.ClassPresenter;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.Teacher;
import com.srinnix.kindergarten.request.model.ClassResponse;
import com.srinnix.kindergarten.util.AlertUtils;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhtu on 2/16/2017.
 */

public class ClassFragment extends BaseFragment implements ClassDelegate, View.OnClickListener {
    @BindView(R.id.cardview_member_class)
    CardView cardViewMemberClass;

    @BindView(R.id.cardview_class_name)
    CardView cardViewClassName;

    @BindView(R.id.cardview_teachers)
    CardView cardViewTeachers;

    @BindView(R.id.cardview_image)
    CardView cardViewImage;

    @BindView(R.id.progressbar_class)
    ProgressBar pbClass;

    @BindView(R.id.imageview_retry)
    ImageView imvRetry;

    @BindView(R.id.recyclerview_member_class)
    RecyclerView rvMember;

    @BindView(R.id.textview_class_name)
    TextView tvClassName;

    @BindView(R.id.textview_see_all)
    TextView tvSeeAll;

    ImageView imvIcon1;
    TextView tvName1;
    ImageView imvChat1;

    ImageView imvIcon2;
    TextView tvName2;
    ImageView imvChat2;

    ImageView imvIcon3;
    TextView tvName3;
    ImageView imvChat3;

    private ClassPresenter mPresenter;

    private ArrayList<String> listImage;
    private ImageAdapter imageAdapter;

    private ArrayList<Child> childArrayList;
    private ChildrenAdapter childrenAdapter;

    public static ClassFragment newInstance() {
        return new ClassFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_class;
    }

    @Override
    protected void initChildView() {
        cardViewClassName.setVisibility(View.GONE);
        cardViewTeachers.setVisibility(View.GONE);
        cardViewMemberClass.setVisibility(View.GONE);
        cardViewImage.setVisibility(View.GONE);
        imvRetry.setVisibility(View.GONE);

        RelativeLayout relTeacher1 = (RelativeLayout) mView.findViewById(R.id.rel_teacher_1);
        imvIcon1 = (ImageView) relTeacher1.findViewById(R.id.imageview_icon);
        tvName1 = (TextView) relTeacher1.findViewById(R.id.textview_teacher_name);
        imvChat1 = (ImageView) relTeacher1.findViewById(R.id.imageview_chat);
        imvChat1.setTag(1);

        RelativeLayout relTeacher2 = (RelativeLayout) mView.findViewById(R.id.rel_teacher_2);
        imvIcon2 = (ImageView) relTeacher2.findViewById(R.id.imageview_icon);
        tvName2 = (TextView) relTeacher2.findViewById(R.id.textview_teacher_name);
        imvChat2 = (ImageView) relTeacher2.findViewById(R.id.imageview_chat);
        imvChat2.setTag(2);

        RelativeLayout relTeacher3 = (RelativeLayout) mView.findViewById(R.id.rel_teacher_3);
        imvIcon3 = (ImageView) relTeacher3.findViewById(R.id.imageview_icon);
        tvName3 = (TextView) relTeacher3.findViewById(R.id.textview_teacher_name);
        imvChat3 = (ImageView) relTeacher3.findViewById(R.id.imageview_chat);
        imvChat3.setTag(3);

        imvChat1.setOnClickListener(this);
        imvChat2.setOnClickListener(this);
        imvChat3.setOnClickListener(this);

        childArrayList = new ArrayList<>();
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childArrayList.add(new Child("223", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTM-2rpkbyJ0vLCf3hxFUTUJVeBVG22uNZvQ9KeGRh0ryieDWmejg", "dál", "adslf", "alsdfjk", "ádkfj"));
        childrenAdapter = new ChildrenAdapter(childArrayList, ChildrenAdapter.TYPE_GRID
                , id -> mPresenter.onClickChildViewHolder(id));
        rvMember.setLayoutManager(new GridLayoutManager(mContext, 4));
        rvMember.setAdapter(childrenAdapter);

        pbClass.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary)
                , PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new ClassPresenter(this);
        return mPresenter;
    }

    @OnClick({R.id.rel_teacher_1, R.id.rel_teacher_2, R.id.rel_teacher_3,
            R.id.textview_see_all})
    void onClickTeachers(View view) {
        switch (view.getId()) {
            case R.id.rel_teacher_1: {
                mPresenter.onClickTeacher(1);
                break;
            }
            case R.id.rel_teacher_2: {
                mPresenter.onClickTeacher(2);
                break;
            }
            case R.id.rel_teacher_3: {
                mPresenter.onClickTeacher(3);
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
    public void displayInfo(ClassResponse classInfo) {
        pbClass.setEnabled(false);
        pbClass.setVisibility(View.GONE);

        if (classInfo == null) {
            imvRetry.setVisibility(View.VISIBLE);
            AlertUtils.showToast(mContext, R.string.commonError);
            return;
        }

        tvClassName.setText(classInfo.getaClass().getName());

        ArrayList<Teacher> teacherArrayList = classInfo.getaClass().getTeacherArrayList();
        tvName1.setText(teacherArrayList.get(0).getName());
        Glide.with(mContext)
                .load(teacherArrayList.get(0).getImage())
                .into(imvIcon1);

        tvName2.setText(teacherArrayList.get(1).getName());
        Glide.with(mContext)
                .load(teacherArrayList.get(1).getImage())
                .into(imvIcon2);

        tvName3.setText(teacherArrayList.get(2).getName());
        Glide.with(mContext)
                .load(teacherArrayList.get(2).getImage())
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

        cardViewClassName.setVisibility(View.VISIBLE);
        cardViewTeachers.setVisibility(View.VISIBLE);
        cardViewImage.setVisibility(View.VISIBLE);
        // TODO: 3/2/2017 animation
    }
}
