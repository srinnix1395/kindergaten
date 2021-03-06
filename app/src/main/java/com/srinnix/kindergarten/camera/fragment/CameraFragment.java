package com.srinnix.kindergarten.camera.fragment;

import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.camera.presenter.CameraPresenter;
import com.srinnix.kindergarten.messageeventbus.MessageLoginSuccessfully;
import com.srinnix.kindergarten.messageeventbus.MessageLogout;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.UiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DELL on 2/7/2017.
 */

public class CameraFragment extends BaseFragment implements SurfaceHolder.Callback {
    @BindView(R.id.surface_view_video)
    SurfaceView mSurfaceView;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.imagview_unsigned_in)
    ImageView imvUnsignedIn;

    @BindView(R.id.layout_unsigned_in)
    RelativeLayout relUnsignedIn;

    private MediaPlayer mPlayer;
    private SurfaceHolder mSurfaceHolder;

    private CameraPresenter mPresenter;
    private String url;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_camera;
    }

    @Override
    protected void initChildView() {
        pbLoading.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary)
                , PorterDuff.Mode.SRC_ATOP);

        if (!SharedPreUtils.getInstance(mContext).isUserSignedIn()) {
            UiUtils.hideProgressBar(pbLoading);
            Glide.with(mContext)
                    .load(R.drawable.kid_drawing_2)
                    .into(imvUnsignedIn);

            mSurfaceView.setVisibility(View.GONE);
            relUnsignedIn.setVisibility(View.VISIBLE);
            return;
        }

        initCamera();
    }

    private void initCamera() {
        url = "rtsp://192.168.137.1:1935/live/myStream";

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && SharedPreUtils.getInstance(mContext).isUserSignedIn()) {
            getParentFragment().getChildFragmentManager().beginTransaction().remove(this).commit();
            onDestroy();
        }
    }

    @Subscribe
    public void onMessageLoginSuccessfully(MessageLoginSuccessfully message) {
        relUnsignedIn.setVisibility(View.GONE);
        mSurfaceView.setVisibility(View.VISIBLE);
        UiUtils.showProgressBar(pbLoading);
        initCamera();
    }

    @Subscribe
    public void onMessageLogout(MessageLogout message) {
        UiUtils.hideProgressBar(pbLoading);
        mSurfaceView.setVisibility(View.GONE);

        releaseMediaPlayer();
    }

    @OnClick(R.id.button_login)
    public void onClickLogin() {
        mPresenter.addFragmentLogin();
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
            if (!mPlayer.isPlaying()) {
                mPlayer.start();
            }
            UiUtils.hideProgressBar(pbLoading);
        });
        mPlayer.setOnBufferingUpdateListener((mp, percent) -> {
            Toast.makeText(mContext, String.valueOf(percent), Toast.LENGTH_SHORT).show();
            UiUtils.showProgressBar(pbLoading);
        });
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        mPlayer.setScreenOnWhilePlaying(true);

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
        if (mPlayer != null) {
            mPlayer.release();
        }
    }
}
