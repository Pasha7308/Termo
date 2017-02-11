package com.pasha.termo;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.pasha.termo.activities.MainActivity;
import com.pasha.termo.model.WeatherDto;
import com.pasha.termo.utils.Colorer;
import com.pasha.termo.utils.DateUtils;
import com.pasha.termo.utils.TempRounder;


class NotificationCreator {
    private Context context;

    NotificationCreator(Context context) {
        this.context = context;
    }

    void addNotification(WeatherDto dto, boolean isDarkNotification) {
        Resources res = context.getResources();

        String tempTermo = TempRounder.round(dto.getServerTermo().getTemp(), false, false);
        String tempIao = TempRounder.round(dto.getServerIao().getTemp(), false, true);
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(), isDarkNotification ? R.layout.notification_dark : R.layout.notification_light);
        remoteViews.setTextViewText(R.id.lblNotTextTermo, tempTermo);
        remoteViews.setTextColor(R.id.lblNotTextTermo, Colorer.getColorOutOfTemp(tempTermo, isDarkNotification));

        remoteViews.setTextViewText(R.id.lblNotTextIao, tempIao);
        remoteViews.setTextColor(R.id.lblNotTextIao, Colorer.getColorOutOfTemp(tempIao, isDarkNotification));

        remoteViews.setViewVisibility(R.id.lblNotTime, (dto.getUpdated().GetDateTime() != null) ? 1 : 0);
        if (dto.getUpdated().GetDateTime() != null) {
            remoteViews.setTextViewText(R.id.lblNotTime, DateUtils.timeToString(dto.getUpdated().GetDateTime(), false));
            remoteViews.setTextColor(R.id.lblNotTime, isDarkNotification ? Color.WHITE : Color.BLACK);
        }

        Bitmap bm = Bitmap.createBitmap(512, 256, Bitmap.Config.ARGB_8888);
        DrawManager.drawOnBitmap(bm, dto, false, isDarkNotification);
        remoteViews.setBitmap(R.id.imgvNot, "setImageBitmap", bm);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_stat_notification)
                        .setContent(remoteViews)
                        .setAutoCancel(false);

        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        android.app.NotificationManager mNotificationManager =
                (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = res.getInteger(R.integer.notificationId);
        mNotificationManager.notify(notificationId, mBuilder.build());
    }
}
