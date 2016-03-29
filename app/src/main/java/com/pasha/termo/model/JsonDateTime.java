package com.pasha.termo.model;

import com.pasha.termo.utils.DateUtils;

import java.util.Date;

public class JsonDateTime {
    private Date localDateTime = null;

    public String getLocalDateTime() {
        return toString();
    }

    public void setLocalDateTime(String string) {
        localDateTime = DateUtils.stringToDate(string);
    }

    public Date GetLocalDateTime() {
        return localDateTime;
    }

    public void SetLocalDateTime(Date localDateTimeIn) {
        localDateTime = localDateTimeIn;
    }

    public Date GetDateTime() {
        return localDateTime;
    }

    public void SetDateTime(Date dateTime) {
        localDateTime = dateTime;
    }

    public JsonDateTime(String string) {
        localDateTime = DateUtils.stringToDate(string);
    }

    public JsonDateTime() {
        localDateTime = null;
    }

    public boolean IsEmpty() {
        return localDateTime == null;
    }

    @Override
    public String toString() {
        return DateUtils.dateToString(localDateTime);
    }
}
