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
    final String channelId = "Chanel1";

    NotificationCreator(Context context) {
        this.context = context;
    }

    void addNotification(WeatherDto dto, boolean isDarkNotification,
            boolean isDigitsInNotification, boolean isNotificationGraphBold) {
        Resources res = context.getResources();

        int temp = dto.getServerTermo().getTemp();
        String tempTermo = TempRounder.round(temp, false, false);
        String tempIao = TempRounder.round(dto.getServerIao().getTemp(), false, false);
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(), isDarkNotification ? R.layout.notification_dark : R.layout.notification_light);
        remoteViews.setTextViewText(R.id.lblNotTextTermo, tempTermo);
        remoteViews.setTextColor(R.id.lblNotTextTermo, Colorer.getColorOutOfTemp(tempTermo, isDarkNotification));

        remoteViews.setTextViewText(R.id.lblNotTextIao, tempIao);
        remoteViews.setTextColor(R.id.lblNotTextIao, Colorer.getColorOutOfTemp(tempIao, isDarkNotification));

        remoteViews.setViewVisibility(R.id.lblNotTime, (dto.getUpdated().GetDateTime() != null) ? 1 : 0);
        if (dto.getUpdated().GetDateTime() != null) {
            remoteViews.setTextViewText(R.id.lblNotTime, DateUtils.timeToString(dto.getUpdated().GetDateTime(), true));
            remoteViews.setTextColor(R.id.lblNotTime, isDarkNotification ? Color.WHITE : Color.BLACK);
        }
        remoteViews.setTextColor(R.id.lblNotTermo, isDarkNotification ? Color.WHITE : Color.BLACK);
        remoteViews.setTextColor(R.id.lblNotIao, isDarkNotification ? Color.WHITE : Color.BLACK);

        Bitmap bm = Bitmap.createBitmap(512, 256, Bitmap.Config.ARGB_8888);
        DrawManager.drawOnBitmap(bm, dto, isNotificationGraphBold, isDarkNotification);
        remoteViews.setBitmap(R.id.imgvNot, "setImageBitmap", bm);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(isDigitsInNotification ? getIcon(temp) : R.drawable.ic_stat_notification)
//                        .setSmallIcon(Icon.createWithBitmap(bm))
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

    private int getIcon(Integer temp) {
        int resource = R.drawable.ic_stat_notification;
        if (temp == null) {
            return resource;
        } else if (temp >= 0 && temp <= 5) {
            resource = R.drawable.ic_00p;
        } else if (temp < 0 && temp >= -5) {
            resource = R.drawable.ic_00m;
        } else if (temp > 310) {
            resource = R.drawable.ic_30pp;
        } else if (temp < -310) {
            resource = R.drawable.ic_30mm;
        } else {
            double tempDouble = (double)temp / 10;
            temp = (int)Math.round(tempDouble);
            switch (temp) {
                case -30: resource = R.drawable.ic_30m;
                    break;
                case -20: resource = R.drawable.ic_20m;
                    break;
                case -21: resource = R.drawable.ic_21m;
                    break;
                case -22: resource = R.drawable.ic_22m;
                    break;
                case -23: resource = R.drawable.ic_23m;
                    break;
                case -24: resource = R.drawable.ic_24m;
                    break;
                case -25: resource = R.drawable.ic_25m;
                    break;
                case -26: resource = R.drawable.ic_26m;
                    break;
                case -27: resource = R.drawable.ic_27m;
                    break;
                case -28: resource = R.drawable.ic_28m;
                    break;
                case -29: resource = R.drawable.ic_29m;
                    break;
                case -10: resource = R.drawable.ic_10m;
                    break;
                case -11: resource = R.drawable.ic_11m;
                    break;
                case -12: resource = R.drawable.ic_12m;
                    break;
                case -13: resource = R.drawable.ic_13m;
                    break;
                case -14: resource = R.drawable.ic_14m;
                    break;
                case -15: resource = R.drawable.ic_15m;
                    break;
                case -16: resource = R.drawable.ic_16m;
                    break;
                case -17: resource = R.drawable.ic_17m;
                    break;
                case -18: resource = R.drawable.ic_18m;
                    break;
                case -19: resource = R.drawable.ic_19m;
                    break;
                case -1: resource = R.drawable.ic_01m;
                    break;
                case -2: resource = R.drawable.ic_02m;
                    break;
                case -3: resource = R.drawable.ic_03m;
                    break;
                case -4: resource = R.drawable.ic_04m;
                    break;
                case -5: resource = R.drawable.ic_05m;
                    break;
                case -6: resource = R.drawable.ic_06m;
                    break;
                case -7: resource = R.drawable.ic_07m;
                    break;
                case -8: resource = R.drawable.ic_08m;
                    break;
                case -9: resource = R.drawable.ic_09m;
                    break;
                case 1: resource = R.drawable.ic_01p;
                    break;
                case 2: resource = R.drawable.ic_02p;
                    break;
                case 3: resource = R.drawable.ic_03p;
                    break;
                case 4: resource = R.drawable.ic_04p;
                    break;
                case 5: resource = R.drawable.ic_05p;
                    break;
                case 6: resource = R.drawable.ic_06p;
                    break;
                case 7: resource = R.drawable.ic_07p;
                    break;
                case 8: resource = R.drawable.ic_08p;
                    break;
                case 9: resource = R.drawable.ic_09p;
                    break;
                case 10: resource = R.drawable.ic_10p;
                    break;
                case 11: resource = R.drawable.ic_11p;
                    break;
                case 12: resource = R.drawable.ic_12p;
                    break;
                case 13: resource = R.drawable.ic_13p;
                    break;
                case 14: resource = R.drawable.ic_14p;
                    break;
                case 15: resource = R.drawable.ic_15p;
                    break;
                case 16: resource = R.drawable.ic_16p;
                    break;
                case 17: resource = R.drawable.ic_17p;
                    break;
                case 18: resource = R.drawable.ic_18p;
                    break;
                case 19: resource = R.drawable.ic_19p;
                    break;
                case 20: resource = R.drawable.ic_20p;
                    break;
                case 21: resource = R.drawable.ic_21p;
                    break;
                case 22: resource = R.drawable.ic_22p;
                    break;
                case 23: resource = R.drawable.ic_23p;
                    break;
                case 24: resource = R.drawable.ic_24p;
                    break;
                case 25: resource = R.drawable.ic_25p;
                    break;
                case 26: resource = R.drawable.ic_26p;
                    break;
                case 27: resource = R.drawable.ic_27p;
                    break;
                case 28: resource = R.drawable.ic_28p;
                    break;
                case 29: resource = R.drawable.ic_29p;
                    break;
                case 30: resource = R.drawable.ic_30p;
                    break;
            }
        }
        return resource;
    }
}
