package com.srinnix.kindergarten.setting.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.children.adapter.ChildrenAdapter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.login.helper.LogoutHelper;
import com.srinnix.kindergarten.model.Child;
import com.srinnix.kindergarten.model.User;
import com.srinnix.kindergarten.setting.delegate.AccountDelegate;
import com.srinnix.kindergarten.setting.presenter.AccountPresenter;
import com.srinnix.kindergarten.util.AlertUtils;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.StringUtil;
import com.srinnix.kindergarten.util.UiUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.srinnix.kindergarten.setting.presenter.AccountPresenter.STATE_EDIT;
import static com.srinnix.kindergarten.setting.presenter.AccountPresenter.STATE_VIEW;

/**
 * Created by anhtu on 4/20/2017.
 */

public class AccountFragment extends BaseFragment implements AccountDelegate {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.layout_retry)
    RelativeLayout layoutRetry;

    @BindView(R.id.textview_retry)
    TextView tvRetry;

    @BindView(R.id.progressbar_loading)
    ProgressBar pbLoading;

    @BindView(R.id.imageview_icon)
    ImageView imvIcon;

    @BindView(R.id.textview_name)
    TextView tvName;

    @BindView(R.id.textview_DOB_title)
    TextView tvDOBtitle;

    @BindView(R.id.textview_DOB)
    TextView tvDob;

    @BindView(R.id.textview_gender)
    TextView tvGender;

    @BindView(R.id.textview_email)
    TextView tvEmail;

    @BindView(R.id.textview_account)
    TextView tvAccountType;

    @BindView(R.id.textview_phone_number)
    TextView tvPhoneNumber;

    @BindView(R.id.image_work)
    ImageView imvWork;

    @BindView(R.id.textview_work)
    TextView tvWork;

    @BindView(R.id.textview_class_title)
    TextView tvClassTitle;

    @BindView(R.id.textview_class)
    TextView tvClass;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    @BindView(R.id.recycler_view_children)
    RecyclerView rvChildren;

    @BindView(R.id.cardview_icon)
    CardView cardViewIcon;

    @BindView(R.id.edittext_DOB)
    EditText etDob;

    @BindView(R.id.spinner_gender)
    Spinner spinnerGender;

    @BindView(R.id.edittext_phone_number)
    EditText etPhoneNumber;

    @BindView(R.id.textview_edit_image)
    TextView tvEditImage;

    @BindView(R.id.progressbar_loading_api)
    ProgressBar pbLoadingAPI;

    private MenuItem menuSave;
    private MenuItem menuEdit;
    private MenuItem menuChangePassword;
    private MenuItem menuSignOut;

    private ArrayList<Child> listChild;
    private ChildrenAdapter childrenAdapter;

    private AccountPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_account;
    }

    @Override
    protected void initData() {
        super.initData();
        listChild = new ArrayList<>();
        childrenAdapter = new ChildrenAdapter(listChild, ChildrenAdapter.TYPE_LINEAR, position -> {

        });
    }

    @Override
    protected void initChildView() {
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.inflateMenu(R.menu.menu_account_fragment);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_item_edit: {
                    mPresenter.onClickEdit(menuSave, menuEdit, menuChangePassword,
                            menuSignOut, tvEditImage, rvChildren, tvDob, etDob,
                            tvGender, spinnerGender, tvPhoneNumber, etPhoneNumber);
                    break;
                }
                case R.id.menu_item_change_password: {
                    mPresenter.onClickChangePassword();
                    break;
                }
                case R.id.menu_item_sign_out: {
                    AlertUtils.showDialogConfirm(mContext, R.string.confirm_log_out, R.string.sign_out, (dialog, which) -> {
                        LogoutHelper.signOut(mContext);
                        onBackPressed();
                    });
                    break;
                }
                case R.id.menu_item_save: {
                    UiUtils.hideKeyboard(getActivity());
                    mPresenter.onClickSave(etDob.getText().toString(),
                            spinnerGender.getSelectedItem().toString(),
                            etPhoneNumber.getText().toString().trim());
                    break;
                }
            }
            return false;
        });
        menuSave = toolbar.getMenu().findItem(R.id.menu_item_save);
        menuEdit = toolbar.getMenu().findItem(R.id.menu_item_edit);
        menuChangePassword = toolbar.getMenu().findItem(R.id.menu_item_change_password);
        menuSignOut = toolbar.getMenu().findItem(R.id.menu_item_sign_out);

        menuSave.setVisible(false);
        menuEdit.setVisible(false);

        rvChildren.setLayoutManager(new LinearLayoutManager(mContext));
        rvChildren.setAdapter(childrenAdapter);

        pbLoading.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary)
                , PorterDuff.Mode.SRC_ATOP);
    }


    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new AccountPresenter(this);
        return mPresenter;
    }

    @OnClick(R.id.imageview_icon)
    public void onClickIcon() {
        mPresenter.onClickIcon(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AccountPresenter.PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            mPresenter.setUriNewImage(uri);
            Glide.with(mContext)
                    .load(uri)
                    .thumbnail(0.5f)
                    .placeholder(R.drawable.dummy_image)
                    .error(R.drawable.ic_user_gray)
                    .into(imvIcon);
        }
    }

    @OnClick(R.id.edittext_DOB)
    public void onClickEdittextDob() {
        UiUtils.showDatePickerDialog(mContext, tvDob.getText().toString(), (view, year, month, dayOfMonth) -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth);
            etDob.setText(simpleDateFormat.format(c.getTime()));
        });
    }

    @OnClick(R.id.layout_retry)
    public void onClickRetry() {
        UiUtils.hideView(layoutRetry);

        UiUtils.showProgressBar(pbLoading);
        mPresenter.getInfo();
    }

    @Override
    public void onFailGetData(int resError) {
        UiUtils.hideProgressBar(pbLoading);

        tvRetry.setText(resError);
        UiUtils.showView(layoutRetry);
    }

    @Override
    public void onSuccessGetData(User user, ArrayList<Child> children) {
        menuEdit.setVisible(true);

        UiUtils.hideProgressBar(pbLoading);
        UiUtils.showView(scrollView);

        toolbar.setTitle(user.getName());
        tvName.setText(user.getName());
        tvGender.setText(user.getGender());
        tvEmail.setText(user.getEmail());
        tvAccountType.setText(StringUtil.getAccountType(user.getAccountType()));
        tvPhoneNumber.setText(user.getPhoneNumber() == null ? "Chưa có" : user.getPhoneNumber());
        UiUtils.showView(cardViewIcon);
        tvEmail.setText(SharedPreUtils.getInstance(mContext).getEmail());
        Glide.with(mContext)
                .load(user.getImage())
                .thumbnail(0.5f)
                .placeholder(R.drawable.dummy_image)
                .error(R.drawable.ic_user_gray)
                .into(imvIcon);

        if (user.getAccountType() == AppConstant.ACCOUNT_PARENTS) {
            imvWork.setImageResource(R.drawable.image_children_circle);
            tvWork.setText(R.string.children);

            if (!listChild.isEmpty()) {
                listChild.clear();
            }
            listChild.addAll(children);
            childrenAdapter.notifyDataSetChanged();

            UiUtils.hideView(tvDob);
            UiUtils.hideView(tvDOBtitle);

            UiUtils.hideView(tvClass);
            UiUtils.hideView(tvClassTitle);
        } else {
            tvDob.setText(user.getDob());

            imvWork.setImageResource(R.drawable.image_class);
            tvWork.setText(R.string.work);

            tvClass.setText(user.getClassName());

            UiUtils.hideView(rvChildren);
        }
    }

    @Override
    public void onStartCallAPIUpdateData() {
        menuSave.setVisible(false);
        UiUtils.showProgressBar(pbLoadingAPI);
    }

    @Override
    public void onSuccessUpdateData(User user) {
        AlertUtils.showToastSuccess(mContext, R.drawable.ic_account_check, R.string.update_successfully);
        mPresenter.setState(STATE_VIEW);

        UiUtils.hideProgressBar(pbLoadingAPI);
        menuSave.setVisible(false);
        menuEdit.setVisible(true);
        menuChangePassword.setVisible(true);
        menuSignOut.setVisible(true);

        UiUtils.hideView(tvEditImage);
        if (user.getAccountType() == AppConstant.ACCOUNT_PARENTS) {
            rvChildren.setEnabled(true);
            rvChildren.setAlpha(1f);
        } else {
            UiUtils.hideView(etDob);
            UiUtils.showView(tvDob);
        }

        UiUtils.hideView(spinnerGender);
        UiUtils.showView(tvGender);
        spinnerGender.setAdapter(null);

        UiUtils.hideView(etPhoneNumber);
        UiUtils.showView(tvPhoneNumber);

        if (user.getImage() != null) {
            Glide.with(mContext)
                    .load(user.getImage())
                    .thumbnail(0.5f)
                    .placeholder(R.drawable.dummy_image)
                    .error(R.drawable.ic_user_gray)
                    .into(imvIcon);
        }

        if (user.getGender() != null) {
            tvGender.setText(user.getGender());
        }
        if (user.getPhoneNumber() != null) {
            tvPhoneNumber.setText(user.getPhoneNumber());
        }
        if (user.getAccountType() == AppConstant.ACCOUNT_TEACHERS && user.getDob() != null) {
            tvDob.setText(user.getDob());
        }
    }

    @Override
    public void onFailUpdateData() {
        UiUtils.hideProgressBar(pbLoadingAPI);
        menuSave.setVisible(true);
    }

    @Override
    public void onBackPressed() {
        if (mPresenter.getState() == STATE_EDIT) {
            AlertUtils.showDialogConfirm(mContext, R.string.message_cancel_edit_info, R.string.next, (dialog, which) -> {
                mPresenter.setState(STATE_VIEW);

                menuEdit.setVisible(true);
                menuSave.setVisible(false);
                menuChangePassword.setVisible(true);
                menuSignOut.setVisible(true);

                UiUtils.hideView(tvEditImage);
                if (mPresenter.getUser().getAccountType() == AppConstant.ACCOUNT_PARENTS) {
                    rvChildren.setEnabled(true);
                    rvChildren.setAlpha(1f);
                } else {
                    UiUtils.hideView(etDob);
                    UiUtils.showView(tvDob);

                    tvDob.setText(mPresenter.getUser().getDob());
                }

                UiUtils.hideView(spinnerGender);
                UiUtils.showView(tvGender);
                spinnerGender.setAdapter(null);

                UiUtils.hideView(etPhoneNumber);
                UiUtils.showView(tvPhoneNumber);

                Glide.with(mContext)
                        .load(mPresenter.getUser().getImage())
                        .thumbnail(0.5f)
                        .placeholder(R.drawable.dummy_image)
                        .error(R.drawable.ic_user_gray)
                        .into(imvIcon);
                tvGender.setText(mPresenter.getUser().getGender());
                tvPhoneNumber.setText(mPresenter.getUser().getPhoneNumber() == null ?
                        "Chưa có" : mPresenter.getUser().getPhoneNumber());
            });
            return;
        }

        super.onBackPressed();
    }
}
