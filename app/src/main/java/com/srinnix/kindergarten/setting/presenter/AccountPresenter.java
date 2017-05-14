package com.srinnix.kindergarten.setting.presenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.children.fragment.InfoChildrenFragment;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.User;
import com.srinnix.kindergarten.request.model.ApiResponse;
import com.srinnix.kindergarten.setting.delegate.AccountDelegate;
import com.srinnix.kindergarten.setting.fragment.ChangePasswordFragment;
import com.srinnix.kindergarten.setting.helper.SettingHelper;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.ErrorUtil;
import com.srinnix.kindergarten.util.ServiceUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.UiUtils;
import com.srinnix.kindergarten.util.ViewManager;

import java.util.ArrayList;

/**
 * Created by anhtu on 5/11/2017.
 */

public class AccountPresenter extends BasePresenter {
    public static final int PICK_IMAGE = 1080;
    public static final int STATE_VIEW = 0;
    public static final int STATE_EDIT = 1;

    private SettingHelper mHelper;
    private AccountDelegate mAccountDelegate;

    private User user;
    private ArrayList<Child> children;

    private Uri uriNewImage;
    private int state = STATE_VIEW;

    public AccountPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mAccountDelegate = (AccountDelegate) mDelegate;

        mHelper = new SettingHelper(mDisposable);
    }

    @Override
    public void onStart() {
        super.onStart();
        getInfo();
    }

    public void getInfo() {
        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            mAccountDelegate.onFailGetData(R.string.cant_connect);
            return;
        }

        SharedPreUtils preUtils = SharedPreUtils.getInstance(mContext);

        String token = preUtils.getToken();
        String userID = preUtils.getUserID();
        int accountType = preUtils.getAccountType();

        mHelper.getAccountInfo(token, userID, accountType)
                .subscribe(pair -> {
                    ApiResponse<User> response = pair.first;

                    if (response == null) {
                        ErrorUtil.handleException(new NullPointerException());
                        mAccountDelegate.onFailGetData(R.string.error_common);
                        return;
                    }

                    if (response.result == ApiResponse.RESULT_NG) {
                        ErrorUtil.handleErrorApi(mContext, response.error);
                        mAccountDelegate.onFailGetData(R.string.error_common);
                        return;
                    }

                    user = response.getData();
                    children = pair.second;
                    mAccountDelegate.onSuccessGetData(user, children);
                }, throwable -> {
                    ErrorUtil.handleException(throwable);
                    mAccountDelegate.onFailGetData(R.string.error_common);
                });
    }

    public void onClickChangePassword() {
        ViewManager.getInstance().addFragment(new ChangePasswordFragment(), null,
                R.anim.translate_right_to_left, R.anim.translate_left_to_right);
    }

    public void onClickIcon(Fragment accountFragment) {
        if (state == STATE_EDIT) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            accountFragment.startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE);
        }
    }

    public void onClickSave(String dob, String gender, String phoneNumber) {
        boolean infoHasNotChanged = (phoneNumber.equals(user.getPhoneNumber()) || (phoneNumber.isEmpty() && user.getPhoneNumber() == null)) &&
                (dob.equals(user.getDob()) || (dob.isEmpty() && user.getDob() == null)) &&
                user.getGender().equals(gender) && uriNewImage == null;

        if (infoHasNotChanged) {
            mAccountDelegate.backToStateView();
            return;
        }

        if (!ServiceUtils.isNetworkAvailable(mContext)) {
            AlertUtils.showToast(mContext, R.string.noInternetConnection);
            return;
        }

        String token = SharedPreUtils.getInstance(mContext).getToken();

        mHelper.updateInfo(mContext, token, user, dob, gender, phoneNumber, uriNewImage)
                .doOnSubscribe(disposable -> mAccountDelegate.onStartCallAPIUpdateData())
                .subscribe(response -> {
                    if (response == null) {
                        throw new NullPointerException();
                    }

                    if (response.result == ApiResponse.RESULT_NG) {
                        ErrorUtil.handleErrorApi(mContext, response.error);
                        mAccountDelegate.onFailUpdateData();
                        return;
                    }

                    User newUser = response.getData();
                    if (newUser.getImage() != null) {
                        user.setImage(newUser.getImage());
                    }

                    if (newUser.getGender() != null) {
                        user.setGender(newUser.getGender());
                    }
                    if (newUser.getPhoneNumber() != null) {
                        user.setPhoneNumber(newUser.getPhoneNumber());
                    }
                    if (newUser.getAccountType() == AppConstant.ACCOUNT_TEACHERS && newUser.getDob() != null) {
                        user.setDob(newUser.getDob());
                    }

                    mAccountDelegate.onSuccessUpdateData(newUser);
                }, throwable -> {
                    ErrorUtil.handleException(mContext, throwable);
                    mAccountDelegate.onFailUpdateData();
                });
    }

    public void onClickEdit(MenuItem menuSave, MenuItem menuEdit, MenuItem menuChangePassword,
                            MenuItem menuSignOut, TextView tvEditImage, RecyclerView rvChildren,
                            TextView tvDob, EditText etDob, TextView tvGender, Spinner spinnerGender,
                            TextView tvPhoneNumber, EditText etPhoneNumber) {
        state = STATE_EDIT;
        new Handler().postDelayed(() -> {
            menuEdit.setVisible(false);
            menuChangePassword.setVisible(false);
            menuSignOut.setVisible(false);
            menuSave.setVisible(true);
        }, 200);

        UiUtils.showView(tvEditImage);

        if (user.getAccountType() == AppConstant.ACCOUNT_PARENTS) {
            rvChildren.setEnabled(false);
            rvChildren.setClickable(false);
            rvChildren.setAlpha(0.5f);
        } else {
            UiUtils.hideView(tvDob);
            UiUtils.showView(etDob);

            etDob.setText(tvDob.getText());
        }

        UiUtils.hideView(tvGender);
        UiUtils.showView(spinnerGender);

        String[] genders = {
                "Nam", "Nữ"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, R.layout.item_list, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinnerGender.setAdapter(adapter);
        for (int i = 0; i < genders.length; i++) {
            if (user.getGender().equals(genders[i])) {
                spinnerGender.setSelection(i, true);
                break;
            }
        }

        UiUtils.hideView(tvPhoneNumber);
        UiUtils.showView(etPhoneNumber);

        if (!tvPhoneNumber.getText().toString().equals("Chưa có")) {
            etPhoneNumber.setText(tvPhoneNumber.getText());
        }
    }

    public void onClickChildren(Child child) {
        InfoChildrenFragment childrenFragment = new InfoChildrenFragment();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstant.KEY_ID, child.getId());
        bundle.putBoolean(AppConstant.KEY_DISPLAY, true);
        childrenFragment.setArguments(bundle);

        ViewManager.getInstance().addFragment(childrenFragment, bundle,
                R.anim.translate_right_to_left, R.anim.translate_left_to_right);
    }

    public User getUser() {
        return user;
    }

    public ArrayList<Child> getChildren() {
        return children;
    }

    public void setUriNewImage(Uri uriNewImage) {
        this.uriNewImage = uriNewImage;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
