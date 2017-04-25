package com.srinnix.kindergarten.bulletinboard.presenter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.delegate.ImagePickerDelegate;
import com.srinnix.kindergarten.bulletinboard.fragment.ImagePickerFragment;
import com.srinnix.kindergarten.bulletinboard.helper.ImagePickerHelper;
import com.srinnix.kindergarten.messageeventbus.MessageImageLocal;
import com.srinnix.kindergarten.model.ImageLocal;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by anhtu on 4/25/2017.
 */

public class ImagePickerPresenter extends BasePresenter {

    public static final int PERMISSIONS_REQUEST_READ_EXTERNAL = 12;
    public static final int REQUEST_CODE_TAKE_PICTURE = 13;

    private int numberImage = 0;
    private ImagePickerDelegate mImagePickerDelegate;
    private ImagePickerHelper mHelper;
    private CompositeDisposable mDisposable;
    public String mCurrentPhotoPath;

    public ImagePickerPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mImagePickerDelegate = (ImagePickerDelegate) mDelegate;

        mDisposable = new CompositeDisposable();
        mHelper = new ImagePickerHelper(mDisposable);
    }

    public void checkPermission(FragmentActivity activity) {
        boolean isHasPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if (!isHasPermission) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_READ_EXTERNAL);
        } else {
            getImage();
        }
    }

    public void getImage() {
        mHelper.getLocalImage(mContext, new ImagePickerHelper.OnLoadImageLocalListener() {
            @Override
            public void onLoadSuccess(ArrayList<ImageLocal> imageLocals) {
                mImagePickerDelegate.onLoadSuccess(imageLocals);
            }

            @Override
            public void onLoadFail() {
                mImagePickerDelegate.onLoadFail(R.string.error_common);
            }
        });
    }

    public void onClickImage(ArrayList<ImageLocal> mListImage, int position) {
        ImageLocal imageLocal = mListImage.get(position);

        if (imageLocal.isSelected()) {
            numberImage--;
            imageLocal.setSelected(false);
        } else {
            numberImage++;
            imageLocal.setSelected(true);
        }

        mImagePickerDelegate.updateStateImage(numberImage, position, imageLocal.isSelected());
    }

    public void onClickCamera(ImagePickerFragment imagePickerFragment) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(mContext, "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                imagePickerFragment.startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PICTURE);
            }
        }
    }

    public void capturedImage() {
        try {
            File file = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        String root = Environment.getExternalStorageDirectory().toString();
        File folder = new File(root + "/Saved_Images");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Image_capture_" + timeStamp;
        File image_file = null;

        try {
            image_file = File.createTempFile(imageFileName, ".jpg", folder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCurrentPhotoPath = image_file.getAbsolutePath();
        return image_file;
    }

    public void onClickAdd(ImagePickerFragment imagePickerFragment, ArrayList<ImageLocal> mListImage) {
        if (!mListImage.isEmpty()) {
            ArrayList<String> arrayList = new ArrayList<>();
            for (ImageLocal imageLocal : mListImage) {
                if (imageLocal.isSelected()) {
                    arrayList.add(imageLocal.getPath());
                }
            }

            EventBus.getDefault().post(new MessageImageLocal(arrayList));
            imagePickerFragment.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }


}
