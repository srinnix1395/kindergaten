package com.srinnix.kindergarten.constant;

/**
 * Created by DELL on 2/3/2017.
 */

public class AppConstant {
    public static final String APP_NAME = "KINDERGARTEN";

    public static final long TIME_BETWEEN_2_MESSAGE = 7200000;
    public static final int ITEM_COMMENT_PER_PAGE = 15;
    public static final int ITEM_LIKE_PER_PAGE = 20;
    public static final int ITEM_HEALTH_PER_PAGE = 6;
    public static final int ITEM_POST_PER_PAGE = 5;
    public static final int ITEM_IMAGE_PER_PAGE = 1;

    //API START
    public static final String BASE_URL = "http://prevalentaugustus-31067.rhcloud.com/";
//    public static final String BASE_URL = "http://192.168.24.108:8080/";

    public static final String API_LOGIN = "user/login";
    public static final String API_UPDATE_REG_ID = "user/updateRegID";
    public static final String API_RESET_PASSWORD = "user/requestResetPassword";
    public static final String API_GET_ACCOUNT_INFO = "user/getInfo";
    public static final String API_CHANGE_PASSWORD = "user/changePassword";
    public static final String API_UPDATE_INFO = "user/updateInfo";

    public static final String API_GET_POST_MEMBER = "post/get/member";
    public static final String API_GET_POST_GUEST = "post/get/guest";
    public static final String API_LIKE_POST = "post/like";
    public static final String API_UNLIKE_POST = "post/unlike";
    public static final String API_GET_LIST_NUMBER_LIKE = "post/getListNumberLike";
    public static final String API_GET_NEW_POST = "post/getNewPost";
    public static final String API_GET_COMMENT = "post/getComment";
    public static final String API_INSERT_COMMENT = "post/insertComment";
    public static final String API_INSERT_POST = "post/insert";
    public static final String API_GET_IMPORTANT_POST = "post/getImportantPost";
    public static final String API_GET_DETAIL_POST = "post/getDetail";

    public static final String API_GET_LIST_CLASS = "class/getList";
    public static final String API_GET_DETAIL_CLASS = "class/getDetail";
    public static final String API_GET_INFO_TEACHER = "class/getInfoTeacher";
    public static final String API_GET_IMAGE_CLASS = "class/getImage";
    public static final String API_GET_TIMETABLE = "class/getTimetable";
    public static final String API_GET_STUDY_TIMETABLE = "class/getStudyTimetable";
    public static final String API_INSERT_IMAGE = "class/insertImage";

    public static final String API_GET_MESSAGE = "chat/getMessage";
    public static final String API_UPLOAD_MESSAGE_IMAGE = "chat/uploadImages";

    public static final String API_GET_INFO_CHILDREN = "children/getInfo";
    public static final String API_GET_HEALTH_INDEX_CHILDREN = "children/getHealthIndex";
    public static final String API_GET_LIST_CHILDREN = "children/getList";

    //API END

    public static final String KINDERGARTEN_SHARED_PREFERENCES = "Kindergarten_shared_preferences";

    public static final String IS_USER_SIGNED_IN = "IS_USER_SIGNED_IN";

    public static final String USER_ID = "USER_ID";
    public static final String EMAIL = "EMAIL";
    public static final String NAME = "NAME";
    public static final String USER_TYPE = "USER_TYPE";
    public static final String TOKEN = "TOKEN";
    public static final String _ID_CLASS = "_ID_CLASS";
    public static final String IMAGE = "IMAGE";
    public static final String GENDER = "GENDER";
    public static final String CLASS_NAME = "CLASS_NAME";

    public static final String LAST_EMAIL_FRAGMENT_LOGIN = "LAST_EMAIL_FRAGMENT_LOGIN";

    //post type
    public static final int POST_NORMAL = 1;
    public static final int POST_IMPORTANT = 2;

