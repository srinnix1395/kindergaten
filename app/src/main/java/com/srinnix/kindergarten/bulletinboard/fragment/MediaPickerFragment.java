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
import com.srinnix.kindergarten.bulletinboard.adapter.MediaPickerAdapter;
import com.srinnix.kindergarten.bulletinboard.delegate.MediaPickerDelegate;
import com.srinnix.kindergarten.bulletinboard.presenter.MediaPickerPresenter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.custom.SpacesItemDecoration;
import com.srinnix.kindergarten.model.MediaLocal;
import com.srinnix.kindergarten.util.UiUtils;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;

/**
 * Created by anhtu on 4/24/2017.
 */

public class MediaPickerFragment extends BaseFragment implements MediaPickerDelegate {

    @BindView(R.id.toolbar_image_picker)
    Toolbar toolbar;

    @BindView(R.id.recyclerview_image)
    RecyclerView rvListMedia;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.layout_retry)
    RelativeLayout layoutRetry;

    @BindView(R.id.textview_retry)
    TextView tvRetry;

    private MenuItem menuItemAdd;

    private MediaPickerAdapter mMediaAdapter;
    private ArrayList<MediaLocal> mListMedia;
    private MediaPickerPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_image_picker;
    }

    @Override
    protected void initData() {
        super.initData();
        mListMedia = new ArrayList<>();
        mMediaAdapter = new MediaPickerAdapter(mListMedia, position -> {
            mPresenter.onClickMedia(mListMedia.get(position), position);
        });
        mPresenter.checkPermissionStorage(getActivity());
    }

    @Override
    protected void initChildView() {
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.tap_to_select_media);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        toolbar.inflateMenu(R.menu.menu_image_picker_fragment);
        menuItemAdd = toolbar.getMenu().findItem(R.id.menu_item_add);
        if (mPresenter.getListImageSelected().size() == 0) {
            menuItemAdd.setVisible(false);
        } else {
            menuItemAdd.setVisible(true);
        }

        MenuItem menuItemCamera = toolbar.getMenu().findItem(R.id.menu_item_camera);
        if (mPresenter.getMediaType() == AppConstant.TYPE_IMAGE) {
            menuItemCamera.setIcon(R.drawable.ic_photo_camera);
        } else {
            menuItemCamera.setIcon(R.drawable.ic_camera);
        }

        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_item_camera: {
                    mPresenter.onClickCamera(MediaPickerFragment.this);
                    break;
                }
                case R.id.menu_item_add: {
                    mPresenter.onClickAdd(MediaPickerFragment.this);
                    break;
                }
            }
            return true;
        });

        pbLoading.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        rvListMedia.setAdapter(mMediaAdapter);

        rvListMedia.setLayoutManager(new GridLayoutManager(mContext, 3));
        SpacesItemDecoration decoration = new SpacesItemDecoration(mContext, mMediaAdapter, 2, 3, false);
        rvListMedia.addItemDecoration(decoration);
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new MediaPickerPresenter(this);
        return mPresenter;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case MediaPickerPresenter.PERMISSIONS_REQUEST_READ_EXTERNAL: {
                    mPresenter.getMedia();
                    break;
                }
                case MediaPickerPresenter.PERMISSIONS_REQUEST_CAMERA: {
                    mPresenter.openCamera(this);
                    break;
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == MediaPickerPresenter.REQUEST_CODE_TAKE_PICTURE || requestCode == MediaPickerPresenter.REQUEST_CODE_TAKE_VIDEO) && resultCode == Activity.RESULT_OK) {
            mPresenter.displayMedia(mListMedia);
        }
    }

    @Override
    public void updateStateMedia(int numberImage, int numberVideo, int position, boolean selected) {
        mMediaAdapter.notifyItemChanged(position, selected);
        if (numberVideo + numberImage == 0) {
            toolbar.setTitle(R.string.tap_to_select_media);

            if (menuItemAdd.isVisible()) {
                menuItemAdd.setVisible(false);
            }
        } else {
            toolbar.setTitle(String.format(Locale.getDefault(), "%d %s và %d %s",
                    numberImage, mContext.getString(R.string.image),
                    numberVideo, mContext.getString(R.string.video)));

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
    public void onLoadSuccess(ArrayList<MediaLocal> mediaLocals) {
        UiUtils.hideProgressBar(pbLoading);

        if (mediaLocals.isEmpty()) {
            return;
        }

        rvListMedia.setVisibility(View.VISIBLE);
        mListMedia.addAll(mediaLocals);
        mMediaAdapter.notifyItemRangeInserted(0, mediaLocals.size());

        rvListMedia.scrollToPosition(0);

        if (mPresenter.getListImageSelected().size() == 0) {
            toolbar.setTitle(R.string.tap_to_select_media);
        } else {
            toolbar.setTitle(String.format(Locale.getDefault(), "%d %s và %d %s",
                    mPresenter.getNumberImage(), mContext.getString(R.string.image),
                    mPresenter.getNumberVideo(), mContext.getString(R.string.video)));
        }
    }

    @Override
    public void insertMediaLocal() {
        mMediaAdapter.notifyItemInserted(0);
        rvListMedia.scrollToPosition(0);
    }
}
