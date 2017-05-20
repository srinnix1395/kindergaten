package com.srinnix.kindergarten.bulletinboard.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.model.MediaLocal;

import java.util.ArrayList;

/**
 * Created by anhtu on 4/25/2017.
 */

public interface MediaPickerDelegate extends BaseDelegate{
    void updateStateMedia(int numberImage, int numberVideo, int position, boolean selected);

    void onLoadFail(int resError);

    void onLoadSuccess(ArrayList<MediaLocal> mediaLocals);

    void insertMediaLocal();
}
