package com.srinnix.kindergarten.bulletinboard.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.adapter.ImagePickerAdapter;
import com.srinnix.kindergarten.bulletinboard.delegate.ImagePickerDelegate;
import com.srinnix.kindergarten.bulletinboard.presenter.ImagePickerPresenter;
import com.srinnix.kindergarten.custom.SpacesItemDecoration;
import com.srinnix.kindergarten.model.ImageLocal;
import com.srinnix.kindergarten.util.UiUtils;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;

/**
 * Created by anhtu on 4/24/2017.
 */

public class ImagePickerFragment extends BaseFragment implements ImagePickerDelegate {

    @BindView(R.id.toolbar_image_picker)
    Toolbar toolbar;

    @BindView(R.id.recycler_view_image)
    RecyclerView rvListImage;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.layout_retry)
    RelativeLayout layoutRetry;

    @BindView(R.id.textview_retry)
    TextView tvRetry;

    private MenuItem menuItemAdd;

    private ImagePickerAdapter mImageAdapter;
    private ArrayList<ImageLocal> mListImage;
    private ImagePickerPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image_picker;
    }

    @Override
    protected void initData() {
        super.initData();
        mListImage = new ArrayList<>();
        mImageAdapter = new ImagePickerAdapter(mListImage, position -> mPresenter.onClickImage(mListImage, position));
        mPresenter.checkPermission(getActivity());
    }

    @Override
    protected void initChildView() {
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.tap_to_select_image);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        toolbar.inflateMenu(R.menu.menu_image_picker_fragment);
        menuItemAdd = toolbar.getMenu().findItem(R.id.menu_item_add);
        menuItemAdd.setVisible(false);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_item_camera: {
                    mPresenter.onClickCamera(ImagePickerFragment.this);
                    break;
                }
                case R.id.menu_item_add: {
                    mPresenter.onClickAdd(ImagePickerFragment.this, mListImage);
                    break;
                }
            }
            return true;
        });

        pbLoading.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        rvListImage.setAdapter(mImageAdapter);

        rvListImage.setLayoutManager(new GridLayoutManager(mContext, 3));
        SpacesItemDecoration decoration = new SpacesItemDecoration(mContext, mImageAdapter, 2, 3, false);
        rvListImage.addItemDecoration(decoration);
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new ImagePickerPresenter(this);
        return mPresenter;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ImagePickerPresenter.PERMISSIONS_REQUEST_READ_EXTERNAL) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                mPresenter.getImage();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePickerPresenter.REQUEST_CODE_TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            mListImage.add(0, new ImageLocal(1, "abc", mPresenter.mCurrentPhotoPath, false));
            mImageAdapter.notifyItemInserted(0);
            mPresenter.capturedImage();
        }
    }

    @Override
    public void updateStateImage(int numberImage, int position, boolean selected) {
        mImageAdapter.notifyItemChanged(position, selected);
        if (numberImage == 0) {
            toolbar.setTitle(R.string.tap_to_select_image);
            if (menuItemAdd.isVisible()) {
                menuItemAdd.setVisible(false);
            }
        } else {
            toolbar.setTitle(String.format(Locale.getDefault(), "%d %s", numberImage, mContext.getString(R.string.image)));
            if (!menuItemAdd.isVisible()) {
                menuItemAdd.setVisible(true);
            }
        }
    }

    @Override
    public void onLoadFail(int resError) {
        UiUtils.hideProgressBar(pbLoading);

        tvRetry.setText(resError);
        layoutRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadSuccess(ArrayList<ImageLocal> imageLocals) {
        UiUtils.hideProgressBar(pbLoading);

        if (imageLocals.isEmpty()) {
            return;
        }

        rvListImage.setVisibility(View.VISIBLE);
        mListImage.addAll(imageLocals);
        mImageAdapter.notifyItemRangeInserted(0, imageLocals.size());

        rvListImage.scrollToPosition(0);
    }
}
