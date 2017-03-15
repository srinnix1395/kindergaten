package com.srinnix.kindergarten.chat.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.model.ContactParent;
import com.srinnix.kindergarten.model.ContactTeacher;

import java.util.ArrayList;

/**
 * Created by anhtu on 3/14/2017.
 */

public interface ChatListDelegate extends BaseDelegate {
    void addContactTeacher(ArrayList<ContactTeacher> contactTeachers);

    void addContactParent(ArrayList<ContactParent> contactParents);

    void updateStatus();
}
