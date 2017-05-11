package com.srinnix.kindergarten.setting.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.children.adapter.ChildrenAdapter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.User;
import com.srinnix.kindergarten.setting.delegate.AccountDelegate;
import com.srinnix.kindergarten.setting.presenter.AccountPresenter;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.StringUtil;
import com.srinnix.kindergarten.util.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhtu on 4/20/2017.
 */

public class AccountFragment extends BaseFragment implements AccountDelegate {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.layout_retry)
    RelativeLayout layoutRetry;

    @BindView(R.id.textview_retry)
    TextView tvRetry;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.imageview_icon)
    ImageView imvIcon;

    @BindView(R.id.textview_name)
    TextView tvName;

    @BindView(R.id.textview_DOB_title)
    TextView tvDOBtitle;

    @BindView(R.id.textview_DOB)
    TextView tvDOB;

    @BindView(R.id.textview_gender)
    TextView tvGender;

    @BindView(R.id.textview_email)
    TextView tvEmail;

    @BindView(R.id.textview_account)
    TextView tvAccountType;

    @BindView(R.id.textview_phone_number)
    TextView tvPhoneNumber;

    @BindView(R.id.image_work)
    ImageView imvWork;

    @BindView(R.id.textview_work)
    TextView tvWork;

    @BindView(R.id.textview_class_title)
    TextView tvClassTitle;

    @BindView(R.id.textview_class)
    TextView tvClass;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    @BindView(R.id.recycler_view_children)
    RecyclerView rvChildren;

    @BindView(R.id.cardview_icon)
    CardView cardViewIcon;

    private ArrayList<Child> listChild;
    private ChildrenAdapter childrenAdapter;

    private AccountPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_account;
    }

    @Override
    protected void initData() {
        super.initData();
        listChild = new ArrayList<>();
        childrenAdapter = new ChildrenAdapter(listChild, ChildrenAdapter.TYPE_LINEAR, position -> {

        });
    }

    @Override
    protected void initChildView() {
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.inflateMenu(R.menu.menu_account_fragment);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_item_edit: {

                    break;
                }
                case R.id.menu_item_change_password: {
                    mPresenter.onClickChangePassword(getChildFragmentManager());
                    break;
                }
                case R.id.menu_item_sign_out: {

                    break;
                }
                case R.id.menu_item_save: {

                    break;
                }
            }
            return false;
        });

        rvChildren.setLayoutManager(new LinearLayoutManager(mContext));
        rvChildren.setAdapter(childrenAdapter);

        pbLoading.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary)
                , PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new AccountPresenter(this);
        return mPresenter;
    }

    @OnClick(R.id.layout_retry)
    public void onClickRetry() {
        UiUtils.hideView(layoutRetry);

        UiUtils.showProgressBar(pbLoading);
        mPresenter.getInfo();
    }

    @Override
    public void onFail(int resError) {
        UiUtils.hideProgressBar(pbLoading);

        tvRetry.setText(resError);
        UiUtils.showView(layoutRetry);
    }

    @Override
    public void onSuccess(User user, ArrayList<Child> children) {
        UiUtils.hideProgressBar(pbLoading);
        UiUtils.showView(scrollView);

        toolbar.setTitle(user.getName());
        tvName.setText(user.getName());
        tvGender.setText(user.getGender());
        tvEmail.setText(user.getEmail());
        tvAccountType.setText(StringUtil.getAccountType(user.getAccountType()));
        tvPhoneNumber.setText(user.getPhoneNumber() == null ? "Chưa có" : user.getPhoneNumber());
        UiUtils.showView(cardViewIcon);
        tvEmail.setText(SharedPreUtils.getInstance(mContext).getEmail());

        if (user.getAccountType() == AppConstant.ACCOUNT_PARENTS) {
            if (user.getImage() != null) {
                Glide.with(mContext)
                        .load(user.getImage())
                        .thumbnail(0.5f)
                        .placeholder(R.drawable.dummy_image)
                        .error(R.drawable.image_parent)
                        .into(imvIcon);
            } else {
                imvIcon.setImageResource(R.drawable.ic_user);
            }

            imvWork.setImageResource(R.drawable.image_children_circle);
            tvWork.setText(R.string.children);

            listChild.addAll(children);
            childrenAdapter.notifyItemRangeInserted(0, children.size());

            UiUtils.hideView(tvDOB);
            UiUtils.hideView(tvDOBtitle);

            UiUtils.hideView(tvClass);
            UiUtils.hideView(tvClassTitle);
        } else {
            Glide.with(mContext)
                    .load(user.getImage())
                    .thumbnail(0.5f)
                    .placeholder(R.drawable.dummy_image)
                    .error(R.drawable.image_teacher)
                    .into(imvIcon);

            tvDOB.setText(user.getDob());

            imvWork.setImageResource(R.drawable.image_class);
            tvWork.setText(R.string.work);

            tvClass.setText(user.getClassName());

            UiUtils.hideView(rvChildren);
        }
    }
}
