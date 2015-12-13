package com.pasha.termo;

public class TempRounder {

    static String Round(
        String currentTemp)
    {
        double tempDouble = Double.parseDouble(currentTemp);
        if (Math.abs(tempDouble) < 10) {
            return String.valueOf(tempDouble);
        } else {
            int tempInt = (int)Math.round(tempDouble);
            return String.valueOf(tempInt);
        }
    }
}