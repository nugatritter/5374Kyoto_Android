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

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.kubotaku.android.code4kyoto5374.MainActivity;
import com.kubotaku.android.code4kyoto5374.R;

/**
 * ゴミ収集日表示ウィジェットプロバイダー
 */
public class GarbageCollectDayWidget extends AppWidgetProvider {

    public static final String TAG = GarbageCollectDayWidget.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Set up the intent that starts the StackViewService, which will
        // provide the views for this collection.
        Intent intent = new Intent(context, GarbageCollectDayWidgetService.class);

        // Add the app widget ID to the intent extras.
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        // Instantiate the RemoteViews object for the app widget layout.
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.garbage_collect_day_widget);

        // Set up the RemoteViews object to use a RemoteViews adapter.
        // This adapter connects
        // to a RemoteViewsService  through the specified intent.
        // This is how you populate the data.
        rv.setRemoteAdapter(R.id.list_collect_days, intent);

        Intent intentActivityStart = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentActivityStart, 0);
        rv.setPendingIntentTemplate(R.id.list_collect_days, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, rv);

        // update list
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_collect_days);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

