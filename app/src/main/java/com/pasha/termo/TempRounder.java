package com.pasha.termo;

public class TempRounder {

    static String Round(
        Integer termo)
    {
        double tempDouble = (double)termo / 10;
        if (Math.abs(tempDouble) < 10) {
            return String.valueOf(tempDouble);
        } else {
            int tempInt = (int)Math.round(tempDouble);
            return String.valueOf(tempInt);
        }
    }
}
