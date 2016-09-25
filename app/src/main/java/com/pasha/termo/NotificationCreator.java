package com.pasha.termo;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.pasha.termo.model.WeatherDto;
import com.pasha.termo.utils.Colorer;
import com.pasha.termo.utils.DateUtils;
import com.pasha.termo.utils.TempRounder;


class NotificationCreator {
    private Context context;

    NotificationCreator(Context context) {
        this.context = context;
    }

    void addNotification(WeatherDto dto) {
        Resources res = context.getResources();

        String tempTermo = TempRounder.round(dto.getServerTermo().getTemp(), false, false);
        String tempIao = TempRounder.round(dto.getServerIao().getTemp(), false, true);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.lblNotTextTermo, tempTermo);
        remoteViews.setTextColor(R.id.lblNotTextTermo, Colorer.getColorOutOfTemp(tempTermo));

        remoteViews.setTextViewText(R.id.lblNotTextIao, tempIao);
        remoteViews.setTextColor(R.id.lblNotTextIao, Colorer.getColorOutOfTemp(tempIao));

        remoteViews.setViewVisibility(R.id.lblNotTime, (dto.getUpdated().GetDateTime() != null) ? 1 : 0);
        if (dto.getUpdated().GetDateTime() != null) {
            remoteViews.setTextViewText(R.id.lblNotTime, DateUtils.timeToString(dto.getUpdated().GetDateTime(), false));
        }

        Bitmap bm = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
        DrawManager.drawOnBitmap(bm, dto, true);
        remoteViews.setBitmap(R.id.imgvNot, "setImageBitmap", bm);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_stat_notification)
                        .setContent(remoteViews)
                        .setAutoCancel(false);

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
        android.app.NotificationManager mNotificationManager =
                (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        int notificationId = res.getInteger(R.integer.notificationId);
        mNotificationManager.notify(notificationId, mBuilder.build());
    }
}
