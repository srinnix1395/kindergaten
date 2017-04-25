package com.srinnix.kindergarten.bulletinboard.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.model.ImageLocal;

import java.util.ArrayList;

/**
 * Created by anhtu on 4/25/2017.
 */

public interface ImagePickerDelegate extends BaseDelegate{
    void updateStateImage(int numberImage, int position, boolean selected);

    void onLoadFail(int resError);

    void onLoadSuccess(ArrayList<ImageLocal> imageLocals);
}
