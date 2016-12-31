package com.kubotaku.android.code4kyoto5374.fragments;

/**
 * Interface for DateTimeDialogFragment dismiss event.
 */

public interface OnDismissDateTimePickerDialogListener {

    void onDismissDialog(final int year, final int month, final int day, final int hour, final int minute);

}
