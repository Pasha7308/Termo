package com.pasha.termo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.pasha.termo.model.WeatherDto;

public class DownloadWebpageText extends AsyncTask<Object, Object, WeatherDto> {

    protected TextView objTermo;
    protected TextView objIao;
    protected ImageView imgGraphCollected;

    protected WeatherDto doInBackground(
        Object... objs)
    {
        objTermo = (TextView)objs[0];
        objIao = (TextView)objs[1];
        imgGraphCollected = (ImageView)objs[2];

        TextDownloader textDownloader = new TextDownloader();
        return textDownloader.downloadUrl();

    }

    protected void onProgressUpdate(
        Integer... progress)
    {
    }
    
    protected void onPostExecute(
        WeatherDto dto)
    {
        if (dto != null) {
            objTermo.setText(termoToString(dto.getServerTermo().getTemp()));
            objIao.setText(termoToString(dto.getServerIao().getTemp()));

            Bitmap bm = Bitmap.createBitmap(192 * 2, 192, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bm);
            imgGraphCollected.setImageBitmap(bm);

            DrawView drawView = new DrawView(false);
            drawView.draw(canvas, dto.getOldValues());
        }
    }

    private String termoToString(Integer termo) {
        if (termo == null) {
            return "";
        }
        double dTemp = (double)termo / 10;
        if (dTemp < -10) {
            dTemp = Math.round(dTemp);
        }
        return String.valueOf(dTemp);
    }
}
