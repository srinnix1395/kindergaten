package com.srinnix.kindergarten.bulletinboard.helper;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.srinnix.kindergarten.model.MediaLocal;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by anhtu on 4/25/2017.
 */

public class MediaPickerHelper {
    private CompositeDisposable mDisposable;

    public MediaPickerHelper(CompositeDisposable mDisposable) {
        this.mDisposable = mDisposable;
    }

    private static final String[] projectionImage = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.MIME_TYPE
    };

    private static final String[] projectionVideo = new String[]{
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Thumbnails.DATA,
            MediaStore.Video.Media.SIZE
    };

    public Single<ArrayList<MediaLocal>> getLocalImage(Context mContext) {
        return Single.fromCallable(() -> {
            Cursor cursor = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projectionImage,
                    null, null, MediaStore.Images.Media.DATE_ADDED);

            if (cursor == null) {
                return null;
            }

            ArrayList<MediaLocal> temp = new ArrayList<>(cursor.getCount());
            File file;

            if (cursor.moveToLast()) {
                do {
                    long id = cursor.getLong(0);
                    String name = cursor.getString(1);
                    String path = cursor.getString(2);
                    boolean isGIF = cursor.getString(3).equalsIgnoreCase("image/gif");

                    file = new File(path);
                    if (file.exists()) {
                        MediaLocal image = new MediaLocal(id, name, path, isGIF);
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

    public Single<MediaLocal> getImageCapture(Context mContext, String path) {
        return Single.fromCallable(() -> {
            MediaLocal image = null;

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
                    image = new MediaLocal(id, name, path, isGIF);
                }
            }
            cursor.close();

            return image;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<MediaLocal>> getLocalVideo(Context mContext) {
        return Single.fromCallable(() -> {
            Cursor cursor = mContext.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projectionVideo,
                    null, null, MediaStore.Video.Media.DATE_ADDED);

            if (cursor == null) {
                return null;
            }

            ArrayList<MediaLocal> temp = new ArrayList<>(cursor.getCount());
            File file;

            if (cursor.moveToLast()) {
                do {
                    long id = cursor.getLong(0);
                    String name = cursor.getString(1);
                    String path = cursor.getString(2);
                    int duration = cursor.getInt(3);
                    String urlThumbnail = cursor.getString(4);
                    int size = cursor.getInt(5);

                    file = new File(path);
                    if (file.exists()) {
                        MediaLocal image = new MediaLocal(id, name, path, true, duration, urlThumbnail, size);
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

    public Single<MediaLocal> getVideoCapture(Context mContext, String path) {
        return Single.fromCallable(() -> {
            MediaLocal video = null;

            Cursor cursor = mContext.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Video.Media._ID,
                            MediaStore.Video.Media.DISPLAY_NAME,
                            MediaStore.Video.Media.DURATION,
                            MediaStore.Video.Thumbnails.DATA,
                            MediaStore.Video.Media.SIZE
                    },
                    "_data = '" + path + "'", null, MediaStore.Video.Media.DATE_ADDED);

            if (cursor == null) {
                return null;
            }

            File file;

            if (cursor.moveToNext()) {
                long id = cursor.getLong(0);
                String name = cursor.getString(1);
                int duration = cursor.getInt(2);
                String urlThumbnail = cursor.getString(3);
                int size = cursor.getInt(4);

                file = new File(path);
                if (file.exists()) {
                    video = new MediaLocal(id, name, path, true, duration, urlThumbnail, size);
                }
            }
            cursor.close();

            return video;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
