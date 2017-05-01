package com.srinnix.kindergarten.bulletinboard.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.srinnix.kindergarten.R;
import com.srinnix.kindergarten.base.fragment.BaseFragment;
import com.srinnix.kindergarten.base.presenter.BasePresenter;
import com.srinnix.kindergarten.constant.AppConstant;
import com.srinnix.kindergarten.messageeventbus.MessageEnabledNotificationRange;
import com.srinnix.kindergarten.util.UiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by anhtu on 5/1/2017.
 */

public class SettingPostFragment extends BaseFragment {
    @BindView(R.id.layout_schedule)
    LinearLayout layoutSchedule;

    @BindView(R.id.radio_now)
    RadioButton rbNow;

    @BindView(R.id.radio_all)
    RadioButton rbAll;

    @BindView(R.id.radio_parent)
    RadioButton rbParent;

    @BindView(R.id.textview_schedule_day)
    TextView tvScheduleDay;

    @BindView(R.id.textview_schedule_hour)
    TextView tvScheduleHour;

    @BindView(R.id.radiogroup_notification_range)
    RadioGroup radioGroupRange;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting_post;
    }

    @Override
    protected void initChildView() {
        radioGroupRange.setAlpha(0.7f);

        rbNow.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (layoutSchedule.getVisibility() == View.VISIBLE) {
                    layoutSchedule.setVisibility(View.GONE);
                }
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-kk:mm", Locale.getDefault());
                String times[] = dateFormat.format(new Date(System.currentTimeMillis())).split("-");
                tvScheduleDay.setText(times[0]);
                tvScheduleHour.setText(times[1]);

                if (layoutSchedule.getVisibility() != View.VISIBLE) {
                    layoutSchedule.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @OnClick({R.id.textview_schedule_day, R.id.textview_schedule_hour})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textview_schedule_day: {
                UiUtils.showDatePickerDialog(mContext, (view1, year, month, dayOfMonth) -> {

                });
                break;
            }
            case R.id.textview_schedule_hour: {
                UiUtils.showTimePickerDialog(mContext, (view12, hourOfDay, minute) -> {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("kk:mm", Locale.getDefault());
                    tvScheduleHour.setText(dateFormat.format(new Date()));
                });
                break;
            }
        }
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    public int getNotificationRange() {
        if (rbNow.isChecked()) {
            return AppConstant.NOTIFICATION_ALL;
        }

        if (rbParent.isChecked()) {
            return AppConstant.NOTIFICATION_PARENT;
        }

        return AppConstant.NOTIFICATION_TEACHER;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void onEventEnabledNotificationRange(MessageEnabledNotificationRange message) {
        if (message.enabled) {
            radioGroupRange.setAlpha(1f);
        } else {
            radioGroupRange.setAlpha(0.7f);
        }

        for (int i = 0; i < radioGroupRange.getChildCount(); i++) {
            radioGroupRange.getChildAt(i).setEnabled(message.enabled);
        }
    }
}
