package com.srinnix.kindergarten.chat.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.model.Message;

import java.util.ArrayList;

/**
 * Created by Administrator on 3/14/2017.
 */

public interface DetailChatDelegate extends BaseDelegate{
    void addMessage(Message message, int position);

    void changeDataMessage(int position);

    void loadMessageSuccess(ArrayList<Object> arrayList, boolean isLoadingDataFirst);

    void loadMessageFail(boolean isLoadingDataFirst);

    void removeMessage(int position);

    void changeDataMessage(int position, boolean isDisplayIconPayload);

    void changeDataMessage(int position, int statusMessagePayload);

    void setStatus(String status);
}
