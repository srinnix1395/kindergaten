package com.srinnix.kindergarten.util;

import android.content.Context;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.constant.ErrorConstant;
import com.srinnix.kindergarten.request.model.Error;

/**
 * Created by Administrator on 3/3/2017.
 */

public class ErrorUtil {
    public static void handleErrorApi(Context context, Error error) {
        switch (error.code) {
            case ErrorConstant.ERROR_CODE_101:{
                AlertUtils.showToast(context, R.string.error_msg_101);
                break;
            }
            default:{
                AlertUtils.showToast(context, R.string.commonError);
                break;
            }
        }
        //// TODO: 3/3/2017 handle error code
    }
}
