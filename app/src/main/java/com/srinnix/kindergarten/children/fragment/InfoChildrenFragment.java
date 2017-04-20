package com.srinnix.kindergarten.children.fragment;

import android.graphics.PorterDuff;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
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
import com.srinnix.kindergarten.children.adapter.HealthChildrenAdapter;
import com.srinnix.kindergarten.children.delegate.ChildrenDelegate;
import com.srinnix.kindergarten.children.presenter.InfoChildrenPresenter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.custom.EndlessScrollDownListener;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.Health;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.util.DebugLog;
import com.srinnix.kindergarten.util.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhtu on 2/21/2017.
 */

public class InfoChildrenFragment extends BaseFragment implements ChildrenDelegate {
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

    @BindView(R.id.recycler_view_timeline)
    RecyclerView rvHealthChildren;

    @BindView(R.id.layout_retry)
    RelativeLayout relRetry;

    @BindView(R.id.textview_retry)
    TextView tvRetry;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.layout_profile)
    CoordinatorLayout layoutProfile;

    @BindView(R.id.appbar_layout)
    AppBarLayout appBarLayout;

    private InfoChildrenPresenter mPresenter;

    private ArrayList<Object> mListChildrenHealth;
    private HealthChildrenAdapter mHealthChildrenAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_info_children;
    }

    @Override
    protected void initChildView() {
        pbLoading.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary),
                PorterDuff.Mode.SRC_ATOP);

        mListChildrenHealth = new ArrayList<>();
        mListChildrenHealth.add(new LoadingItem());
        mHealthChildrenAdapter = new HealthChildrenAdapter(mListChildrenHealth, position -> {
            mPresenter.onClickIndex(mListChildrenHealth, position);
        });
        rvHealthChildren.setAdapter(mHealthChildrenAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvHealthChildren.setLayoutManager(layoutManager);
        EndlessScrollDownListener scrollDownListener = new EndlessScrollDownListener(layoutManager) {
            @Override
            public void onLoadMore() {
                DebugLog.i("on load more");
                int size = mListChildrenHealth.size();
                if (size > 1 && mListChildrenHealth.get(size - 2) instanceof Health) {
                    mPresenter.getTimelineChildren(((Health) mListChildrenHealth.get(size - 1)).getCreatedAt());
                }
            }
        };
        rvHealthChildren.addOnScrollListener(scrollDownListener);
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new InfoChildrenPresenter(this);
        return mPresenter;
    }

    @OnClick(R.id.layout_retry)
    public void onClickRetry() {
        relRetry.setVisibility(View.GONE);
        UiUtils.showProgressBar(pbLoading);

        mPresenter.getInfoChildren();
        mPresenter.getTimelineChildren(System.currentTimeMillis());
    }

    @Override
    public void onLoadFail(int resError) {
        UiUtils.hideProgressBar(pbLoading);

        tvRetry.setText(resError);
        relRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadChildren(Child child) {
        UiUtils.hideProgressBar(pbLoading);
        layoutProfile.setVisibility(View.VISIBLE);

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
    }

    @Override
    public void onLoadChildrenTimeLine(ArrayList<Health> data) {
        int sizeNewData = data.size();
        if (sizeNewData > 0) {
            data.get(sizeNewData - 1).setDisplayLine(false);
        }

        int sizeTotal = mListChildrenHealth.size();

        if (sizeNewData < AppConstant.ITEM_HEALTH_PER_PAGE) {
            if (!mListChildrenHealth.isEmpty() && mListChildrenHealth.get(sizeTotal - 1) instanceof LoadingItem) {
                mListChildrenHealth.remove(sizeTotal - 1);
                mHealthChildrenAdapter.notifyItemRemoved(sizeTotal - 1);
            }
            if (sizeNewData > 0) {
                sizeTotal = mListChildrenHealth.size();

                if (sizeTotal > 1 && mListChildrenHealth.get(sizeTotal - 1) instanceof Health) {
                    ((Health) mListChildrenHealth.get(sizeTotal - 1)).setDisplayLine(true);
                    mHealthChildrenAdapter.notifyItemChanged(sizeTotal - 1, true);
                }

                mListChildrenHealth.addAll(data);
                mHealthChildrenAdapter.notifyItemRangeInserted(sizeTotal, data.size());
            }
        } else {
            if (sizeTotal > 1 && mListChildrenHealth.get(sizeTotal - 1) instanceof Health) {
                ((Health) mListChildrenHealth.get(sizeTotal - 1)).setDisplayLine(true);
                mHealthChildrenAdapter.notifyItemChanged(sizeTotal - 1, true);
            }

            mListChildrenHealth.addAll(sizeTotal - 1, data);
            mHealthChildrenAdapter.notifyItemRangeInserted(sizeTotal - 1, data.size());
        }
    }
}
