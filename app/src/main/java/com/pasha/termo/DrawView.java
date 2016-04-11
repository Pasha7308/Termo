package com.pasha.termo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class DrawView extends View {
    private final static int totalDegrees = 5;
    private final static int textHeight = 20;

    Paint paintGrid = new Paint();
    Paint paint = new Paint();

    Point pnt = new Point();
    Point pntLast = new Point();

    ArrayList<Integer> temps = null;
    int min = 9999, max = -9999;
    double middle = 0.0;
    int heightSingle = 100;
    int width = 0, height = 0;

    public void setTemps(ArrayList<Integer> temps) {
        this.temps = temps;
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(false);
        paint.setDither(false);
        paint.setStrokeWidth(2);
        paint.setTextSize(30);

        paintGrid.setColor(Color.GRAY);
        paintGrid.setAntiAlias(false);
        paintGrid.setDither(false);
        temps = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            temps.add(i);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (temps == null || temps.size() == 0) {
            return;
        }
        getMinMax(canvas);
        int widthSingle = (width - 20) / (temps.size() - 1);
        drawGrid(canvas);

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
        int middle = ((max + min) / 2);
        int middleTotal = totalDegrees / 2;
        min = middle / 10 - middleTotal;
        max = min + totalDegrees;
        for (int i = 0; i < totalDegrees + 1; i++) {
            int y = 2 + ((i * heightSingle));
            canvas.drawLine(2, y, width - 4, y, paintGrid);
            int cur = max - i;
            canvas.drawText(String.valueOf(cur), 2, y + ((i == 0) ? 2 + textHeight : -2), paint);
        }
    }

    private int getCur(int cur) {
        return height - 2 + (int)(height * - ((((double)cur / 10) - min) / (double)(max - min)));
    }

    private void getMinMax(Canvas canvas) {
        for (int i = 0; i < temps.size(); i++) {
            Integer cur = temps.get(i);
            if (cur == null) {
                continue;
            }
            if (max == -9999 || max < cur) {
                max = cur;
            }
            if (min == 9999 || min > cur) {
                min = cur;
            }
        }
        middle = (double)(max + min) / 2.0;
        heightSingle = canvas.getHeight() / (totalDegrees * 10);
        width = canvas.getWidth();
        height = canvas.getHeight() - 4;
    }

    private void copyPoint() {
        pntLast.x = pnt.x;
        pntLast.y = pnt.y;
    }
}
