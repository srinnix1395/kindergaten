package com.srinnix.kindergarten.children.fragment;

import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.children.presenter.InfoChildrenPresenter;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by anhtu on 2/21/2017.
 */

public class InfoChildrenFragment extends BaseFragment {
    @BindView(R.id.imageview_icon)
    CircleImageView imvIcon;

    @BindView(R.id.textview_name)
    TextView tvName;

    @BindView(R.id.textview_DOB)
    TextView tvDOB;

    @BindView(R.id.textview_hobby)
    TextView tvHobby;

    @BindView(R.id.textview_characteristic)
    TextView tvCharacteristic;

    private InfoChildrenPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_info_children;
    }

    @Override
    protected void initChildView() {

    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new InfoChildrenPresenter(this);
        return mPresenter;
    }
}