    //account type
    public static final int ACCOUNT_GUESTS = 0;
    public static final int ACCOUNT_PARENTS = 1;
    public static final int ACCOUNT_TEACHERS = 2;
    public static final int ACCOUNT_MANAGERS = 3;

    public static final String SERVER_HAS_DEVICE_TOKEN = "HAS_DEVICE_TOKEN";
    public static final String URL_CAMERA = "URL_CAMERA";

    public static final String MALE = "Nam";
    public static final String FEMALE = "Nữ";

    public static final String KEY_ID = "KEY_ID";
    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_STATUS = "KEY_STATUS";
    public static final String KEY_MEDIA = "KEY_MEDIA";
    public static final String KEY_ICON = "KEY_ICON";
    public static final String KEY_ACCOUNT_TYPE = "KEY_ACCOUNT_TYPE";
    public static final String KEY_CLASS = "KEY_CLASS";
    public static final String KEY_COMMENT = "KEY_COMMENT";
    public static final String KEY_LIKE = "KEY_LIKE";
    public static final String KEY_HEALTH = "KEY_HEALTH";
    public static final String KEY_HEALTH_TYPE = "KEY_HEALTH_TYPE";
    public static final String KEY_GENDER = "KEY_GENDER";
    public static final String KEY_DOB = "KEY_DOB";
    public static final String KEY_GROUP = "KEY_GROUP";
    public static final String KEY_TIME = "KEY_TIME";
    public static final String KEY_FRAGMENT = "KEY_FRAGMENT";
    public static final String KEY_RECEIVE_NOTIFICATION = "KEY_RECEIVE_NOTIFICATION";
    public static final String KEY_DISPLAY = "KEY_DISPLAY";
    public static final String KEY_MEDIA_TYPE = "KEY_MEDIA_TYPE";
    public static final String KEY_CONVERSATION_ID = "KEY_CONVERSATION_ID";
    public static final String KEY_LIMIT = "KEY_LIMIT";
    public static final String KEY_POST = "KEY_POST";

    public static final int UPDATE_ALL_VIEW_HOLDER = 1;

    public static final int FRAGMENT_BULLETIN_BOARD = 5;
    public static final int FRAGMENT_CLASS = 6;
    public static final int FRAGMENT_CAMERA = 7;
    public static final int FRAGMENT_CHILDREN = 8;
    public static final int FRAGMENT_INFO_CHILDREN = 9;
    public static final int FRAGMENT_HEALTH_INDEX = 10;
    public static final int FRAGMENT_STUDY_TIMETABLE = 11;
    public static final int FRAGMENT_DETAIL_POST = 12;
    public static final int FRAGMENT_DETAIL_CHAT = 13;
    public static final int FRAGMENT_POST = 14;
    public static final int FRAGMENT_POST_IMAGE = 15;
    public static final int FRAGMENT_CHAT_LIST = 16;
    public static final int NOTIFICATION = 17;

    public static final int STATE_WEIGHT_NORMAL = 1;
    public static final int STATE_WEIGHT_OBESE = 2;
    public static final int STATE_WEIGHT_MALNUTRITION = 3;

    public static final int STATE_HEIGHT_NORMAL = 1;
    public static final int STATE_HEIGHT_STUNTED = 2;

    public static final int UNSPECIFIED = -1;

    public static final int TYPE_WEIGHT = 1;
    public static final int TYPE_HEIGHT = 2;

    public static final int NOTIFICATION_ALL = 1;
    public static final int NOTIFICATION_PARENT = 2;
    public static final int NOTIFICATION_TEACHER = 3;

    public static final String GROUP_TINY = "Nhà trẻ";
    public static final String GROUP_SMALL = "Mẫu giáo bé";
    public static final String GROUP_MEDIUM = "Mẫu giáo nhỡ";
    public static final String GROUP_BIG = "Mẫu giáo lớn";

    public static final String LANGUAGE = "LANGUAGE";
    public static final String REG_ID = "REG_ID";

    public static final String NOW = "now";

    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;
    public static final int NOTIFICATION_ID = 16516;
}
