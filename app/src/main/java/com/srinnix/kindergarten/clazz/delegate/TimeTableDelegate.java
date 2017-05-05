package com.srinnix.kindergarten.clazz.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.model.Timetable;

/**
 * Created by anhtu on 5/4/2017.
 */

public interface TimeTableDelegate extends BaseDelegate{
    void onFail(int resError);

    void onSuccessTimetable(Timetable data);

}
