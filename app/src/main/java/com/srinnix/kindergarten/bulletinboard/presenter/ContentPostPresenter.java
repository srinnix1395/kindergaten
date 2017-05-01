package com.srinnix.kindergarten.bulletinboard.presenter;

import android.os.Bundle;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.fragment.ImagePickerFragment;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.model.ImageLocal;
import com.srinnix.kindergarten.util.ViewManager;

import java.util.ArrayList;

/**
 * Created by anhtu on 4/24/2017.
 */

public class ContentPostPresenter extends BasePresenter {

    public ContentPostPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
    }

    public void onClickImage(ArrayList<ImageLocal> mListImage) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(AppConstant.KEY_IMAGE, mListImage);

        ViewManager.getInstance().addFragment(new ImagePickerFragment(), bundle,
                R.anim.translate_right_to_left, R.anim.translate_left_to_right);
    }

    public void onClickVideo() {

    }

    public void onClickFacebook() {

    }
}
