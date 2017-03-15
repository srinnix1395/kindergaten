package com.srinnix.kindergarten.constant;

import com.srinnix.kindergarten.R;

/**
 * Created by DELL on 2/3/2017.
 */

public class AppConstant {
    public static final String APP_NAME = "KINDERGARTEN";

    //API START
//    public static final String BASE_URL = "http://prevalentaugustus-31067.rhcloud.com/";
    public static final String BASE_URL = "http://192.168.0.102:8080/";

    public static final String API_LOGIN = "user/login";
    public static final String API_UPDATE_REG_ID = "user/updateRegID";


    public static final String API_GET_POST_MEMBER = "post/get/member";
    public static final String API_GET_POST_GUEST = "post/get/guest";
    public static final String API_LIKE_POST = "post/like";
    public static final String API_UNLIKE_POST = "post/unlike";


    public static final String API_GET_LIST_CLASS = "class/getListClass";
    public static final String API_GET_DETAIL_CLASS = "class/getClassInfo";
    public static final String API_GET_INFO_TEACHER = "class/getInfoTeacher";

    public static final String API_GET_MESSAGE = "chat/message";

    public static final String API_GET_INFO_CHILDREN = "children/getInfo";
    public static final String API_GET_TIMELINE_CHILDREN = "children/getTimeLine";
    //API END

    public static final String KINDERGARTEN_SHARED_PREFERENCES = "Kindergarten_shared_preferences";
    public static final int[] ICON_TAB_SELECTED = {
            R.drawable.ic_new_selected,
            R.drawable.ic_class_selected,
            R.drawable.ic_camera_selected,
            R.drawable.ic_children_selected
    };


    public static final int[] ICON_TAB_UNSELECTED = {
            R.drawable.ic_new_unselected,
            R.drawable.ic_class_unselected,
            R.drawable.ic_camera_unselected,
            R.drawable.ic_children_unselected
    };

    public static final int[] TITLE_TAB = {
            R.string.newFeed,
            R.string.aClass,
            R.string.camera,
            R.string.children
    };

    public static final String IS_USER_SIGNED_IN = "IS_USER_SIGNED_IN";

    public static final String USER_ID = "USER_ID";
    public static final String EMAIL = "EMAIL";
    public static final String NAME = "NAME";
    public static final String USER_TYPE = "USER_TYPE";
    public static final String TOKEN = "TOKEN";
    public static final String _ID_CLASS = "_ID_CLASS";

    public static final String LAST_EMAIL_FRAGMENT_LOGIN = "LAST_EMAIL_FRAGMENT_LOGIN";

    //post type
    public static final int POST_NORMAL = 1;
    public static final int POST_IMPORTANT = 2;

    //account type
    public static final int ACCOUNT_GUESTS = 0;
    public static final int ACCOUNT_PARENTS = 1;
    public static final int ACCOUNT_TEACHERS = 2;
    public static final int ACCOUNT_MANAGERS = 3;

    public static final String KEY_INFO = "INFO";

    public static final String HAS_DEVICE_TOKEN = "HAS_DEVICE_TOKEN";
    public static final String URL_CAMERA = "URL_CAMERA";

    public static final String MALE = "Nam";
    public static final String FEMALE = "Nữ";
}
