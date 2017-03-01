package com.srinnix.kindergarten.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.srinnix.kindergarten.R;

/**
 * Created by Administrator on 3/1/2017.
 */

public class LoginActivity extends AppCompatActivity {
    private LoginFragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initFragment();
    }

    private void initFragment() {
        mFragment = new LoginFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.layout_login, mFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (mFragment != null) {
            mFragment.onBackPressed();
        }
    }
}
