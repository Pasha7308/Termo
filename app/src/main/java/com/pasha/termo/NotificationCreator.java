package com.pasha.termo;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.pasha.termo.activities.MainActivity;
import com.pasha.termo.activities.SettingsActivity;
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
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(), isDarkTheme() ? R.layout.notificationdark : R.layout.notificationlight);
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
    private boolean isDarkTheme() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String theme = sharedPref.getString(SettingsActivity.KEY_PREF_THEME_NOTIFICATION, "0");
        int themeInt = Integer.parseInt(theme);
        return themeInt == 0;
    }
}
