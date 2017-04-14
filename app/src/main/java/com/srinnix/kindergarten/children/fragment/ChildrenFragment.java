package com.srinnix.kindergarten.children.fragment;

import android.graphics.PorterDuff;
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
import com.srinnix.kindergarten.children.adapter.HealthChildrenAdapter;
import com.srinnix.kindergarten.children.adapter.viewholder.HealthChildrenViewHolder;
import com.srinnix.kindergarten.children.delegate.ChildrenDelegate;
import com.srinnix.kindergarten.children.presenter.ChildrenPresenter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.custom.EndlessScrollDownListener;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.HealthTotalChildren;
import com.srinnix.kindergarten.model.LoadingItem;
import com.srinnix.kindergarten.util.DebugLog;
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

    @BindView(R.id.recycler_view_timeline)
    RecyclerView rvTimeline;

    @BindView(R.id.layout_retry)
    RelativeLayout relRetry;

    @BindView(R.id.textview_retry)
    TextView tvRetry;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.layout_unsigned_in)
    RelativeLayout relUnsignedIn;

    @BindView(R.id.view_line)
    View viewLine;

    @BindView(R.id.imagview_unsigned_in)
    ImageView imvUnsignedIn;

    @BindView(R.id.layout_profile)
    RelativeLayout layoutProfile;

    @BindView(R.id.layout_timeline)
    RelativeLayout layoutTimeline;

    private ChildrenPresenter mPresenter;

    private ArrayList<Object> mListChildrenHealth;
    private HealthChildrenAdapter mHealthChildrenAdapter;

    private ArrayList<Child> mListChildren;
    private ChildrenAdapter mChildrenAdapter;
    private boolean isDisplayInfo = false;

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
        mChildrenAdapter = new ChildrenAdapter(mListChildren, ChildrenAdapter.TYPE_LINEAR, position -> {
            rvListChildren.setVisibility(View.GONE);
            UiUtils.showProgressBar(pbLoading);

            mPresenter.setIdChild(mListChildren.get(position).getId());
            mPresenter.getInfoChildren(mListChildren.get(position).getId());
            mPresenter.getTimelineChildren(System.currentTimeMillis());
        });
        rvListChildren.setAdapter(mChildrenAdapter);
        rvListChildren.setLayoutManager(new LinearLayoutManager(mContext));

        mListChildrenHealth = new ArrayList<>();
        mListChildrenHealth.add(new LoadingItem());
        mHealthChildrenAdapter = new HealthChildrenAdapter(mListChildrenHealth, new HealthChildrenViewHolder.OnClickViewHolderListener() {
            @Override
            public void onClickIndex(int position) {
                mPresenter.onClickIndex(mListChildrenHealth, position);
            }

            @Override
            public void onClickHealth(int position) {
                mPresenter.onClickHealth(((HealthTotalChildren) mListChildrenHealth.get(position)));
            }
        });
        rvTimeline.setAdapter(mHealthChildrenAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvTimeline.setLayoutManager(layoutManager);
        EndlessScrollDownListener scrollDownListener = new EndlessScrollDownListener(layoutManager) {
            @Override
            public void onLoadMore() {
                DebugLog.i("on load more");
                int size = mListChildrenHealth.size();
                if (size > 1 && mListChildrenHealth.get(size - 2) instanceof HealthTotalChildren) {
                    mPresenter.getTimelineChildren(((HealthTotalChildren) mListChildrenHealth.get(size - 1)).getCreatedAt());
                }
            }
        };
        rvTimeline.addOnScrollListener(scrollDownListener);
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
        relRetry.setVisibility(View.GONE);
        UiUtils.showProgressBar(pbLoading);
        mPresenter.onClickRetry();
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
    public void onLoadChildren(Child child) {
        isDisplayInfo = true;

        UiUtils.hideProgressBar(pbLoading);
        layoutProfile.setVisibility(View.VISIBLE);
        viewLine.setVisibility(View.VISIBLE);
        layoutTimeline.setVisibility(View.VISIBLE);

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
    public void onLoadChildrenTimeLine(ArrayList<HealthTotalChildren> data) {
        int sizeNewData = data.size();
        int sizeTotal = mListChildrenHealth.size();
        if (sizeNewData < AppConstant.ITEM_HEALTH_PER_PAGE) {
            if (!mListChildrenHealth.isEmpty() && mListChildrenHealth.get(sizeTotal - 1) instanceof LoadingItem) {
                mListChildrenHealth.remove(sizeTotal - 1);
                mHealthChildrenAdapter.notifyItemRemoved(sizeTotal - 1);
            }
            if (sizeNewData > 0) {
                sizeTotal = mListChildrenHealth.size();
                mListChildrenHealth.addAll(data);
                mHealthChildrenAdapter.notifyItemRangeInserted(sizeTotal, data.size());
            }
        } else {
            mListChildrenHealth.addAll(sizeTotal - 1, data);
            mHealthChildrenAdapter.notifyItemRangeInserted(sizeTotal - 1, data.size());
        }
    }

    @Override
    public void onBackPressed() {
        if (isDisplayInfo) {
            layoutProfile.setVisibility(View.GONE);
            imvIcon.setImageDrawable(null);
            tvName.setText("");
            tvDOB.setText("");
            tvHobby.setText("");
            tvCharacteristic.setText("");

            viewLine.setVisibility(View.GONE);
            layoutTimeline.setVisibility(View.GONE);
            mListChildrenHealth.clear();
            mListChildrenHealth.add(new LoadingItem());
            mHealthChildrenAdapter.notifyDataSetChanged();

            rvListChildren.setVisibility(View.VISIBLE);
            isDisplayInfo = false;
            return;
        }

        super.onBackPressed();
    }
}