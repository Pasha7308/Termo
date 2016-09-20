package com.pasha.termo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RemoteViews;

import com.pasha.termo.model.WeatherDto;
import com.pasha.termo.utils.Colorer;
import com.pasha.termo.utils.DateUtils;
import com.pasha.termo.utils.TempRounder;

import static android.support.v4.app.NotificationCompat.VISIBILITY_PUBLIC;

public class DownloadWebpageService extends AsyncTask<Object, Integer, WeatherDto>
{
    private static final String LOG = "DownloadWebpageService";
    private Intent intent;
    private Context context;
    private Class callerClass;
    private int wPx = 0;
    private int hPx = 0;

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
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        for (int widgetId : allWidgetIds) {

            boolean is1x1 = callerClass.getName().contains("WeatherAppWidgetProvider1x1");

            String tempTermo = TempRounder.round(dto.getServerTermo().getTemp(), is1x1, false);
            String tempIao = TempRounder.round(dto.getServerIao().getTemp(), is1x1, true);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), is1x1 ? R.layout.widget_1x1 : R.layout.widget_2x1);

            remoteViews.setTextViewText(R.id.lblWidgetText, tempTermo);
            remoteViews.setTextColor(R.id.lblWidgetText, Colorer.getColorOutOfTemp(tempTermo));

            remoteViews.setTextViewText(R.id.lblWidgetTor, tempIao);
            remoteViews.setTextColor(R.id.lblWidgetTor, Colorer.getColorOutOfTemp(tempIao));

            if (dto.getUpdated().GetDateTime() != null) {
                remoteViews.setTextViewText(R.id.lblWidgetTime, DateUtils.timeToString(dto.getUpdated().GetDateTime(), is1x1));
                remoteViews.setTextColor(R.id.lblWidgetTime, Color.WHITE);
            }

            getSizes(widgetId);
            Log.i(LOG, "W: " + wPx + ", H: " + hPx);
            Bitmap bm;
            if (wPx != 0 && hPx != 0) {
                bm = Bitmap.createBitmap(wPx, hPx, Bitmap.Config.ARGB_8888);
            } else {
                bm = Bitmap.createBitmap(is1x1 ? 192 : 192 * 2, 192, Bitmap.Config.ARGB_8888);
            }
            drawOnBitmap(bm, dto, true);
            remoteViews.setBitmap(R.id.imgvWidget, "setImageBitmap", bm);

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
        addNotification(dto);
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

    private void drawOnBitmap(Bitmap bm, WeatherDto dto, boolean isWidget) {
        Canvas canvas = new Canvas(bm);
        DrawView drawView = new DrawView(isWidget);
        drawView.draw(canvas, dto.getOldValues());
    }

    private void addNotification(WeatherDto dto) {
        Resources res = context.getResources();
        String caption = res.getString(R.string.strCaption);
        String tempTermo = TempRounder.round(dto.getServerTermo().getTemp(), false, false);
        String tempIao = TempRounder.round(dto.getServerIao().getTemp(), false, true);
        String text = String.format(res.getString(R.string.strNotificationTemplate), tempTermo, tempIao);
        String textTermo = String.format(res.getString(R.string.strNotificationTemplateTermo), tempTermo);
        String textIao = String.format(res.getString(R.string.strNotificationTemplateIao), tempIao);
        Bitmap bm = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
        drawOnBitmap(bm, dto, false);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(caption)
                        .setContentText(text)
                        .setLargeIcon(bm)
                        .setAutoCancel(false);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(caption);
        mBuilder.setStyle(inboxStyle);
        inboxStyle.addLine(textTermo);
        inboxStyle.addLine(textIao);

        mBuilder.setVisibility(VISIBILITY_PUBLIC);

// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);
// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        int notificationId = res.getInteger(R.integer.notificationId);
        mNotificationManager.notify(notificationId, mBuilder.build());
    }
}
