package com.pasha.termo;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pasha.termo.model.WeatherDto;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TextDownloader {

    private static final String LOG = "TextDownloader";
    static private final String url = "https://termotomsk-183603.appspot.com/weather";

    public WeatherDto downloadUrl()
    {
        Log.i(LOG, "downloadUrl");

        String strUrl = url;
        WeatherDto dto = null;
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
                dto = processIt(strIn);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
                if (is != null) {
                    is.close();
                }
            }
        } catch (IOException E) {
            return dto;
        }
        return dto;
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

    private WeatherDto processIt(
        String strIn)
    {
        WeatherDto dto = null;
        try {
            dto = new ObjectMapper().readValue(strIn, WeatherDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }

}
