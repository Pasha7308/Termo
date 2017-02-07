package com.pasha.termo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RemoteViews;

import com.pasha.termo.activities.MainActivity;
import com.pasha.termo.activities.SettingsActivity;
import com.pasha.termo.model.WeatherDto;
import com.pasha.termo.utils.Colorer;
import com.pasha.termo.utils.DateUtils;
import com.pasha.termo.utils.TempRounder;

public class DownloadWebpageService extends AsyncTask<Object, Integer, WeatherDto>
{
    private static final String LOG = "DownloadWebpageService";
    private Intent intent;
    private Context context;
    private Class callerClass;
    private int wPx = 0;
    private int hPx = 0;
    private NotificationCreator notificationCreator;

    protected WeatherDto doInBackground(
        Object... objs)
    {
        intent = (Intent)objs[0];
        context = (Context)objs[1];
        callerClass = (Class)objs[2];
        notificationCreator = new NotificationCreator(context);
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
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        for (int widgetId : allWidgetIds) {

            boolean isComplex = callerClass.getName().contains("WeatherAppWidgetProviderComplex");

            RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(), isComplex ? R.layout.widget_complex : R.layout.widget_simple);

            String tempTermo = TempRounder.round(dto.getServerTermo().getTemp(), true, false);
            remoteViews.setTextViewText(R.id.lblWidgetText, tempTermo);
            remoteViews.setTextColor(R.id.lblWidgetText, Colorer.getColorOutOfTemp(tempTermo));

            if (isComplex) {
                String tempIao = TempRounder.round(dto.getServerIao().getTemp(), true, true);
                remoteViews.setTextViewText(R.id.lblWidgetTor, tempIao);
                remoteViews.setTextColor(R.id.lblWidgetTor, Colorer.getColorOutOfTemp(tempIao));
            }

            if (dto.getUpdated().GetDateTime() != null) {
                remoteViews.setTextViewText(R.id.lblWidgetTime, DateUtils.timeToString(dto.getUpdated().GetDateTime(), isComplex));
                remoteViews.setTextColor(R.id.lblWidgetTime, Color.WHITE);
            }

            getSizes(widgetId);
            Log.i(LOG, "W: " + wPx + ", H: " + hPx);
            Bitmap bm;
            if (wPx != 0 && hPx != 0) {
                bm = Bitmap.createBitmap(wPx, hPx, Bitmap.Config.ARGB_8888);
            } else {
                bm = Bitmap.createBitmap(192, 192, Bitmap.Config.ARGB_8888);
            }
            DrawManager.drawOnBitmap(bm, dto, true);
            remoteViews.setBitmap(R.id.imgvWidget, "setImageBitmap", bm);

            // Register an onClickListener
            Intent clickIntent = new Intent(context, callerClass);

            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

            PendingIntent pendingIntentUpdate = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.btnWidRefresh, pendingIntentUpdate);

            Intent showIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntentShow = PendingIntent.getActivity(context, 0, showIntent, 0);
            remoteViews.setOnClickPendingIntent(isComplex ? R.id.layComplex : R.id.laySimple, pendingIntentShow);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
        if (isAddNotofication()) {
            notificationCreator.addNotification(dto);
        }
    }

    private void getSizes(int appWidgetId)
    {
        AppWidgetProviderInfo providerInfo = AppWidgetManager.getInstance(context).getAppWidgetInfo(appWidgetId);

        int w_dp = providerInfo.minWidth;
        int h_dp = providerInfo.minHeight;

        wPx = dpToPx(w_dp, context);
        hPx = dpToPx(h_dp, context);
    }

    private static int dpToPx(int dp, Context context)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * displayMetrics.density);
    }

    private boolean isAddNotofication() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String add = sharedPref.getString(SettingsActivity.KEY_PREF_IS_NOTIFICATION, "1");
        int addInt = Integer.parseInt(add);
        return addInt == 1;
    }
}
