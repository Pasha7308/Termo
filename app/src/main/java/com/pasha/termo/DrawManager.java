package com.pasha.termo;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.pasha.termo.model.WeatherDto;

class DrawManager {
    static void drawOnBitmap(Bitmap bm, WeatherDto dto, boolean isWidget, boolean isDark) {
        Canvas canvas = new Canvas(bm);
        DrawView drawView = new DrawView(isWidget, isDark);
        drawView.draw(canvas, dto.getOldValues());
    }
}
