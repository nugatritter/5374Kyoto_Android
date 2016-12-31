package com.kubotaku.android.code4kyoto5374.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Date or Time Select Dialog Fragment
 */

public class DateTimePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private static final int TYPE_PICKER_DATE = 0;

    private static final int TYPE_PICKER_TIME = 1;

    private static final int TIME_FROM = 0;

    private static final int TIME_TO = 1;

    private static final String ARGS_TYPE = "args_type";

    private static final String ARGS_TIME = "args_time";

    private static final String ARGS_YEAR = "args_year";

    private static final String ARGS_MONTH = "args_month";

    private static final String ARGS_DAY = "args_day";

    private static final String ARGS_HOUR = "args_hour";

    private static final String ARGS_MINUTE = "args_minute";

    private int type;

    private int year;

    private int month;

    private int day;

    private int hour;

    private int minute;

    private Date selectedDate;

    private OnDismissDateTimePickerDialogListener onDialogFragmentDismissListener;

    /**
     * 日付選択ダイアログを生成する
     *
     * @param year  初期値：年
     * @param month 初期値：月
     * @param day   初期値：日
     * @return ダイアログインスタンス
     */
    public static DateTimePickerFragment createDatePickerInstance(final int year, final int month, final int day) {
        DateTimePickerFragment fragment = new DateTimePickerFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_TYPE, TYPE_PICKER_DATE);
        args.putInt(ARGS_YEAR, year);
        args.putInt(ARGS_MONTH, month);
        args.putInt(ARGS_DAY, day);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 時間選択ダイアログを生成する
     *
     * @param hour   初期値：時
     * @param minute 初期値：分
     * @return ダイアログインスタンス
     */
    public static DateTimePickerFragment createTimePickerInstance(final int hour, final int minute) {
        DateTimePickerFragment fragment = new DateTimePickerFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_TYPE, TYPE_PICKER_TIME);
        args.putInt(ARGS_HOUR, hour);
        args.putInt(ARGS_MINUTE, minute);
        fragment.setArguments(args);
        return fragment;
    }


    public DateTimePickerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            type = getArguments().getInt(ARGS_TYPE);
            year = getArguments().getInt(ARGS_YEAR, 0);
            month = getArguments().getInt(ARGS_MONTH, 0);
            day = getArguments().getInt(ARGS_DAY, 0);
            hour = getArguments().getInt(ARGS_HOUR, 0);
            minute = getArguments().getInt(ARGS_MINUTE, 0);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (type == TYPE_PICKER_DATE) {
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        } else {
            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        final Fragment targetFragment = getTargetFragment();
        if ((targetFragment != null) && (targetFragment instanceof OnDismissDateTimePickerDialogListener)) {
            this.onDialogFragmentDismissListener = (OnDismissDateTimePickerDialogListener) targetFragment;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.onDialogFragmentDismissListener = null;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
        notifySelectDateTime();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        notifySelectDateTime();
    }


    private void notifySelectDateTime() {
        if (onDialogFragmentDismissListener != null) {
            onDialogFragmentDismissListener.onDismissDialog(year, month, day, hour, minute);
        }
    }

}
