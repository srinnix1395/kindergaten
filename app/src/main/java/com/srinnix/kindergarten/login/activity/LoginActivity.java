package com.srinnix.kindergarten.login.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.login.fragment.ForgetPasswordFragment;
import com.srinnix.kindergarten.login.fragment.LoginFragment;

/**
 * Created by Administrator on 3/1/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private ForgetPasswordFragment passwordFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initFragment();
    }

    private void initFragment() {
        LoginFragment fragment = new LoginFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.layout_login, fragment);
        transaction.commit();
    }

    public void addOrRemoveFragment(boolean isAdd) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (isAdd) {
            passwordFragment = new ForgetPasswordFragment();
            transaction.add(R.id.layout_login, passwordFragment);
        } else {
            transaction.remove(passwordFragment);
        }
        transaction.commit();
    }
}