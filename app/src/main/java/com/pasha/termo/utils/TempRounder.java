package com.pasha.termo.utils;

public class TempRounder {

    static public String round(Integer termo, boolean is1x1, boolean shorter)
    {
        if (termo == null) {
            return "0";
        }
        boolean makeShort = true;
        if (shorter && is1x1) {
            if ((termo >= 0) && (termo < 100)) {
                makeShort = false;
            }
        } else {
            if ((Math.abs(termo) < 100 && is1x1) || (termo > -100 && !is1x1)) {
                makeShort = false;
            }
        }
        double tempDouble = (double)termo / 10;
        if (makeShort) {
            int tempInt = (int)Math.round(tempDouble);
            String ret = String.valueOf(tempInt);
            if (ret.equals("0") && (termo < 0)) {
                ret = "-" + ret;
            }
            return ret;
        } else {
            return String.valueOf(tempDouble);
        }
    }

    static public String termoToString(Integer termo) {
        if (termo == null) {
            return "";
        }
        double dTemp = (double)termo / 10;
        return String.valueOf(dTemp);
    }
}
