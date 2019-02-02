package com.pasha.termo.utils;

import android.graphics.Color;

public class Colorer {

    static public int getColorOutOfTemp(
        String tempString,
        boolean isDark)
    {
        double temp;
        try {
            temp = Double.parseDouble(tempString);
        } catch (NullPointerException e) {
            temp = 0.0;
        }
        if (temp < -40) {
            return !isDark ? Color.rgb(153, 0, 153) : Color.rgb(255, 192, 255);
        } else if (temp < -30) {
            return !isDark ? Color.rgb(102, 0, 153) : Color.rgb(231, 192, 255);
        } else if (temp < -20) {
            return !isDark ? Color.rgb(0, 0, 179) : Color.rgb(192, 192, 255);
        } else if (temp < -10) {
            return !isDark ? Color.rgb(0, 102, 153) : Color.rgb(128, 191, 255);
        } else if (temp < 0) {
            return !isDark ? Color.rgb(0, 153, 153) : Color.rgb(128, 225, 255);
        } else if (temp < 10) {
            return !isDark ? Color.rgb(0, 153, 0) : Color.rgb(192, 255, 128);
        } else if (temp < 20) {
            return !isDark ? Color.rgb(153, 140, 0) : Color.rgb(255, 255, 128);
        } else if (temp < 30) {
            return !isDark ? Color.rgb(153, 115, 0) : Color.rgb(255, 228, 119);
        } else if (temp < 40) {
            return !isDark ? Color.rgb(153, 76, 0) : Color.rgb(255, 168, 128);
        } else {
            return !isDark ? Color.rgb(153, 0, 0) : Color.rgb(204, 0, 0);
        }
    }
    static public int getBackgoundColorFromTheme(
            int theme) {
        int color = 0;
        switch (theme) {
            case 1: // light
                color = Color.argb(64, 255, 255, 255);
                break;
            case 0: // dark
            default:
                color = Color.argb(255, 56, 66, 72);
                break;
        }
        return color;
    }
}
