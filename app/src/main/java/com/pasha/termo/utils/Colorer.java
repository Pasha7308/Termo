package com.pasha.termo.utils;

import android.graphics.Color;

public class Colorer {

    static public int getColorOutOfTemp(
        String tempString)
    {
        Double temp;
        try {
            temp = Double.parseDouble(tempString);
        } catch (NullPointerException e) {
            temp = 0.0;
        }
        if (temp < -40) {
            return Color.rgb(255, 192, 255);
        } else if (temp < -30) {
            return Color.rgb(231, 192, 255);
        } else if (temp < -20) {
            return Color.rgb(192, 192, 255);
        } else if (temp < -10) {
            return Color.rgb(128, 191, 255);
        } else if (temp < 0) {
            return Color.rgb(128, 225, 255);
        } else if (temp < 10) {
            return Color.rgb(192, 255, 128);
        } else if (temp < 20) {
            return Color.rgb(255, 255, 128);
        } else if (temp < 30) {
            return Color.rgb(255, 228, 119);
        } else if (temp < 40) {
            return Color.rgb(255, 168, 128);
        } else {
            return Color.rgb(204, 0, 0);
        }
    }
    static public int getBackgoundColorFromTheme(
            int theme) {
        int color = 0;
        switch (theme) {
            case 1: // light
                color = Color.argb(128, 128, 128, 128);
                break;
            case 0: // dark
            default:
                color = Color.argb(255, 56, 66, 72);
                break;
        }
        return color;
    }
}
