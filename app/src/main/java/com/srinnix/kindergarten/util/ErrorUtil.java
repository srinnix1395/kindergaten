package com.srinnix.kindergarten.util;

import android.content.Context;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.constant.ErrorConstant;
import com.srinnix.kindergarten.login.helper.LogoutHelper;
import com.srinnix.kindergarten.request.model.Error;

/**
 * Created by Administrator on 3/3/2017.
 */

public class ErrorUtil {
    public static void handleErrorApi(Context context, Error error) {
        switch (error.code) {
            case ErrorConstant.ERROR_CODE_101: {
                AlertUtils.showToast(context, R.string.error_msg_101);
                break;
            }
            case ErrorConstant.ERROR_CODE_102:{
                AlertUtils.showAlertDialog(context, R.string.session_expired,
                        R.string.please_re_login, R.string.OK, () -> {
                            LogoutHelper.signOut(context);
                        });
                break;
            }
            default: {
                AlertUtils.showToast(context, R.string.error_common);
                break;
            }
        }
        //// TODO: 3/3/2017 handle error code
    }

    public static void handleException(Context mContext, Throwable throwable) {
        throwable.printStackTrace();

        AlertUtils.showToast(mContext, R.string.error_common);



    }

    public static void handleException(Throwable throwable) {
        throwable.printStackTrace();
    }
}
