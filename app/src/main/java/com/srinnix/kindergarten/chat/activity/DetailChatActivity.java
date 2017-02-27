package com.srinnix.kindergarten.chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.chat.fragment.DetailChatFragment;

/**
 * Created by anhtu on 2/27/2017.
 */

public class DetailChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_chat);
        initFragment();
    }

    private void initFragment() {
        Intent intent = getIntent();

        DetailChatFragment fragment = new DetailChatFragment();
        fragment.setArguments(intent.getExtras());

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.layout_chat, fragment);
        transaction.commit();
    }
}
