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
import java.nio.charset.StandardCharsets;

public class TextDownloader {

    private static final String LOG = "TextDownloader";
    static private final String url = "http://pasha7308.asuscomm.com:8081/weather";

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
                conn.setReadTimeout(15000); // milliseconds
                conn.setConnectTimeout(20000); // milliseconds
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
        Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        char[] buffer = new char[len];
        return (reader.read(buffer) > 0) ? new String(buffer) : "";
    }

    private WeatherDto processIt(
        String strIn)
    {
        try {
            return new ObjectMapper().readValue(strIn, WeatherDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*  test values
        try {
            WeatherDto dto = new ObjectMapper().readValue(strIn, WeatherDto.class);

            dto.getOldValues().clear();
            for (int i = 0; i < 19; i++) {
                dto.getOldValues().add(i*-2 - 10);
            }
            dto.getOldValues().add(dto.getOldValues().get(dto.getOldValues().size() - 1) - 5);
            dto.getServerTermo().setTemp(dto.getOldValues().get(dto.getOldValues().size() - 1));

            dto.getOldValuesIao().clear();
            for (int i = 0; i < 19; i++) {
                dto.getOldValuesIao().add(i*-2 - 35);
            }
            dto.getOldValuesIao().add(dto.getOldValuesIao().get(dto.getOldValuesIao().size() - 1) + 5);
            dto.getServerIao().setTemp(dto.getOldValuesIao().get(dto.getOldValuesIao().size() - 1));
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        return null;
    }
}
