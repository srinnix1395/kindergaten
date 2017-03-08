package com.srinnix.kindergarten.camera.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.camera.presenter.CameraPresenter;

import java.io.IOException;

import butterknife.BindView;

/**
 * Created by DELL on 2/7/2017.
 */

public class CameraFragment extends BaseFragment {
    @BindView(R.id.surface_view_video)
    SurfaceView mSurfaceView;

    private MediaPlayer mPlayer;
    private SurfaceHolder mSurfaceHolder;

    private CameraPresenter mPresenter;
    private String url;

    @Override
    protected void getData() {
        super.getData();
        Bundle arguments = getArguments();
//        url = arguments.getString(AppConstant.URL_CAMERA);
        url = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
//        url = "https://cdn.livestream.com/grid/LSPlayer.swf?channel=tnhtv&fb_version=2.0&autoPlay=true";

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_camera;
    }

    @Override
    protected void initChildView() {
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                initMediaPlayer();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });


    }

    private void initMediaPlayer() {
        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(mediaPlayer -> {
            mPlayer.start();
        });
        mPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                Toast.makeText(mContext, "đang update", Toast.LENGTH_SHORT).show();
            }
        });
        mPlayer.setDisplay(mSurfaceHolder);
        mPlayer.setScreenOnWhilePlaying(true);

        playVideo();
    }

    private void playVideo() {
        try {
            mPlayer.setDataSource("https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4");
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new CameraPresenter(this);
        return mPresenter;
    }
}
