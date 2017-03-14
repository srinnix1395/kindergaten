package com.srinnix.kindergarten.chat.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.model.Message;

import java.util.ArrayList;

/**
 * Created by Administrator on 3/14/2017.
 */

public interface DetailChatDelegate extends BaseDelegate{
    void addMessageLast(Message message);

    void changeMessage(int position);

    void addAllMessage(ArrayList<Object> arrayList, int position);

    void loadMessageFail();

    void addMessageLast(Message message, int position);

    void removeMessage(int position);
}
