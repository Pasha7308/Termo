package com.pasha.termo;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TextDownloader {

    private static final String LOG = "TextDownloader";
    static private final String xmlUrl = "http://termo.tomsk.ru/data.xml";

    static public String getXmlUrl()
    {
        return xmlUrl;
    }

    public String downloadUrl(
        String strUrl)
    {
        Log.i(LOG, "downloadUrl");

        try {
            InputStream is = null;
            HttpURLConnection conn = null;
            int len = 1000;

            try {
                URL url = new URL(strUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000); // milliseconds
                conn.setConnectTimeout(150060); // milliseconds
                is = conn.getInputStream();
                return processIt(readIt(is, len));
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                if (is != null) {
                    is.close();
                }
            }
        } catch (IOException E) {
            return "N/A";
        }
    }

    private String readIt(
        InputStream stream,
        int len)
        throws IOException
    {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    public String processIt(
        String strIn)
    {
    /*
     * 	     	<current temp="-21.0" date="28.01.2013" time="21:19" change="-"/>
     */
        if (strIn.length() < 500) {
            return "Wrong answer";
        }
        int iStart = strIn.indexOf("<current temp=\"") + "<current temp=\"".length();
        int iFinish = strIn.indexOf("\" date=\"");
        return strIn.substring(iStart, iFinish);
    }

}
