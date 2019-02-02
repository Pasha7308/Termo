package com.pasha.termo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.pasha.termo.model.ServerType;

import java.util.ArrayList;

public class DrawView {
    private static final String LOG = "DrawView";

    private final static int totalDegrees = 5;

    private boolean minmaxFound;
    private boolean isWidget;
    private Paint paintGrid;
    private Paint paintTermo;
    private Paint paintIao;

    private Point pnt = new Point();
    private Point pntLast = new Point();

    private ArrayList<Integer> temps = null;
    private int min;
    private int max;
    private int width;
    private int height;
    private int textHeight;

    DrawView(boolean isWidget, boolean isDark) {
        minmaxFound = false;
        temps = new ArrayList<>();
        this.isWidget = isWidget;

        paintTermo = new Paint();
        paintTermo.setColor(isDark ?
                Color.WHITE :
                Color.BLACK);
        paintTermo.setAntiAlias(true);
        paintTermo.setDither(true);
        paintTermo.setStrokeWidth(2);

        paintIao = new Paint();
        paintIao.setColor(isDark ?
                Color.argb(255, 128, 255, 128) :
                Color.argb(255, 0,   128, 0));
        paintIao.setAntiAlias(true);
        paintIao.setDither(true);
        paintIao.setStrokeWidth(2);

        paintGrid = new Paint();
        paintGrid.setColor(isDark ?
                Color.LTGRAY :
                Color.GRAY);
        paintGrid.setAntiAlias(true);
        paintGrid.setDither(true);
    }

    public void draw(Canvas canvas, ArrayList<Integer> temps, ServerType serverType) {
        fillTemps(temps);
        if (temps.size() == 0) {
            return;
        }
        getMinMax(canvas);
        int widgetLineSize = height / 15;
        int radius = ((height > width) ? width : height) / 50;
        if (isWidget) {
            paintTermo.setStrokeWidth(widgetLineSize);
        }
        if (!isWidget) {
            drawGrid(canvas);
        }

        int widthSingle = (width - 20) / (temps.size() - 1);
        Paint paint = serverType == ServerType.Termo ? paintTermo : paintIao;
        for (int i = 0; i < temps.size(); i++) {
            Integer cur = temps.get(i);
            if (cur == null) {
                continue;
            }
            pnt.x = 20 + i * widthSingle;
            pnt.y = getCur(cur);
            canvas.drawCircle(pnt.x, pnt.y, isWidget ? (float)(widgetLineSize / 2) : radius, paint);
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
//        Log.i(LOG, "Width: " + width + ", Height: " + height + ", min:" + min  + ", max:" + max );
    }

    private int getCur(int cur) {
        int ret;
        ret = height - 2 - (int) (height * ((((double) cur) - min) / (double) (max - min)));
        return ret;
    }

    private void getMinMax(Canvas canvas) {
        if (minmaxFound) {
            return;
        }
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
        paintTermo.setTextSize(textHeight);
        paintGrid.setTextSize(textHeight);
        minmaxFound = true;
    }

    private void copyPoint() {
        pntLast.x = pnt.x;
        pntLast.y = pnt.y;
    }

    private void fillTemps(ArrayList<Integer> tempsIn) {
        temps.clear();
        for (int i = isWidget ? tempsIn.size() - 12 : 0; i < tempsIn.size(); i++) {
            temps.add(tempsIn.get(i));
        }
    }
}
