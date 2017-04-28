package com.srinnix.kindergarten.bulletinboard.helper;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.srinnix.kindergarten.model.ImageLocal;
import com.srinnix.kindergarten.util.ErrorUtil;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhtu on 4/25/2017.
 */

public class ImagePickerHelper {
    private CompositeDisposable mDisposable;

    public ImagePickerHelper(CompositeDisposable mDisposable) {
        this.mDisposable = mDisposable;
    }

    public static final String[] projection = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA
    };


    public void getLocalImage(Context mContext, OnLoadImageLocalListener listener) {
        mDisposable.add(Single.fromCallable(() -> {

            Cursor cursor = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                    null, null, MediaStore.Images.Media.DATE_ADDED);

            if (cursor == null) {
                return null;
            }

            ArrayList<ImageLocal> temp = new ArrayList<>(cursor.getCount());
            File file;

            if (cursor.moveToLast()) {
                do {
                    long id = cursor.getLong(0);
                    String name = cursor.getString(1);
                    String path = cursor.getString(2);

                    file = new File(path);
                    if (file.exists()) {
                        ImageLocal image = new ImageLocal(id, name, path, false);
                        temp.add(image);
                    }

                } while (cursor.moveToPrevious());
            }
            cursor.close();

            return temp;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(imageLocals -> {
                    if (imageLocals == null) {
                        listener.onLoadFail();
                    } else {
                        listener.onLoadSuccess(imageLocals);
                    }
                }, ErrorUtil::handleException));
    }

    public void getImageCapture(Context mContext, String path, OnGetImageCaptureListener listener) {
        mDisposable.add(Single.fromCallable(() -> {
            ImageLocal image = null;

            Cursor cursor = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Images.Media._ID,
                            MediaStore.Images.Media.DISPLAY_NAME
                    },
                    "_data = '" + path + "'", null, MediaStore.Images.Media.DATE_ADDED);

            if (cursor == null) {
                return null;
            }

            File file;

            if (cursor.moveToNext()) {
                long id = cursor.getLong(0);
                String name = cursor.getString(1);

                file = new File(path);
                if (file.exists()) {
                    image = new ImageLocal(id, name, path, false);
                }
            }
            cursor.close();

            return image;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(imageLocal -> {
                    if (imageLocal == null) {
                        listener.onLoadFail();
                    } else {
                        listener.onLoadSuccess(imageLocal);
                    }
                }, ErrorUtil::handleException));
    }

    public interface OnLoadImageLocalListener {
        void onLoadSuccess(ArrayList<ImageLocal> imageLocals);

        void onLoadFail();
    }

    public interface OnGetImageCaptureListener {
        void onLoadSuccess(ImageLocal imageLocal);

        void onLoadFail();
    }
}