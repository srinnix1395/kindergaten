package com.srinnix.kindergarten.bulletinboard.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.model.Comment;

import java.util.ArrayList;

/**
 * Created by anhtu on 3/28/2017.
 */

public interface CommentDelegate extends BaseDelegate {
    void onLoadCommentSuccess(ArrayList<Comment> commentArrayList, boolean isLoadFirst);

    void onLoadCommentFail(int noInternetConnection, boolean isLoadFirst);

    void insertCommentSuccess(Comment comment);
}
