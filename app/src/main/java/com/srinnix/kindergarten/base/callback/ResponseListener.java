package com.srinnix.kindergarten.base.callback;

import com.srinnix.kindergarten.request.model.ApiResponse;

/**
 * Created by anhtu on 4/5/2017.
 */

public interface ResponseListener<T> {
    void onSuccess(ApiResponse<T> response);

    void onFail(Throwable throwable);

    void onFinally();
}
