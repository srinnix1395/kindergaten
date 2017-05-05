package com.srinnix.kindergarten.clazz.delegate;

import com.srinnix.kindergarten.base.delegate.BaseDelegate;
import com.srinnix.kindergarten.model.StudyTimetable;

import java.util.ArrayList;

/**
 * Created by anhtu on 5/5/2017.
 */

public interface StudyTimetableDelegate extends BaseDelegate {

    void onSuccessStudyTimetable(ArrayList<StudyTimetable> data);

    void onFail(int resError);
}
