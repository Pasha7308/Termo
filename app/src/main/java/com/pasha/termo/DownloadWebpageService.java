package com.pasha.termo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import com.pasha.termo.model.WeatherDto;
import com.pasha.termo.utils.Colorer;
import com.pasha.termo.utils.DateUtils;
import com.pasha.termo.utils.TempRounder;

public class DownloadWebpageService extends AsyncTask<Object, Integer, WeatherDto>
{
    protected Intent intent;
    protected Context context;
    protected Class callerClass;

    protected WeatherDto doInBackground(
        Object... objs)
    {
        intent = (Intent)objs[0];
        context = (Context)objs[1];
        callerClass = (Class)objs[2];
        TextDownloader textDownloader = new TextDownloader();
        return textDownloader.downloadUrl();
    }

    protected void onProgressUpdate(
            Integer... progress)
    {
    }

    protected void onPostExecute(
        WeatherDto dto)
    {
//        currentTemp = "-24.5";
        if (dto == null) {
            return;
        }
        String tempTermo = TempRounder.round(dto.getServerTermo().getTemp());
        String tempIao = TempRounder.round(dto.getServerIao().getTemp());

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        for (int widgetId : allWidgetIds) {

            boolean is1x1 = callerClass.getName().contains("WeatherAppWidgetProvider1x1");

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), is1x1 ? R.layout.widget_1x1 : R.layout.widget_2x1);

            remoteViews.setTextViewText(R.id.lblWidgetText, tempTermo);
            remoteViews.setTextColor(R.id.lblWidgetText, Colorer.getColorOutOfTemp(tempTermo));

            remoteViews.setTextViewText(R.id.lblWidgetTor, tempIao);
            remoteViews.setTextColor(R.id.lblWidgetTor, Colorer.getColorOutOfTemp(tempIao));

            if (dto.getUpdated().GetDateTime() != null) {
                remoteViews.setTextViewText(R.id.lblWidgetTime, DateUtils.timeToString(dto.getUpdated().GetDateTime(), is1x1));
            }

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
