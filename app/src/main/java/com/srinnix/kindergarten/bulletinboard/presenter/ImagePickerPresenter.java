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
import com.srinnix.kindergarten.bulletinboard.delegate.ImagePickerDelegate;
import com.srinnix.kindergarten.bulletinboard.fragment.ImagePickerFragment;
import com.srinnix.kindergarten.bulletinboard.helper.ImagePickerHelper;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.messageeventbus.MessageImageLocal;
import com.srinnix.kindergarten.model.ImageLocal;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.ErrorUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by anhtu on 4/25/2017.
 */

public class ImagePickerPresenter extends BasePresenter {

    public static final int PERMISSIONS_REQUEST_READ_EXTERNAL = 12;
    public static final int PERMISSIONS_REQUEST_CAMERA = 14;
    public static final int REQUEST_CODE_TAKE_PICTURE = 13;

    private int numberImage = 0;
    private ImagePickerDelegate mImagePickerDelegate;
    private ImagePickerHelper mHelper;
    private File fileCapture;

    public ImagePickerPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mImagePickerDelegate = (ImagePickerDelegate) mDelegate;

        mHelper = new ImagePickerHelper(mDisposable);
    }

    @Override
    public void getData(Bundle bundle) {
        super.getData(bundle);
        ArrayList<ImageLocal> arrayList = bundle.getParcelableArrayList(AppConstant.KEY_IMAGE);
        numberImage = arrayList != null ? arrayList.size() : 0;
    }

    public void checkPermissionStorage(FragmentActivity activity, LongSparseArray<ImageLocal> mListImageSelected) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean isHasPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

            if (!isHasPermission) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_READ_EXTERNAL);
            } else {
                getImage(mListImageSelected);
            }
        } else {
            getImage(mListImageSelected);

        }
    }

    public void getImage(LongSparseArray<ImageLocal> mListImageSelected) {
        mDisposable.add(mHelper.getLocalImage(mContext)
                .subscribe(imageLocals -> {
                    if (imageLocals == null) {
                        mImagePickerDelegate.onLoadFail(R.string.error_common);
                    } else {
                        int i = 0;
                        for (ImageLocal imageLocal : imageLocals) {
                            if (i == mListImageSelected.size()) {
                                break;
                            }
                            if (mListImageSelected.indexOfKey(imageLocal.getId()) >= 0) {
                                i++;
                                imageLocal.setSelected(true);
                            }
                        }
                        mImagePickerDelegate.onLoadSuccess(imageLocals);
                    }
                }, throwable -> {
                    ErrorUtil.handleException(throwable);
                    mImagePickerDelegate.onLoadFail(R.string.error_common);
                }));
    }

    public void onClickCamera(ImagePickerFragment imagePickerFragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean isHasPermission = ContextCompat.checkSelfPermission(imagePickerFragment.getActivity()
                    , Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

            if (!isHasPermission) {
                ActivityCompat.requestPermissions(imagePickerFragment.getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSIONS_REQUEST_CAMERA);
            } else {
                openCamera(imagePickerFragment);
            }
        } else {
            openCamera(imagePickerFragment);
        }

    }

    public void openCamera(ImagePickerFragment imagePickerFragment) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
            File publicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

            String file = "IMAGE_CAPTURE_" + System.currentTimeMillis() + ".jpg";
            fileCapture = new File(publicDirectory, file);

            Uri imageCapture = Uri.fromFile(fileCapture);

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageCapture);

            imagePickerFragment.startActivityForResult(cameraIntent, REQUEST_CODE_TAKE_PICTURE);
        } else {
            AlertUtils.showToast(mContext, R.string.error_no_camera);
        }
    }

    public void displayImage(ArrayList<ImageLocal> mListImage) {
        Uri imageUri = Uri.fromFile(fileCapture);
        if (imageUri != null) {
            MediaScannerConnection.scanFile(mContext, new String[]{imageUri.getPath()}, null, (path, uri) -> {
                mHelper.getImageCapture(mContext, fileCapture.getPath())
                        .subscribe(imageLocal -> {
                            mListImage.add(0, imageLocal);
                            mImagePickerDelegate.insertImageLocal(0);
                        }, ErrorUtil::handleException);
            });
        }
    }

    public void onClickImage(ImageLocal imageLocal, int position, LongSparseArray<ImageLocal> mListImageSelected) {
        if (imageLocal.isSelected()) {
            numberImage--;
            imageLocal.setSelected(false);
            mListImageSelected.remove(imageLocal.getId());
        } else {
            if (numberImage == 10) {
                AlertUtils.showToast(mContext, "Số ảnh tối đa là 10 ảnh");
                return;
            }

            numberImage++;
            imageLocal.setSelected(true);
            mListImageSelected.put(imageLocal.getId(), imageLocal);
        }

        mImagePickerDelegate.updateStateImage(numberImage, position, imageLocal.isSelected());
    }

    public void onClickAdd(ImagePickerFragment imagePickerFragment, LongSparseArray<ImageLocal> mListImage) {
        if (mListImage.size() > 0) {
            ArrayList<ImageLocal> arrayList = new ArrayList<>();
            for (int i = mListImage.size() - 1; i >= 0; i--) {
                arrayList.add(mListImage.valueAt(i));
            }
            EventBus.getDefault().post(new MessageImageLocal(arrayList));
            imagePickerFragment.onBackPressed();
        }
    }
}
