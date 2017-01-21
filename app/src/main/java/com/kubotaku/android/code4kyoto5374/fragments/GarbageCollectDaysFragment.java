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


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kubotaku.android.code4kyoto5374.R;
import com.kubotaku.android.code4kyoto5374.SegregationActivity;
import com.kubotaku.android.code4kyoto5374.data.Alarm;
import com.kubotaku.android.code4kyoto5374.data.AreaDays;
import com.kubotaku.android.code4kyoto5374.data.GarbageCollectDay;
import com.kubotaku.android.code4kyoto5374.data.GarbageDays;
import com.kubotaku.android.code4kyoto5374.data.GarbageType;
import com.kubotaku.android.code4kyoto5374.data.HomePlace;
import com.kubotaku.android.code4kyoto5374.util.AppUtil;
import com.kubotaku.android.code4kyoto5374.util.Prefs;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * ごみ収集日表示画面Fragment
 */
public class GarbageCollectDaysFragment extends Fragment
        implements OnDismissDialogFragmentListener {

    public static final String TAG = GarbageCollectDaysFragment.class.getSimpleName();

    public static GarbageCollectDaysFragment newInstance() {
        final GarbageCollectDaysFragment fragment = new GarbageCollectDaysFragment();

        final Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }


    public GarbageCollectDaysFragment() {
        // Required empty public constructor
    }

    private Realm realm;

    private GarbageCollectDaysAdapter garbageCollectDaysAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_garbage_collect_days, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        realm = Realm.getDefaultInstance();
        loadAreaDays();
        setupViews();
    }

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
    }

    private void loadAreaDays() {

        garbageCollectDayList.clear();

        final Context context = getContext();
        final HomePlace homePlace = Prefs.loadHomePlace(context);

        final int masterID = homePlace.areaMasterID;
        final String areaName = homePlace.areaName;

        final RealmResults<AreaDays> results = realm.where(AreaDays.class)
                .equalTo("masterAreaID", masterID)
                .equalTo("areaName", areaName).findAll();

        if (results.size() == 1) {
            final AreaDays areaDays = results.first();

            getActivity().setTitle(areaDays.areaName);

            final GarbageCollectDay burnable = new GarbageCollectDay();
            burnable.type = GarbageType.TYPE_BURNABLE;
            burnable.days = GarbageCollectDay.GarbageDaysForViews.newList(areaDays.burnableDays);
            burnable.alarm = Prefs.loadAlarm(context, GarbageType.TYPE_BURNABLE);
            garbageCollectDayList.add(burnable);

            final GarbageCollectDay plastic = new GarbageCollectDay();
            plastic.type = GarbageType.TYPE_PLASTIC;
            plastic.days = GarbageCollectDay.GarbageDaysForViews.newList(areaDays.plasticDays);
            plastic.alarm = Prefs.loadAlarm(context, GarbageType.TYPE_PLASTIC);
            garbageCollectDayList.add(plastic);

            final GarbageCollectDay bin = new GarbageCollectDay();
            bin.type = GarbageType.TYPE_BIN;
            bin.days = GarbageCollectDay.GarbageDaysForViews.newList(areaDays.binDays);
            bin.alarm = Prefs.loadAlarm(context, GarbageType.TYPE_BIN);
            garbageCollectDayList.add(bin);

            final GarbageCollectDay small = new GarbageCollectDay();
            small.type = GarbageType.TYPE_SMALL;
            small.days = GarbageCollectDay.GarbageDaysForViews.newList(areaDays.smallDays);
            small.alarm = Prefs.loadAlarm(context, GarbageType.TYPE_SMALL);
            garbageCollectDayList.add(small);
        }
    }

    private void setupViews() {
        final View view = getView();

        final ListView listView = (ListView) view.findViewById(R.id.list_collect_days);
        garbageCollectDaysAdapter = new GarbageCollectDaysAdapter();
        listView.setAdapter(garbageCollectDaysAdapter);
        listView.setOnItemSelectedListener(onItemSelectedListener);
        listView.setOnItemClickListener(onItemClickListener);

        Button btnSearch = (Button) view.findViewById(R.id.collect_btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SegregationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDismissDialog(boolean needUpdate) {
        if (needUpdate) {
            loadAreaDays();

            if (garbageCollectDaysAdapter != null) {
                garbageCollectDaysAdapter.notifyDataSetChanged();
            }
        }
    }

    // -----------------------------

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            final GarbageCollectDay item = (GarbageCollectDay) adapterView.getAdapter().getItem(i);
            final NotifySettingsDialogFragment dialogFragment = NotifySettingsDialogFragment.newInstance(item.type);
            dialogFragment.setTargetFragment(GarbageCollectDaysFragment.this, 0);
            dialogFragment.show(getFragmentManager(), null);
        }
    };

    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };

    private List<GarbageCollectDay> garbageCollectDayList = new ArrayList<>();

    private static final class ViewHolder {
        TextView daysAfter;
        TextView type;
        TextView collectDays;
        TextView alarm;
        ImageView clock;
    }

    private class GarbageCollectDaysAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return garbageCollectDayList.size();
        }

        @Override
        public GarbageCollectDay getItem(int position) {
            if (garbageCollectDayList.size() > position) {
                return garbageCollectDayList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                final LayoutInflater inflater = getActivity().getLayoutInflater();
                final View view = inflater.inflate(R.layout.garbage_area_days_view, parent, false);

                holder.daysAfter = (TextView) view.findViewById(R.id.text_days_after);
                holder.type = (TextView) view.findViewById(R.id.text_garbage_type);
                holder.collectDays = (TextView) view.findViewById(R.id.text_collect_days);
                holder.alarm = (TextView) view.findViewById(R.id.text_alarm_settings);
                holder.clock = (ImageView) view.findViewById(R.id.image_alarm_clock);

                convertView = view;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final GarbageCollectDay item = getItem(position);
            if (item != null) {

                Context context = getContext();

                holder.type.setText(GarbageType.getViewText(item.type));
                int textColor = GarbageType.getViewTextColor(context, item.type);
                holder.type.setTextColor(textColor);

                final int viewColor = GarbageType.getViewColor(context, item.type);
                convertView.setBackgroundColor(viewColor);

                final int nearestDaysAfter = AppUtil.calcNearestDaysAfter(item.days, 8, 0, false);
                holder.daysAfter.setText(AppUtil.convertDaysAfterText(nearestDaysAfter));
                holder.daysAfter.setTextColor(textColor);

                holder.collectDays.setText(AppUtil.createGarbageCollectDaysText(item.days, nearestDaysAfter));
                holder.collectDays.setTextColor(textColor);

                holder.alarm.setTextColor(textColor);
                if (item.alarm != null && item.alarm.enable) {
                    holder.alarm.setText(item.alarm.toString());
                } else {
                    holder.alarm.setText("設定なし");
                }

                Drawable clock = GarbageType.getAlarmClockDrawable(context, item.type);
                holder.clock.setImageDrawable(clock);
            }

            return convertView;
        }
    }
}
