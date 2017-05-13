package com.srinnix.kindergarten.setting.fragment;

import android.graphics.Color;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.setting.delegate.SettingDelegate;
import com.srinnix.kindergarten.setting.presenter.SettingPresenter;
import com.srinnix.kindergarten.util.SharedPreUtils;
import com.srinnix.kindergarten.util.UiUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhtu on 3/6/2017.
 */

public class SettingFragment extends BaseFragment implements SettingDelegate {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.switch_noti)
    SwitchCompat mSwitchNoti;

    @BindView(R.id.spinner_language)
    AppCompatSpinner spinnerLanguage;

    private SettingPresenter mPresenter;

    private ArrayAdapter<String> adapterLanguage;
    private String[] languages={
            "Tiếng Anh" , "Tiếng Việt"
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initChildView() {
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle(mContext.getString(R.string.setting));
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(view -> {
            onBackPressed();
        });

        if (!SharedPreUtils.getInstance(mContext).isUserSignedIn()) {
            UiUtils.hideView(mSwitchNoti);
        }

        mSwitchNoti.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mPresenter.onCheckSwitch(isChecked);
        });

        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreUtils.getInstance(mContext).setLanguage(languages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapterLanguage = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, languages);
        adapterLanguage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapterLanguage);

        String language = SharedPreUtils.getInstance(mContext).getLanguage();
        for (int i = 0; i < languages.length; i++) {
            if (languages[i].equals(language)) {
                spinnerLanguage.setSelection(i, true);
                break;
            }
        }
    }

    @Override
    protected BasePresenter initPresenter() {
        mPresenter = new SettingPresenter(this);
        return mPresenter;
    }

    @OnClick(R.id.textview_about)
    public void onClickAbout() {
        // TODO: 5/12/2017
    }

    @Override
    public void updateSwitch(boolean flag) {
        mSwitchNoti.setChecked(flag);
    }
}
