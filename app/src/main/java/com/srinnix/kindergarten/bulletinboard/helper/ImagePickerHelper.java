package com.srinnix.kindergarten.bulletinboard.helper;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.srinnix.kindergarten.model.ImageLocal;

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

    private static final String[] projection = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.MIME_TYPE
    };

    public Single<ArrayList<ImageLocal>> getLocalImage(Context mContext) {
        return Single.fromCallable(() -> {
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
                    boolean isGIF = cursor.getString(3).equalsIgnoreCase("image/gif");

                    file = new File(path);
                    if (file.exists()) {
                        ImageLocal image = new ImageLocal(id, name, path, isGIF, false);
                        temp.add(image);
                    }

                } while (cursor.moveToPrevious());
            }
            cursor.close();

            return temp;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ImageLocal> getImageCapture(Context mContext, String path) {
        return Single.fromCallable(() -> {
            ImageLocal image = null;

            Cursor cursor = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Images.Media._ID,
                            MediaStore.Images.Media.DISPLAY_NAME,
                            MediaStore.Images.Media.MIME_TYPE
                    },
                    "_data = '" + path + "'", null, MediaStore.Images.Media.DATE_ADDED);

            if (cursor == null) {
                return null;
            }

            File file;

            if (cursor.moveToNext()) {
                long id = cursor.getLong(0);
                String name = cursor.getString(1);
                boolean isGIF = cursor.getString(2).equalsIgnoreCase("image/gif");

                file = new File(path);
                if (file.exists()) {
                    image = new ImageLocal(id, name, path, isGIF, false);
                }
            }
            cursor.close();

            return image;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
