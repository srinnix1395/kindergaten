package com.srinnix.kindergarten.bulletinboard.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.model.Post;

import java.util.ArrayList;

/**
 * Created by DELL on 2/3/2017.
 */

public interface BulletinBoardDelegate extends BaseDelegate {
    void updateSchoolBoard(ArrayList<Post> arrayList);

    void setErrorItemLoading();

    void handleLikePost(Integer integer);
}
