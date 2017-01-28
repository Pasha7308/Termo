package com.pasha.termo;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.pasha.termo.activities.SettingsActivity;
import com.pasha.termo.utils.Colorer;

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
            RemoteViews views = new RemoteViews(context.getPackageName(),
                this.getClass().getName().contains("1x1") ? R.layout.widget_1x1 : R.layout.widget_2x1);
            views.setTextColor(R.id.lblWidgetTor, Color.GRAY);
            views.setTextColor(R.id.lblWidgetText, Color.GRAY);
            setWidgetColors(R.id.lay1x1, context, views, true);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        ComponentName thisWidget = new ComponentName(context, this.getClass());
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        Intent intent = new Intent(context.getApplicationContext(), this.getClass());
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
        new DownloadWebpageService().execute(intent, context.getApplicationContext(), this.getClass());

    }

    static public void setWidgetColors(int id, Context context, RemoteViews views, boolean setColor) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String theme = sharedPref.getString(SettingsActivity.KEY_PREF_THEME, "0");
        int themeInt = Integer.parseInt(theme);
        int color = Colorer.getBackgoundColorFromTheme(themeInt);
        if (setColor) {
            views.setInt(id, "setBackgroundColor", color);
        } else {
            views.setInt(id, "setBackground", color);
        }
    }
}