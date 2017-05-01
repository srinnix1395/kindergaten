package com.srinnix.kindergarten.messageeventbus;

import com.srinnix.kindergarten.model.ImageLocal;

import java.util.ArrayList;

/**
 * Created by anhtu on 4/25/2017.
 */

public class MessageImageLocal {
    public final ArrayList<ImageLocal> mListImage;

    public MessageImageLocal(ArrayList<ImageLocal> mListImage) {
        this.mListImage = mListImage;
    }
}
