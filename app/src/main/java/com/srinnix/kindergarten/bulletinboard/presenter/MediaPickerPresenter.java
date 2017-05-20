package com.srinnix.kindergarten.bulletinboard.presenter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LongSparseArray;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.delegate.MediaPickerDelegate;
import com.srinnix.kindergarten.bulletinboard.fragment.MediaPickerFragment;
import com.srinnix.kindergarten.bulletinboard.helper.MediaPickerHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.messageeventbus.MessageImageLocal;
import com.srinnix.kindergarten.model.MediaLocal;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.ErrorUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Single;

/**
 * Created by anhtu on 4/25/2017.
 */

public class MediaPickerPresenter extends BasePresenter {

    public static final int PERMISSIONS_REQUEST_READ_EXTERNAL = 12;
    public static final int PERMISSIONS_REQUEST_CAMERA = 14;
    public static final int REQUEST_CODE_TAKE_PICTURE = 13;
    public static final int REQUEST_CODE_TAKE_VIDEO = 15;

    private int numberImage = 0;
    private int numberVideo = 0;
    private LongSparseArray<MediaLocal> mListMediaSelected = new LongSparseArray<>();

    private MediaPickerDelegate mMediaPickerDelegate;
    private MediaPickerHelper mHelper;
    private File fileCapture;
    private int mediaType;

    public MediaPickerPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mMediaPickerDelegate = (MediaPickerDelegate) mDelegate;

