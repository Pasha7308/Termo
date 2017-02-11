package com.pasha.termo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.pasha.termo.model.WeatherDto;

public class DownloadWebpageText extends AsyncTask<Object, Object, WeatherDto> {

    private TextView objTermo;
    private TextView objIao;
    private ImageView imgGraphCollected;

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

            DrawView drawView = new DrawView(false, false);
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
