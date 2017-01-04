package com.kubotaku.android.code4kyoto5374;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.kubotaku.android.code4kyoto5374.fragments.SegregationFragment;

/**
 * ごみの分別判定用Activity
 */
public class SegregationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segragation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupViews();
    }

    private void setupViews() {
        showSegregationView();
    }

    private void showSegregationView() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(SegregationFragment.TAG) == null) {
            SegregationFragment fragment = SegregationFragment.newInstance();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.view_holder, fragment, SegregationFragment.TAG);
            ft.commitAllowingStateLoss();
        }
    }
}
