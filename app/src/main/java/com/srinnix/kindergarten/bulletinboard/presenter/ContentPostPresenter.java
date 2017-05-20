package com.srinnix.kindergarten.bulletinboard.presenter;

import android.os.Bundle;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.fragment.MediaPickerFragment;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.MediaLocal;
import com.srinnix.kindergarten.util.ViewManager;

import java.util.ArrayList;

/**
 * Created by anhtu on 4/24/2017.
 */

public class ContentPostPresenter extends BasePresenter {

    public ContentPostPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
    }

    public void onClickImage(ArrayList<MediaLocal> mListMedia) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AppConstant.KEY_MEDIA, mListMedia);

        ViewManager.getInstance().addFragment(new MediaPickerFragment(), bundle,
                R.anim.translate_right_to_left, R.anim.translate_left_to_right);
    }

    public void onClickVideo(ArrayList<MediaLocal> mListMedia) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AppConstant.KEY_MEDIA, mListMedia);
        bundle.putInt(AppConstant.KEY_MEDIA_TYPE, AppConstant.TYPE_VIDEO);

        ViewManager.getInstance().addFragment(new MediaPickerFragment(), bundle,
                R.anim.translate_right_to_left, R.anim.translate_left_to_right);
    }

    public void onClickFacebook() {

    }
}
