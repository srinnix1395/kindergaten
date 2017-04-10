package com.srinnix.kindergarten.clazz.fragment;

import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.clazz.adapter.ClassAdapter;
import com.srinnix.kindergarten.clazz.delegate.ClassListDelegate;
import com.srinnix.kindergarten.clazz.presenter.ClassListPresenter;
import com.srinnix.kindergarten.model.Class;
import com.srinnix.kindergarten.util.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2/21/2017.
 */

public class ClassListFragment extends BaseFragment implements ClassListDelegate{
    @BindView(R.id.recycler_view_class_list)
    RecyclerView mRvListClass;

    @BindView(R.id.layout_retry)
    RelativeLayout relRetry;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.imageview_retry)
    ImageView imvRetry;

    @BindView(R.id.textview_retry)
    TextView tvRetry;

    private ArrayList<Class> mListClass;
    private ClassAdapter mAdapter;
    private ClassListPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_class_list;
    }

    @Override
    protected void initChildView() {
        mRvListClass.setLayoutManager(new LinearLayoutManager(mContext));
        mRvListClass.setAdapter(mAdapter);

        pbLoading.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(mContext, R.color.colorPrimary),
                PorterDuff.Mode.SRC_ATOP);

        if (!isFirst) {
            mRvListClass.setVisibility(View.VISIBLE);
            UiUtils.hideProgressBar(pbLoading);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mListClass = new ArrayList<>();
        mAdapter = new ClassAdapter(mListClass, position -> mPresenter.onClickClass(mListClass.get(position)));
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new ClassListPresenter(this);
        return mPresenter;
    }

    @OnClick(R.id.layout_retry)
    public void onClickRetry(){
        mPresenter.onClickRetry(imvRetry, tvRetry, pbLoading);
    }

    @Override
    public void onLoadSuccess(ArrayList<Class> arrayList) {
        UiUtils.hideProgressBar(pbLoading);

        relRetry.setVisibility(View.INVISIBLE);

        if (mRvListClass.getVisibility() != View.VISIBLE) {
            mRvListClass.setVisibility(View.VISIBLE);
        }
        if (!mListClass.isEmpty()) {
            mListClass.clear();
        }
        mListClass.addAll(arrayList);
        mAdapter.notifyItemRangeInserted(0, arrayList.size());
    }

    @Override
    public void onLoadError(int resError) {
        if (mRvListClass.getVisibility() == View.VISIBLE) {
            mRvListClass.setVisibility(View.GONE);
        }

        UiUtils.hideProgressBar(pbLoading);
        imvRetry.setVisibility(View.VISIBLE);
        tvRetry.setText(resError);
        tvRetry.setVisibility(View.VISIBLE);
    }
}
