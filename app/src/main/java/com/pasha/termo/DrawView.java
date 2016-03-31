package com.pasha.termo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class DrawView extends View {
    private final static int totalDegrees = 5;

    Paint paintGrid = new Paint();
    Paint paint = new Paint();
    ArrayList<Integer> temps = null;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(false);
        paint.setDither(false);
        paint.setStrokeWidth(2);

        paintGrid.setColor(Color.GRAY);
        paintGrid.setAntiAlias(false);
        paintGrid.setDither(false);
        temps = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            temps.add(i);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (temps == null || temps.size() == 0) {
            return;
        }
        Point pntMax = new Point(canvas.getWidth(), canvas.getHeight() - 2);
        int widthSingle = (int)(pntMax.x * 0.90) / (temps.size() - 1);
        Integer max = null;
        Integer min = null;
        for (int i = 0; i < temps.size(); i++) {
            Integer cur = temps.get(i);
            if (cur == null) {
                continue;
            }
            if (max == null || max < cur) {
                max = cur;
            }
            if (min == null || min > cur) {
                min = cur;
            }
        }
        int middleCanvas = pntMax.y / 2;

        if (max == null || min == null) {
            return;
        }
        drawGrid(canvas, pntMax, min, max);

        double middle = (double)(max + min) / 2.0;
        int heightSingle = pntMax.y / (totalDegrees * 10);
        Point pntLast = new Point();
        for (int i = 0; i < temps.size(); i++) {
            Integer cur = temps.get(i);
            if (cur == null) {
                continue;
            }
            Point pnt = new Point();
            pnt.x = (int)(pntMax.x / 20) + i * widthSingle;
            pnt.y = 1 + (pntMax.y / 2) - (int)((cur - middle) * heightSingle);
            canvas.drawCircle(pnt.x, pnt.y, pntMax.y / 100, paint);
            if (i == 0) {
                pntLast = pnt;
                continue;
            }
            canvas.drawLine(pntLast.x, pntLast.y, pnt.x, pnt.y, paint);
            pntLast = pnt;
        }
    }
    private void drawGrid(Canvas canvas, Point pntMax, int min, int max) {
        int heightSingle = pntMax.y / totalDegrees ;
        int middle = ((max + min) / 2);
        int middleTotal = totalDegrees / 2;
        for (int i = 0; i < totalDegrees; i++) {
            int y = 1 + (int)((i * heightSingle));
            canvas.drawLine(1, y, pntMax.x - 2, y, paintGrid);
        }
    }

    public void setTemps(ArrayList<Integer> temps) {
        this.temps = temps;
    }
}
