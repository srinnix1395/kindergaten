package com.srinnix.kindergarten.constant;

import com.srinnix.kindergarten.R;

/**
 * Created by DELL on 2/3/2017.
 */

public class AppConstant {
    //API START
    public static final String BASE_URL = "http://prevalentaugustus-31067.rhcloud.com/";

    public static final String API_LOGIN = "/user/login";

    //API END

    public static final String KINDERGARTEN_SHARED_PREFERENCES = "Kindergarten_shared_preferences";
	public static final int[] ICON_TAB_SELECTED = {
			R.drawable.ic_newfeed_selected,
			R.drawable.ic_class_selected,
			R.drawable.ic_camera_selected,
			R.drawable.ic_children_selected
	};
	
	
	public static final int[] ICON_TAB_UNSELECTED = {
			R.drawable.ic_newfeed_unselected,
			R.drawable.ic_class_unselected,
			R.drawable.ic_camera_unselected,
			R.drawable.ic_children_unselected
	};
	public static final String IS_USER_SIGNED_IN = "IS_USER_SIGNED_IN";
	
	public static final String USER_ID = "USER_ID";
    public static final String EMAIL = "EMAIL";
    public static final String USERNAME = "USERNAME";
    public static final String USER_TYPE = "USER_TYPE";
    public static final String TOKEN = "TOKEN";
    public static final String CREATED_AT = "CREATED_AT";

    public static final String KEY_NAME_CONVERSATION = "KEY_NAME_CONVERSATION";
	public static final String LAST_EMAIL_FRAGMENT_LOGIN = "LAST_EMAIL_FRAGMENT_LOGIN";
}
