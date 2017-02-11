package com.pasha.termo.widgetProviders;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.pasha.termo.DownloadWebpageService;
import com.pasha.termo.R;
import com.pasha.termo.activities.SettingsActivity;
import com.pasha.termo.utils.Colorer;
import com.pasha.termo.widgetProviders.enums.WidgetType;

public class WeatherAppWidgetProvider extends AppWidgetProvider {

    public static String LOG = "WeatherAppWidgetProvider";

    @Override
    public void onUpdate(
        Context context,
        AppWidgetManager appWidgetManager,
        int[] appWidgetIds)
    {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

//        Log.i(LOG, "onUpdate " + Arrays.toString(appWidgetIds);
//        Log.i(LOG, "NAME " + this.getClass().getName());

        for (int appWidgetId : appWidgetIds) {
            WidgetType widgetType = WidgetType.fromString(this.getClass().getName());
            RemoteViews views = new RemoteViews(context.getPackageName(), widgetType.getLayoutId());
            views.setTextColor(R.id.lblWidgetText, Color.GRAY);
            if (widgetType.isSecontTempVisible()) {
                views.setTextColor(R.id.lblWidgetTor, Color.GRAY);
            }
            setWidgetColors(widgetType.getViewId(), context, views);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        ComponentName thisWidget = new ComponentName(context, this.getClass());
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        Intent intent = new Intent(context.getApplicationContext(), this.getClass());
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
        new DownloadWebpageService().execute(intent, context.getApplicationContext(), this.getClass());

    }

    private void setWidgetColors(int id, Context context, RemoteViews views) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String theme = sharedPref.getString(SettingsActivity.KEY_PREF_THEME_WIDGET, "0");
        int themeInt = Integer.parseInt(theme);
        int color = Colorer.getBackgoundColorFromTheme(themeInt);
        views.setInt(id, "setBackgroundColor", color);
    }
}