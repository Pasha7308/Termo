package com.pasha.termo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class DrawView extends View {
    Paint paint = new Paint();
    ArrayList<Integer> temps = null;

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.BLACK);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (temps == null || temps.size() == 0) {
            return;
        }
        int width = canvas.getWidth() / temps.size();
        for (int i = 0; i < temps.size(); i++) {
            canvas.drawLine(
                    canvas.getWidth() - (i * width),
                    0,
                    canvas.getWidth() - ((i + 1) * width),
                    canvas.getHeight(),
                    paint);
        }
    }

    public void setTemps(ArrayList<Integer> temps) {
        this.temps = temps;
    }
}
