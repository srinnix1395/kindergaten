package com.srinnix.kindergarten.messageeventbus;

import com.srinnix.kindergarten.model.MediaLocal;

import java.util.ArrayList;

/**
 * Created by anhtu on 4/25/2017.
 */

public class MessageImageLocal {
    public final ArrayList<MediaLocal> mListImage;

    public MessageImageLocal(ArrayList<MediaLocal> mListImage) {
        this.mListImage = mListImage;
    }
}
