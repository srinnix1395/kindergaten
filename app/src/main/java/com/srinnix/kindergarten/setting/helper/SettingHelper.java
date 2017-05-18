package com.srinnix.kindergarten.setting.helper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.CursorLoader;
import android.support.v4.util.Pair;

import com.srinnix.kindergarten.base.helper.BaseHelper;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.User;
import com.srinnix.kindergarten.request.RetrofitClient;
import com.srinnix.kindergarten.request.model.ApiResponse;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by anhtu on 4/20/2017.
 */

public class SettingHelper extends BaseHelper {

    public SettingHelper(CompositeDisposable mDisposable) {
        super(mDisposable);
    }

    public Single<Pair<ApiResponse<User>, ArrayList<Child>>> getAccountInfo(String token, String idUser, int accountType) {

        return Single.zip(getUserInfo(token, idUser, accountType), getChildren(), Pair::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Single<ApiResponse<User>> getUserInfo(String token, String idUser, int accountType) {
        return mApiService.getAccountInfo(token, idUser, accountType);
    }

    private Single<ArrayList<Child>> getChildren() {
        return Single.fromCallable(() -> {
            RealmResults<Child> results = Realm.getDefaultInstance().where(Child.class).findAll();
            ArrayList<Child> children = new ArrayList<>();
            for (Child result : results) {
                children.add(new Child(result.getId(), result.getName(), result.getImage(), result.getIdClass()));
            }
            return children;
        });
    }

    public Single<ApiResponse<Boolean>> changePassword(String token, String idUser, String oldPassword, String newPassword) {
        return mApiService.changePassword(token, idUser, oldPassword, newPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ApiResponse<User>> updateInfo(Context mContext, String token, User user, String dob, String gender, String phoneNumber, Uri uriNewImage) {
        RequestBody idUser = RequestBody.create(MediaType.parse("text/plain"), user.getId());
        RequestBody accountType = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(user.getAccountType()));
        RequestBody genderBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(user.getGender().equals(gender) ? "null" : gender));
        RequestBody dobBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(dob.equals(user.getDob()) ? "null" : dob));
        RequestBody phoneBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(phoneNumber.equals(user.getPhoneNumber()) ? "null" : phoneNumber));

        MultipartBody.Part partImage = null;
        if (uriNewImage != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                partImage = RetrofitClient.prepareFilePart(getRealPathFromURI_API11to18(mContext, uriNewImage));
            } else {
                partImage = RetrofitClient.prepareFilePart(getRealPathFromURI_API19(mContext, uriNewImage));
            }
        }
        return mApiService.updateInfo(token, idUser, accountType, genderBody, dobBody, phoneBody, partImage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String getRealPathFromURI_API19(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }


    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if(cursor != null){
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }


}
