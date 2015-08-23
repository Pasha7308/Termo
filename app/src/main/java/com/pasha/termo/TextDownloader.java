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
    static private final String xmlUrlTermo = "http://termo.tomsk.ru/data.xml";
    static private final String xmlUrlIao = "http://meteo.iao.ru/weather.php?lang=en";

    static public String getXmlUrlTermo()
    {
        return xmlUrlTermo;
    }

    static public String getXmlUrlIao()
    {
        return xmlUrlIao;
    }

    public String downloadUrl(
        DownloadWebpageSource source)
    {
        Log.i(LOG, "downloadUrl");

        String strUrl = "";
        switch (source) {
            case Termo:
                strUrl = getXmlUrlTermo();
                break;
            case Iao:
                strUrl = getXmlUrlIao();
        }
        String strRet = "";
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
                String strIn = readIt(is, len);
                switch (source) {
                    case Termo:
                        strRet = processItTermo(strIn);
                        break;
                    case Iao:
                        strRet = processItIao(strIn);
                        break;
                }
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
        return strRet;
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

    public String processItTermo(
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

    public String processItIao(
            String strIn)
    {
    /*
     * 	     {"datetime":1440342000,"temp":18,"hum":38,"press":742,"wind_s":2.5,"wind_d":"w","dewp":3.4}
     */
        if (!strIn.contains("datetime") || !strIn.contains("temp")) {
            return "Wrong answer";
        }
        int iStart = strIn.indexOf("\"temp\":") + "\"temp\":".length();
        int iFinish = strIn.indexOf(",\"hum\"");
        return strIn.substring(iStart, iFinish);
    }

}
