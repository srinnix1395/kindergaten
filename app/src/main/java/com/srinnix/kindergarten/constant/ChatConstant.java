package com.srinnix.kindergarten.constant;

/**
 * Created by Administrator on 2/22/2017.
 */

public class ChatConstant {

    public static final String SERVER_URL = "http://prevalentaugustus-31067.rhcloud.com/";
//        public static final String SERVER_URL = "http://192.168.0.103";

    //Event START
    public static final String EVENT_SETUP = "setup";
    public static final String EVENT_SEND_SUCCESSFULLY = "send_successfully";

    //Event END

    //Key data START
    public static final String _ID = "_id";
    public static final String _ID_SENDER = "_id_sender";
    public static final String _ID_RECEIVER = "_id_receiver";
    public static final String MESSAGE = "message";
    public static final String _ID_MESSAGE_CLIENT = "id_message_client";
    public static final String CREATED_AT = "created_at";

    //Key data END

    public static final int ONLINE = 1;
    public static final int OFFLINE = 2;
    public static final int UNDEFINED = 3;

    public static final int TYPING = 0;
    public static final int PENDING = 1;
    public static final int SERVER_RECEIVED = 2;
    public static final int FRIEND_RECEIVED = 3;
    public static final int HANDLE_COMPLETE = 4;

    //chat item
    public static final int SINGLE = 0;
    public static final int FIRST = 1;
    public static final int MIDDLE = 2;
    public static final int LAST = 3;

    public static final int ITEM_MESSAGE_PER_PAGE = 30;
    public static final long TIME_DISTANCE = 300000;
}
