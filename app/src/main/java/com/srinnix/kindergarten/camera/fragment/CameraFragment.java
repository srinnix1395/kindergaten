package com.srinnix.kindergarten.camera.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.camera.presenter.CameraPresenter;

import butterknife.BindView;

/**
 * Created by DELL on 2/7/2017.
 */

public class CameraFragment extends BaseFragment {
    @BindView(R.id.videoview_camera)
    VideoView vvCamera;

    private CameraPresenter mPresenter;
    private String url;

    @Override
    protected void getData() {
        super.getData();
        Bundle arguments = getArguments();
//        url = arguments.getString(AppConstant.URL_CAMERA);
//        url = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
        url = "https://cdn.livestream.com/grid/LSPlayer.swf?channel=tnhtv&fb_version=2.0&autoPlay=true";

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_camera;
    }

    @Override
    protected void initChildView() {
//        MediaController mediacontroller = new MediaController(getContext());
//        mediacontroller.setAnchorView(vvCamera);

        vvCamera.setVideoURI(Uri.parse("https://cdn.livestream.com/grid/LSPlayer.swf?channel=tnhtv&fb_version=2.0&autoPlay=true"));
        vvCamera.start();

    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new CameraPresenter(this);
        return mPresenter;
    }
}
