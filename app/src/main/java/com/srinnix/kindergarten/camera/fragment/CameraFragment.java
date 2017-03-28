package com.srinnix.kindergarten.camera.fragment;

import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.camera.presenter.CameraPresenter;
import com.srinnix.kindergarten.util.UiUtils;

import java.io.IOException;

import butterknife.BindView;

/**
 * Created by DELL on 2/7/2017.
 */

public class CameraFragment extends BaseFragment implements SurfaceHolder.Callback {
    @BindView(R.id.surface_view_video)
    SurfaceView mSurfaceView;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    private MediaPlayer mPlayer;
    private SurfaceHolder mSurfaceHolder;

    private CameraPresenter mPresenter;
    private String url;

    @Override
    protected void getData() {
        super.getData();
        Bundle arguments = getArguments();
        url = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_camera;
    }

    @Override
    protected void initChildView() {
        url = "rtsp://192.168.0.104:1935/live/myStream";

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);

        pbLoading.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary)
                , PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }


    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new CameraPresenter(this);
        return mPresenter;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mPlayer = new MediaPlayer();
        mPlayer.setDisplay(mSurfaceHolder);
        mPlayer.setOnPreparedListener(mediaPlayer -> {
            mPlayer.start();
            UiUtils.hideProgressBar(pbLoading);
        });
        mPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Toast.makeText(mContext, String.valueOf(percent), Toast.LENGTH_SHORT).show();
                UiUtils.showProgressBar(pbLoading);
            }
        });
        mPlayer.setScreenOnWhilePlaying(true);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.prepareAsync();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mPlayer.release();
    }
}
