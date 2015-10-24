package com.pasha.termo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DownloadWebpageServiceText extends AsyncTask<Object, Integer, String>
{

	protected Intent intent;
    protected Context context;
    protected Class callerClass;

    protected String doInBackground(
        Object... objs)
    {
        intent = (Intent)objs[0];
        context = (Context)objs[1];
        callerClass = (Class)objs[2];
        TextDownloader textDownloader = new TextDownloader();
        return textDownloader.downloadUrl(DownloadWebpageSource.Termo);
    }

    protected void onProgressUpdate(
        Integer... progress)
    {
    }

    protected void onPostExecute(
        String currentTemp)
    {
//        currentTemp = String.valueOf(Calendar.getInstance().get(Calendar.SECOND));
        SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss");
        String currentTime = sdf.format(Calendar.getInstance().getTime());

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        for (int widgetId : allWidgetIds) {

            boolean is1x1 = callerClass.getName().contains("WeatherAppWidgetProvider1x1");
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), is1x1 ? R.layout.widget_1x1 : R.layout.widget_2x1);
            remoteViews.setTextViewText(R.id.lblWidgetText, currentTemp);
            remoteViews.setTextColor(R.id.lblWidgetText, Colorer.getColorOutOfTemp(currentTemp));
            remoteViews.setTextViewText(R.id.lblWidgetTime, currentTime);

            // Register an onClickListener
            Intent clickIntent = new Intent(context, callerClass);

            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

            PendingIntent pendingIntentUpdate = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.btnWidRefresh, pendingIntentUpdate);

            Intent showIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntentShow = PendingIntent.getActivity(context, 0, showIntent, 0);
            remoteViews.setOnClickPendingIntent(is1x1 ? R.id.lay1x1 : R.id.lay2x1, pendingIntentShow);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

}
