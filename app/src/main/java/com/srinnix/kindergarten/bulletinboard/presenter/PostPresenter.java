package com.srinnix.kindergarten.bulletinboard.presenter;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.bulletinboard.fragment.ImagePickerFragment;
import com.srinnix.kindergarten.bulletinboard.helper.PostHelper;
import com.srinnix.kindergarten.util.ViewManager;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by anhtu on 4/24/2017.
 */

public class PostPresenter extends BasePresenter {

    private CompositeDisposable mDisposable;
    private PostHelper mHelper;

    public PostPresenter(BaseDelegate mDelegate) {
        super(mDelegate);
        mDisposable = new CompositeDisposable();
        mHelper = new PostHelper(mDisposable);
    }


    public void onClickPost(CharSequence text) {

    }

    public void onClickImage() {
        ViewManager.getInstance().addFragment(new ImagePickerFragment(), null,
                R.anim.translate_right_to_left, R.anim.translate_left_to_right);
    }

    public void onClickVideo() {

    }

    public void onClickFacebook() {

    }

    public void onClickSchedule() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.clear();
        }
    }
}
