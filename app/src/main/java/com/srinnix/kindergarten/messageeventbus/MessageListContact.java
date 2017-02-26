package com.srinnix.kindergarten.messageeventbus;

import com.srinnix.kindergarten.model.Contact;

import java.util.ArrayList;

/**
 * Created by anhtu on 2/25/2017.
 */

public class MessageListContact {
    public final ArrayList<Contact> arrayList;

    public MessageListContact(ArrayList<Contact> arrayList) {
        this.arrayList = arrayList;
    }
}
