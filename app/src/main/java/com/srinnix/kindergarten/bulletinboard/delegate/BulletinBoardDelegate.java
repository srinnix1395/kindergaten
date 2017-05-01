package com.srinnix.kindergarten.bulletinboard.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.model.Post;
import com.srinnix.kindergarten.request.model.PostResponse;

import java.util.ArrayList;

/**
 * Created by DELL on 2/3/2017.
 */

public interface BulletinBoardDelegate extends BaseDelegate {
    void updateSchoolBoard(ArrayList<Post> arrayList, boolean isLoadFirst);

    void setErrorItemLoading();

    void handleLikePost(Integer integer, boolean like, int numberOfLikes);

    void onRefreshResult(boolean result, PostResponse data);

    void updateNumberComment(int position, int numberOfComments);

    void updateLogout();

    void onGetImportantPost(boolean result, PostResponse data);

    void deletePost(int i);
}
