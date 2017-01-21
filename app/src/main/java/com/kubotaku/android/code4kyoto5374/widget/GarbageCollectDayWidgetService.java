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
package com.kubotaku.android.code4kyoto5374.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService
        ;

import com.kubotaku.android.code4kyoto5374.R;
import com.kubotaku.android.code4kyoto5374.data.AreaDays;
import com.kubotaku.android.code4kyoto5374.data.GarbageCollectDay;
import com.kubotaku.android.code4kyoto5374.data.GarbageType;
import com.kubotaku.android.code4kyoto5374.data.HomePlace;
import com.kubotaku.android.code4kyoto5374.util.AppUtil;
import com.kubotaku.android.code4kyoto5374.util.Prefs;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * ゴミ収集日表示ウィジェット用表示処理サービス
 */

public class GarbageCollectDayWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GarbageCollectDayWidgetViewsFactory(getApplicationContext());
    }

    private static class GarbageCollectDayWidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context context;

        private List<GarbageCollectDay> garbageCollectDayList;

        public GarbageCollectDayWidgetViewsFactory(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            loadGarbageCollectDay();
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            if (garbageCollectDayList == null) {
                return 0;
            }
            return garbageCollectDayList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            GarbageCollectDay item = getItem(position);
            if (item == null) {
                return null;
            }

            RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.garbage_collect_day_view);

            Intent intent = new Intent();
            view.setOnClickFillInIntent(R.id.root_view, intent);

            view.setTextViewText(R.id.text_garbage_type, GarbageType.getViewText(item.type));
            int textColor = GarbageType.getViewTextColor(context, item.type);
            view.setTextColor(R.id.text_garbage_type, textColor);

            final int viewColor = GarbageType.getViewColor(context, item.type);
            view.setInt(R.id.root_view, "setBackgroundColor", viewColor);

            final int nearestDaysAfter = AppUtil.calcNearestDaysAfter(item.days, 8, 0, false);
            view.setTextViewText(R.id.text_days_after, AppUtil.convertDaysAfterText(nearestDaysAfter));
            view.setTextColor(R.id.text_days_after, textColor);

            view.setTextViewText(R.id.text_collect_days, AppUtil.createGarbageCollectDaysText(item.days, nearestDaysAfter));
            view.setTextColor(R.id.text_collect_days, textColor);

            return view;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        private GarbageCollectDay getItem(int position) {
            if (garbageCollectDayList == null) {
                return null;
            }

            if (garbageCollectDayList.size() > position) {
                return garbageCollectDayList.get(position);
            }
            return null;
        }

        private void loadGarbageCollectDay() {
            if (garbageCollectDayList == null) {
                garbageCollectDayList = new ArrayList<>();
            }
            garbageCollectDayList.clear();

            final HomePlace homePlace = Prefs.loadHomePlace(context);

            final int masterID = homePlace.areaMasterID;
            final String areaName = homePlace.areaName;

            Realm realm = Realm.getDefaultInstance();
            final RealmResults<AreaDays> results = realm.where(AreaDays.class)
                    .equalTo("masterAreaID", masterID)
                    .equalTo("areaName", areaName).findAll();

            if (results.size() == 1) {
                final AreaDays areaDays = results.first();

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

            realm.close();
        }
    }

}

