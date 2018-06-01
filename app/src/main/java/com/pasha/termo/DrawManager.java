package com.pasha.termo;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.pasha.termo.model.ServerType;
import com.pasha.termo.model.WeatherDto;

class DrawManager {
    static void drawOnBitmap(Bitmap bm, WeatherDto dto, boolean isWidget, boolean isDark,
                             boolean isNotificationShowTermo, boolean isNotificationShowIao) {
        Canvas canvas = new Canvas(bm);
        DrawView drawView = new DrawView(isWidget, isDark);
        if (isNotificationShowTermo) {
            drawView.draw(canvas, dto.getOldValues(), ServerType.Termo);
        }
        if (isNotificationShowIao) {
            drawView.draw(canvas, dto.getOldValuesIao(), ServerType.Iao);
        }
    }
}
