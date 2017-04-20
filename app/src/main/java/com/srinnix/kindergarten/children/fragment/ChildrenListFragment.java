package com.srinnix.kindergarten.children.fragment;

import android.graphics.PorterDuff;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import com.srinnix.kindergarten.children.delegate.ChildrenListDelegate;
import com.srinnix.kindergarten.children.presenter.ChildrenListPresenter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.messageeventbus.MessageLoginSuccessfully;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.UiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhtu on 4/20/2017.
 */

public class ChildrenListFragment extends BaseFragment implements ChildrenListDelegate {
    @BindView(R.id.recycler_view_children_list)
    RecyclerView rvListChildren;

    @BindView(R.id.layout_retry)
    RelativeLayout relRetry;

    @BindView(R.id.textview_retry)
    TextView tvRetry;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.layout_unsigned_in)
    RelativeLayout relUnsignedIn;

    @BindView(R.id.imagview_unsigned_in)
    ImageView imvUnsignedIn;

    private ArrayList<Child> mListChildren;
    private ChildrenAdapter mChildrenAdapter;

    private ChildrenListPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_children_list;
    }

    @Override
    protected void initChildView() {
        pbLoading.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary),
                PorterDuff.Mode.SRC_ATOP);

        if (!SharedPreUtils.getInstance(mContext).isUserSignedIn()) {
            UiUtils.hideProgressBar(pbLoading);
            Glide.with(mContext)
                    .load(R.drawable.kid_drawing)
                    .into(imvUnsignedIn);

            relUnsignedIn.setVisibility(View.VISIBLE);
            return;
        }

        initRecyclerView();
        mPresenter.getListChildren(this, pbLoading);
    }

    private void initRecyclerView() {
        mListChildren = new ArrayList<>();
        mChildrenAdapter = new ChildrenAdapter(mListChildren, ChildrenAdapter.TYPE_LINEAR, position -> {
            mPresenter.openFragmentInfoChild(this, mListChildren.get(position).getId());
        });
        rvListChildren.setAdapter(mChildrenAdapter);
        rvListChildren.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new ChildrenListPresenter(this);
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

    @OnClick(R.id.layout_retry)
    void onClickRetry() {
        relRetry.setVisibility(View.GONE);
        UiUtils.showProgressBar(pbLoading);
        mPresenter.getListChildren(this, pbLoading);
    }

    @OnClick(R.id.button_login)
    public void onClickLogin() {
        mPresenter.addFragmentLogin();
    }

    @Subscribe
    public void onEventLoginSuccessfully(MessageLoginSuccessfully messsage) {
        if (SharedPreUtils.getInstance(mContext).isUserSignedIn()) {
            initRecyclerView();

            imvUnsignedIn.setImageDrawable(null);
            relUnsignedIn.setVisibility(View.GONE);

            UiUtils.showProgressBar(pbLoading);

            mPresenter.getListChildren(this, pbLoading);
        }
    }

    @Override
    public void onLoadListChildren(ArrayList<Child> childArrayList) {
        UiUtils.hideProgressBar(pbLoading);

        if (!mListChildren.isEmpty()) {
            mListChildren.clear();
        }
        mListChildren.addAll(childArrayList);
        mChildrenAdapter.notifyItemRangeInserted(0, childArrayList.size());

        rvListChildren.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadFail(int resError) {
        UiUtils.hideProgressBar(pbLoading);

        tvRetry.setText(resError);
        relRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        InfoChildrenFragment fragment = (InfoChildrenFragment) getChildFragmentManager().findFragmentByTag(String.valueOf(AppConstant.FRAGMENT_INFO_CHILDREN));

        if (fragment != null && !mListChildren.isEmpty()) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.remove(fragment);
            transaction.commit();
            fragment.onDestroy();
            return;
        }

        super.onBackPressed();
    }
}