        mHelper = new MediaPickerHelper(mDisposable);
    }

    @Override
    public void getData(Bundle bundle) {
        super.getData(bundle);
        mediaType = bundle.getInt(AppConstant.KEY_MEDIA_TYPE, AppConstant.TYPE_IMAGE);

        ArrayList<MediaLocal> arrayList = bundle.getParcelableArrayList(AppConstant.KEY_MEDIA);
        if (arrayList != null) {
            for (MediaLocal mediaLocal : arrayList) {
                mListMediaSelected.put(mediaLocal.getId(), mediaLocal);
                if (mediaLocal.isVideo()) {
                    numberVideo++;
                } else {
                    numberImage++;
                }
            }
        }
    }

    public void checkPermissionStorage(FragmentActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean isHasPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

            if (!isHasPermission) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_READ_EXTERNAL);
            } else {
                getMedia();
            }
        } else {
            getMedia();

        }
    }

    public void getMedia() {
        if (mediaType == AppConstant.TYPE_IMAGE) {
            getImage();
        } else {
            getVideo();
        }
    }

    public void getVideo() {
        mDisposable.add(mHelper.getLocalVideo(mContext)
                .subscribe(imageLocals -> {
                    if (imageLocals == null) {
                        mMediaPickerDelegate.onLoadFail(R.string.error_common);
                    } else {
                        int i = 0;
                        for (MediaLocal mediaLocal : imageLocals) {
                            if (i == mListMediaSelected.size()) {
                                break;
                            }
                            if (mListMediaSelected.indexOfKey(mediaLocal.getId()) >= 0) {
                                i++;
                                mediaLocal.setSelected(true);
                            }
                        }
                        mMediaPickerDelegate.onLoadSuccess(imageLocals);
                    }
                }, throwable -> {
                    ErrorUtil.handleException(throwable);
                    mMediaPickerDelegate.onLoadFail(R.string.error_common);
                }));
    }

    public void getImage() {
        mDisposable.add(mHelper.getLocalImage(mContext)
                .subscribe(imageLocals -> {
                    if (imageLocals == null) {
                        mMediaPickerDelegate.onLoadFail(R.string.error_common);
                    } else {
                        int i = 0;
                        for (MediaLocal mediaLocal : imageLocals) {
                            if (i == mListMediaSelected.size()) {
                                break;
                            }
                            if (mListMediaSelected.indexOfKey(mediaLocal.getId()) >= 0) {
                                i++;
                                mediaLocal.setSelected(true);
                            }
                        }
                        mMediaPickerDelegate.onLoadSuccess(imageLocals);
                    }
                }, throwable -> {
                    ErrorUtil.handleException(throwable);
                    mMediaPickerDelegate.onLoadFail(R.string.error_common);
                }));
    }

    public void onClickCamera(MediaPickerFragment mediaPickerFragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean isHasPermission = ContextCompat.checkSelfPermission(mediaPickerFragment.getActivity()
                    , Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

            if (!isHasPermission) {
                ActivityCompat.requestPermissions(mediaPickerFragment.getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSIONS_REQUEST_CAMERA);
            } else {
                openCamera(mediaPickerFragment);
            }
        } else {
            openCamera(mediaPickerFragment);
        }

    }

    public void openCamera(MediaPickerFragment mediaPickerFragment) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(mContext.getPackageManager()) != null) {

            if (mediaType == AppConstant.TYPE_IMAGE) {
                File publicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

                String file = "IMAGE_CAPTURE_" + System.currentTimeMillis() + ".jpg";
                fileCapture = new File(publicDirectory, file);

                Uri imageCapture = Uri.fromFile(fileCapture);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageCapture);

                mediaPickerFragment.startActivityForResult(cameraIntent, REQUEST_CODE_TAKE_PICTURE);
            } else {
                File publicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);

                String file = "VIDEO_CAPTURE_" + System.currentTimeMillis() + ".mp4";
                fileCapture = new File(publicDirectory, file);

                Uri videoCapture = Uri.fromFile(fileCapture);

                Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoCapture);

                mediaPickerFragment.startActivityForResult(cameraIntent, REQUEST_CODE_TAKE_VIDEO);
            }
        } else {
            AlertUtils.showToast(mContext, R.string.error_no_camera);
        }
    }

    public void displayMedia(ArrayList<MediaLocal> mListMedia) {
        Uri mediaUri = Uri.fromFile(fileCapture);
        if (mediaUri != null) {
            MediaScannerConnection.scanFile(mContext, new String[]{mediaUri.getPath()}, null, (path, uri) -> {
                Single<MediaLocal> single;
                if (mediaType == AppConstant.TYPE_IMAGE) {
                    single = mHelper.getImageCapture(mContext, fileCapture.getPath());
                } else {
                    single = mHelper.getVideoCapture(mContext, fileCapture.getPath());
                }
                single.subscribe(mediaLocal -> {
                    mListMedia.add(0, mediaLocal);
                    mMediaPickerDelegate.insertMediaLocal();
                }, ErrorUtil::handleException);
            });
        }
    }

    public void onClickMedia(MediaLocal mediaLocal, int position) {
        if (mediaLocal.isSelected()) {
            if (mediaLocal.isVideo()) {
                numberVideo--;
            } else {
                numberImage--;
            }
            mediaLocal.setSelected(false);
            mListMediaSelected.remove(mediaLocal.getId());
        } else {
            if (numberImage + numberVideo == 10) {
                AlertUtils.showToast(mContext, "Số ảnh tối đa là 10 ảnh");
                return;
            }

            if (mediaLocal.isVideo()) {
                numberVideo++;
            } else {
                numberImage++;
            }

            mediaLocal.setSelected(true);
            mListMediaSelected.put(mediaLocal.getId(), mediaLocal);
        }

        mMediaPickerDelegate.updateStateMedia(numberImage, numberVideo, position, mediaLocal.isSelected());
    }

    public void onClickAdd(MediaPickerFragment mediaPickerFragment) {
        if (mListMediaSelected.size() > 0) {
            ArrayList<MediaLocal> arrayList = new ArrayList<>();
            for (int i = mListMediaSelected.size() - 1; i >= 0; i--) {
                arrayList.add(mListMediaSelected.valueAt(i));
            }
            EventBus.getDefault().post(new MessageImageLocal(arrayList));
            mediaPickerFragment.onBackPressed();
        }
    }

    public LongSparseArray<MediaLocal> getListImageSelected() {
        return mListMediaSelected;
    }

    public int getMediaType() {
        return mediaType;
    }

    public int getNumberImage() {
        return numberImage;
    }

    public int getNumberVideo() {
        return numberVideo;
    }
}
