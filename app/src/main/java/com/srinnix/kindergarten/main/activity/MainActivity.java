package com.srinnix.kindergarten.main.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.chat.fragment.DetailChatFragment;
import com.srinnix.kindergarten.clazz.fragment.ClassFragment;
import com.srinnix.kindergarten.main.delegate.MainDelegate;
import com.srinnix.kindergarten.main.fragment.MainFragment;

/**
 * Created by DELL on 2/4/2017.
 */

public class MainActivity extends AppCompatActivity implements MainDelegate{
	private MainFragment mainFragment;
    private DetailChatFragment detailChatFragment;
    private ClassFragment classFragment;
    @Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initChildViews();
	}

	private void initChildViews() {
//		mainFragment = MainFragment.newInstance();
//        detailChatFragment = new DetailChatFragment();
        classFragment = new ClassFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.relative_layout_container, classFragment);
		fragmentTransaction.commit();
	}

//	@Override
//	public void onBackPressed() {
//        mainFragment.onBackPressed();
//	}
}
