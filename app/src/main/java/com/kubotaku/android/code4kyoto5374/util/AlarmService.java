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
package com.kubotaku.android.code4kyoto5374.util;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import com.kubotaku.android.code4kyoto5374.R;
import com.kubotaku.android.code4kyoto5374.data.Alarm;
import com.kubotaku.android.code4kyoto5374.data.AreaDays;
import com.kubotaku.android.code4kyoto5374.data.GarbageCollectDay;
import com.kubotaku.android.code4kyoto5374.data.GarbageDays;
import com.kubotaku.android.code4kyoto5374.data.GarbageType;
import com.kubotaku.android.code4kyoto5374.data.HomePlace;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * アラーム（Notification通知）サービスクラス
 */
public class AlarmService extends IntentService {

    /**
     * 通知アラームを設定する
     *
     * @param context     コンテキスト
     * @param garbageType ごみ種別
     */
    public static void setupAlarm(Context context, final int garbageType) {
        setupAlarm(context, garbageType, false);
    }

    /**
     * 通知アラームを設定する
     *
     * @param context     コンテキスト
     * @param garbageType ごみ種別
     * @param ignoreToday trueのとき、当日の発行を行わない
     */
    private static void setupAlarm(Context context, final int garbageType, final boolean ignoreToday) {

        final Intent intent = createIntent(context, garbageType);
        final PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        final Alarm alarm = Prefs.loadAlarm(context, garbageType);
        if (!alarm.enable) {
            return;
        }

        final HomePlace homePlace = Prefs.loadHomePlace(context);
        final int masterID = homePlace.areaMasterID;
        final String areaName = homePlace.areaName;

        Realm realm = Realm.getDefaultInstance();
        final RealmResults<AreaDays> results = realm.where(AreaDays.class)
                .equalTo("masterAreaID", masterID)
                .equalTo("areaName", areaName).findAll();

        if (results.size() != 1) {
            realm.close();
            return; // error
        }

        final AreaDays areaDays = results.first();
        final List<GarbageCollectDay.GarbageDaysForViews> garbageDays
                = GarbageCollectDay.GarbageDaysForViews.newList(areaDays.getTargetGarbageDays(garbageType));
        if (garbageDays == null) {
            realm.close();
            return; // error
        }

        final int nextDay = AppUtil.calcNearestDaysAfter(garbageDays, alarm.hour, alarm.minute, ignoreToday);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DATE, nextDay);
        calendar.set(Calendar.HOUR_OF_DAY, alarm.hour);
        calendar.set(Calendar.MINUTE, alarm.minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final String time = sdf.format(calendar.getTime());

        final long triggerMillis = calendar.getTimeInMillis();
        setupAlarm(context, pendingIntent, triggerMillis);

        realm.close();
    }

    private static void setupAlarm(Context context, PendingIntent intent, final long triggerAtMillis) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setupAlarmNew(context, intent, triggerAtMillis);
        } else {
            setupAlarmOld(context, intent, triggerAtMillis);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void setupAlarmNew(Context context, PendingIntent intent, final long triggerAtMillis) {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC, triggerAtMillis, intent);
    }

    private static void setupAlarmOld(Context context, PendingIntent intent, final long triggerAtMillis) {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, triggerAtMillis, intent);
    }

    /**
     * アラーム通知をキャンセルする
     *
     * @param context     コンテキスト
     * @param garbageType ごみ種別
     */
    public static void cancelAlarm(Context context, final int garbageType) {
        final Intent intent = createIntent(context, garbageType);
        final PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private static final String PARAM_GARBAGE_TYPE = "com.kubotaku.android.code4kyoto5374.util.extra.PARAM_GARBAGE_TYPE";

    public AlarmService() {
        super("AlarmService");
    }

    /**
     * サービス起動インテントを生成する
     *
     * @see IntentService
     */
    public static Intent createIntent(Context context, final int garbageType) {
        Intent intent = new Intent(context, AlarmService.class);
        intent.putExtra(PARAM_GARBAGE_TYPE, garbageType);
        return intent;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final int garbageType = intent.getIntExtra(PARAM_GARBAGE_TYPE, GarbageType.TYPE_NO);
            handleActionNotify(garbageType);
        }
    }

    private static final int NOTIFY_ID = 1;

    private void handleActionNotify(final int garbageType) {
        String garbageText = "";
        switch (garbageType) {
            case GarbageType.TYPE_BURNABLE:
            case GarbageType.TYPE_BIN:
            case GarbageType.TYPE_PLASTIC:
            case GarbageType.TYPE_SMALL:
                garbageText = GarbageType.getViewText(garbageType);
                break;

            default:
                return; // 対応外の種別なので何もしない
        }

        final Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(garbageText + "の収集日です")
                .setContentText("朝8:00までに所定の場所に出しましょう")
                .build();

        NotificationManagerCompat.from(this).notify(NOTIFY_ID, notification);

        // 次のアラームを設定
        setupAlarm(this, garbageType, true);
    }
}
