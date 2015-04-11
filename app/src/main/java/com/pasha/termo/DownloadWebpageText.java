package com.pasha.termo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.widget.TextView;

public class DownloadWebpageText extends AsyncTask<Object, Integer, String> {

	protected TextView objView;
	
    protected String doInBackground(
        Object... objs)
    {
    	objView = (TextView)objs[0];
        TextDownloader textDownloader = new TextDownloader();
        return textDownloader.downloadUrl(TextDownloader.getXmlUrl());
    }

    protected void onProgressUpdate(
        Integer... progress)
    {
    }
    
    protected void onPostExecute(
        String result)
    {
    	objView.setText(result);
    }

}
