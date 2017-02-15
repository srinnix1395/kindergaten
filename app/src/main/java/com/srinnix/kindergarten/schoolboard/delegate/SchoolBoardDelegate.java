package com.srinnix.kindergarten.schoolboard.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.model.Post;

import java.util.ArrayList;

/**
 * Created by DELL on 2/3/2017.
 */

public interface SchoolBoardDelegate extends BaseDelegate {
    void updateSchoolBoard(ArrayList<Post> arrayList);
}
