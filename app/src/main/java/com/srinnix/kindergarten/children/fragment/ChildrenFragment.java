package com.srinnix.kindergarten.children.fragment;

import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
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
import com.srinnix.kindergarten.children.adapter.TimelineAdapter;
import com.srinnix.kindergarten.children.delegate.ChildrenDelegate;
import com.srinnix.kindergarten.children.presenter.ChildrenPresenter;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhtu on 2/21/2017.
 */

public class ChildrenFragment extends BaseFragment implements ChildrenDelegate {
    @BindView(R.id.imageview_icon)
    ImageView imvIcon;

    @BindView(R.id.textview_name)
    TextView tvName;

    @BindView(R.id.textview_DOB)
    TextView tvDOB;

    @BindView(R.id.textview_hobby)
    TextView tvHobby;

    @BindView(R.id.textview_characteristic)
    TextView tvCharacteristic;

    @BindView(R.id.recycler_view_children_list)
    RecyclerView rvListChildren;

    @BindView(R.id.layout_retry)
    RelativeLayout relRety;

    @BindView(R.id.textview_retry)
    TextView tvRetry;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.layout_unsigned_in)
    RelativeLayout relUnsignedIn;

    @BindView(R.id.imagview_unsigned_in)
    ImageView imvUnsignedIn;

    @BindView(R.id.cardview_profile)
    CardView cardViewProfile;

    private ChildrenPresenter mPresenter;
    private TimelineAdapter mTimelineAdapter;

    private ArrayList<Child> mListChildren;
    private ChildrenAdapter mChildrenAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_info_children;
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
    }

    private void initRecyclerView() {
        mListChildren = new ArrayList<>();
        mChildrenAdapter = new ChildrenAdapter(mListChildren, ChildrenAdapter.TYPE_LINEAR, new ChildrenAdapter.OnClickChildListener() {
            @Override
            public void onClick(String id) {
                rvListChildren.setVisibility(View.GONE);
                UiUtils.showProgressBar(pbLoading);

                mPresenter.getInfoChildren(id);
            }
        });
        rvListChildren.setAdapter(mChildrenAdapter);
        rvListChildren.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new ChildrenPresenter(this);
        return mPresenter;
    }

    @OnClick(R.id.button_login)
    public void onClickLogin() {
        mPresenter.addFragmentLogin();
    }

    @OnClick(R.id.layout_retry)
    public void onClickRetry() {
        relRety.setVisibility(View.GONE);
        UiUtils.showProgressBar(pbLoading);
        mPresenter.onClickRetry();
    }

    @Override
    public void onLoadListChildren(ArrayList<Child> childArrayList) {
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
        relRety.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadChildren(Child child) {
        UiUtils.hideProgressBar(pbLoading);
        cardViewProfile.setVisibility(View.VISIBLE);

        Glide.with(mContext)
                .load(child.getImage())
                .thumbnail(0.5f)
                .placeholder(R.drawable.dummy_image)
                .error(R.drawable.dummy_image)
                .into(imvIcon);

        tvName.setText(String.format("%s%s", child.getName(), child.getAka().isEmpty() ? "" : String.format(" (%s)", child.getAka())));
        tvDOB.setText(child.getDOB());
        tvHobby.setText(child.getHobby());
        tvCharacteristic.setText(child.getCharacteristic());

        // TODO: 3/31/2017 timeline
    }
}
