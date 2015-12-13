package com.pasha.termo;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Arrays;

public class WeatherAppWidgetProvider extends AppWidgetProvider {

    public static String LOG = "WeatherAppWidgetProvider";

    @Override
    public void onUpdate(
        Context context,
        AppWidgetManager appWidgetManager,
        int[] appWidgetIds)
    {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.i(LOG, "onUpdate " + Arrays.toString(appWidgetIds));
        Log.i(LOG, "NAME " + this.getClass().getName());

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(),
                this.getClass().getName().contains("1x1") ? R.layout.widget_1x1 : R.layout.widget_2x1);
            views.setTextColor(R.id.lblWidgetTor, Color.GRAY);
            views.setTextColor(R.id.lblWidgetText, Color.GRAY);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        ComponentName thisWidget = new ComponentName(context, this.getClass());
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        Intent intent = new Intent(context.getApplicationContext(), this.getClass());
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
        new DownloadWebpageServiceTermo().execute(intent, context.getApplicationContext(), this.getClass());
        new DownloadWebpageServiceTor().execute(intent, context.getApplicationContext(), this.getClass());
    }

}