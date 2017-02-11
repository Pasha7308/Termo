package com.pasha.termo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;

public class DrawView {
    private static final String LOG = "DrawView";

    private final static int totalDegrees = 5;

    private boolean isWidget;
    private Paint paintGrid;
    private Paint paint;

    private Point pnt = new Point();
    private Point pntLast = new Point();

    private ArrayList<Integer> temps = null;
    private int min;
    private int max;
    private int width;
    private int height;
    private int textHeight;

    public DrawView(boolean isWidget, boolean isDark) {
        this.isWidget = isWidget;

        paint = new Paint();
        paint.setColor(isDark ? Color.WHITE : Color.BLACK);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeWidth(2);

        paintGrid = new Paint();
        paintGrid.setColor(isDark ? Color.LTGRAY : Color.GRAY);
        paintGrid.setAntiAlias(true);
        paintGrid.setDither(true);
    }

    public void draw(Canvas canvas, ArrayList<Integer> temps) {
        fillTemps(temps);
        if (temps == null || temps.size() == 0) {
            return;
        }
        getMinMax(canvas);
        int widgetLineSize = height / 15;
        int radius = ((height > width) ? width : height) / 50;
        if (isWidget) {
            paint.setStrokeWidth(widgetLineSize);
        }
        if (!isWidget) {
            drawGrid(canvas);
        }

        int widthSingle = (width - 20) / (temps.size() - 1);
        for (int i = 0; i < temps.size(); i++) {
            Integer cur = temps.get(i);
            if (cur == null) {
                continue;
            }
            pnt.x = 20 + i * widthSingle;
            pnt.y = getCur(cur);
            canvas.drawCircle(pnt.x, pnt.y, isWidget ? widgetLineSize / 2 : radius, paint);
            if (i == 0) {
                copyPoint();
                continue;
            }
            canvas.drawLine(pntLast.x, pntLast.y, pnt.x, pnt.y, paint);
            copyPoint();
        }
    }

    private void drawGrid(Canvas canvas) {
        int heightSingle = height / totalDegrees;
        for (int i = 0; i < totalDegrees + 1; i++) {
            int y = 2 + ((i * heightSingle));
            canvas.drawLine(2, y, width - 4, y, paintGrid);
            int cur = max / 10 - i;
            canvas.drawText(String.valueOf(cur), 0, y + ((i == 0) ? textHeight : -2), paintGrid);
        }
        Log.i(LOG, "Width: " + width + ", Height: " + height + ", min:" + min  + ", max:" + max );
    }

    private int getCur(int cur) {
        int ret = height - 2 - (int) (height * ((((double) cur) - min) / (double) (max - min)));
        return ret;
    }

    private void getMinMax(Canvas canvas) {
        int minReal = 9999;
        int maxReal = -9999;
        int last = 9999;
        for (int i = 0; i < temps.size(); i++) {
            Integer cur = temps.get(i);
            if (cur == null) {
                continue;
            }
            if (maxReal == -9999 || maxReal < cur) {
                maxReal = cur;
            }
            if (minReal == 9999 || minReal > cur) {
                minReal = cur;
            }
            last = cur;
        }
        width = canvas.getWidth();
        height = canvas.getHeight() - 4;

        if (!isWidget) {
            int middle = ((maxReal + minReal) / 2);
            int middleTotal = totalDegrees / 2;
            min = (middle / 10 - middleTotal) * 10;
            max = min + (totalDegrees * 10);
        } else {
            int middle = ((maxReal + minReal) / 2);
            int middleTotal = (totalDegrees * 10) / 2 + 1;
            min = middle - middleTotal;
            max = min + (totalDegrees * 10);
            if (last != 9999) {
                if (last < min) {
                    min = last;
                    max = min + (totalDegrees * 10);
                }
                if (last > max) {
                    max = last;
                    min = max - (totalDegrees * 10);
                }
            }
        }
        textHeight = height / 12;
        paint.setTextSize(textHeight);
        paintGrid.setTextSize(textHeight);
    }

    private void copyPoint() {
        pntLast.x = pnt.x;
        pntLast.y = pnt.y;
    }

    private void fillTemps(ArrayList<Integer> tempsIn) {
        temps = tempsIn;
        Log.i(LOG, "size: " + temps.size());
        if (isWidget) {
            // remove all values except last hour
            int size = temps.size();
            for (int i = 0; i < (size - 12); i++) {
                temps.remove(0);
            }
        }
        Log.i(LOG, "size: " + temps.size());
    }
}
