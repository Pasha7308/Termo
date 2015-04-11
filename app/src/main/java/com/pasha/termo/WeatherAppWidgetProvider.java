package com.pasha.termo;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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

        final int N = appWidgetIds.length;

        String termo = "UPD";

        for (int i = 0; i < N; i++) {
            RemoteViews views = new RemoteViews(context.getPackageName(),
                this.getClass().getName().contains("1x1") ? R.layout.widget_1x1 : R.layout.widget_2x1);
            views.setTextViewText(R.id.lblWidgetText, termo);
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }

        ComponentName thisWidget = new ComponentName(context, this.getClass());
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        Intent intent = new Intent(context.getApplicationContext(), this.getClass());
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
        new DownloadWebpageServiceText().execute(intent, context.getApplicationContext(), this.getClass());

    }

}