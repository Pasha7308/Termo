package com.pasha.termo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadWebpageGraph extends AsyncTask<Object, Integer, Bitmap> {

	protected ImageView objView;
	
    protected Bitmap doInBackground(
        Object... objs)
    {
    	String strUrl = (String)objs[0];
    	objView = (ImageView)objs[1];
        try {
            return downloadUrl(strUrl);
        } catch (IOException e) {
            return null;
        }
    }

    protected void onProgressUpdate(
        Integer... progress)
    {
    }
    
    protected void onPostExecute(
        Bitmap bitmap)
    {
    	if (bitmap != null) {
    		objView.setImageBitmap(bitmap);
    	}
    }

    private Bitmap downloadUrl(
        String strUrll)
        throws IOException
    {
        InputStream is = null;

        try {
            URL url = new URL(strUrll);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            is = conn.getInputStream();

            return readIt(is);
            
        } finally {
            if (is != null) {
                is.close();
            } 
        }
    }

    public Bitmap readIt(
        InputStream stream)
        throws IOException
    {
    	return BitmapFactory.decodeStream(stream);
    }
    
}
