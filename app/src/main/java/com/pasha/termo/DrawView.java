package com.pasha.termo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.StringTokenizer;

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

    public DrawView(boolean isWidget) {
        this.isWidget = isWidget;

        paint = new Paint();
        paint.setColor(isWidget ? Color.WHITE : Color.BLACK);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeWidth(2);

        paintGrid = new Paint();
        paintGrid.setColor(isWidget ? Color.LTGRAY : Color.GRAY);
        paintGrid.setAntiAlias(true);
        paintGrid.setDither(true);
    }

    public void draw(Canvas canvas, ArrayList<Integer> temps) {
        this.temps = temps;
        if (temps == null || temps.size() == 0) {
            return;
        }
        getMinMax(canvas);
        drawGrid(canvas);

        int widthSingle = (width - 20) / (temps.size() - 1);
        for (int i = 0; i < temps.size(); i++) {
            Integer cur = temps.get(i);
            if (cur == null) {
                continue;
            }
            pnt.x = 20 + i * widthSingle;
            pnt.y = getCur(cur);
            canvas.drawCircle(pnt.x, pnt.y, ((height > width) ? width : height) / 100, paint);
            if (i == 0) {
                copyPoint();
                continue;
            }
            canvas.drawLine(pntLast.x, pntLast.y, pnt.x, pnt.y, paint);
            copyPoint();
        }
    }

    private void drawGrid(Canvas canvas) {
        int heightSingle = (height - 4) / totalDegrees;
        for (int i = 0; i < totalDegrees + 1; i++) {
            int y = 2 + ((i * heightSingle));
            canvas.drawLine(2, y, width - 4, y, paintGrid);
            int cur = max - i;
            canvas.drawText(String.valueOf(cur), 0, y + ((i == 0) ? -4 + textHeight : -6), paintGrid);
        }
        Log.i(LOG, "Width: " + width + ", Height: " + height + ", min:" + min  + ", max:" + max );
    }

    private int getCur(int cur) {
        return height - 2 + (int)(height * - ((((double)cur / 10) - min) / (double)(max - min)));
    }

    private void getMinMax(Canvas canvas) {
        int minReal = 9999;
        int maxReal = -9999;
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
        }
        width = canvas.getWidth();
        height = canvas.getHeight() - 4;

        int middle = ((maxReal + minReal) / 2);
        int middleTotal = totalDegrees / 2 + 1;
        min = middle / 10 - middleTotal;
        max = min + totalDegrees;
        textHeight = height / 15;
        paint.setTextSize(textHeight);
        paintGrid.setTextSize(textHeight);
    }

    private void copyPoint() {
        pntLast.x = pnt.x;
        pntLast.y = pnt.y;
    }
}
