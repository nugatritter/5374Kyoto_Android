/**
 * Copyright 2017 kubotaku1119 <kubotaku1119@gmail.com>
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kubotaku.android.code4kyoto5374.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.kubotaku.android.code4kyoto5374.R;
import com.kubotaku.android.code4kyoto5374.data.Alarm;
import com.kubotaku.android.code4kyoto5374.util.AlarmService;
import com.kubotaku.android.code4kyoto5374.util.Prefs;

/**
 * 通知設定ダイアログ
 */
public class NotifySettingsDialogFragment extends DialogFragment
        implements OnDismissDateTimePickerDialogListener {

    public static final String TAG = NotifySettingsDialogFragment.class.getSimpleName();

    private static final String ARGS_TYPE = "args_type";

    public NotifySettingsDialogFragment() {
        // Required empty public constructor
    }

    public static NotifySettingsDialogFragment newInstance(final int garbageType) {
        NotifySettingsDialogFragment fragment = new NotifySettingsDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARGS_TYPE, garbageType);
        fragment.setArguments(args);
        return fragment;
    }

    private Alarm alarm;

    private int garbageType;

    private OnDismissDialogFragmentListener onDismissDialogFragmentListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            garbageType = getArguments().getInt(ARGS_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notify_settings_dialog, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // title非表示
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Dialog dialog = getDialog();
        dialog.setCancelable(true);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dialogWidth = (int) (metrics.widthPixels * 0.9f);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = dialogWidth;
        dialog.getWindow().setAttributes(lp);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        onDismissDialogFragmentListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        final Fragment targetFragment = getTargetFragment();
        if ((targetFragment != null) && (targetFragment instanceof OnDismissDialogFragmentListener)) {
            onDismissDialogFragmentListener = (OnDismissDialogFragmentListener) targetFragment;
        }

        loadAlarmSettings();
        setupViews();
    }

    private void loadAlarmSettings() {
        this.alarm = Prefs.loadAlarm(getContext(), garbageType);
    }

    private void setupViews() {
        final View view = getView();

        final TextView alarmTime = (TextView) view.findViewById(R.id.alarm_text_time);
        alarmTime.setText(this.alarm.toString());
        alarmTime.setOnClickListener(onClickAlarmTimeListener);

        final Switch alarmSwitch = (Switch) view.findViewById(R.id.alarm_switch);
        alarmSwitch.setChecked(this.alarm.enable);
        alarmSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    private View.OnClickListener onClickAlarmTimeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final DateTimePickerFragment picker = DateTimePickerFragment.createTimePickerInstance(alarm.hour, alarm.minute);
            picker.setTargetFragment(NotifySettingsDialogFragment.this, 0);
            picker.show(getFragmentManager(), null);
        }
    };

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            alarm.enable = b;
            Prefs.saveAlarm(getContext(), garbageType, alarm);
        }
    };

    @Override
    public void onDismissDialog(int year, int month, int day, int hour, int minute) {
        this.alarm.hour = hour;
        this.alarm.minute = minute;
        final TextView alarmTime = (TextView) getView().findViewById(R.id.alarm_text_time);
        alarmTime.setText(this.alarm.toString());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        Prefs.saveAlarm(getContext(), garbageType, this.alarm);

        setupAlarm();

        if (onDismissDialogFragmentListener != null) {
            onDismissDialogFragmentListener.onDismissDialog(true);
        }
    }

    private void setupAlarm() {
        // 設定を有効にする場合でもすでにONになっているかもしれないのでキャンセルする
        AlarmService.cancelAlarm(getContext(), garbageType);

        // アラームを設定する
        if (alarm.enable) {
            AlarmService.setupAlarm(getContext(), garbageType);
        }
    }
}
