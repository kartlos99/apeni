package com.example.kdiakonidze.beerapeni.utils;

import java.text.DecimalFormat;

public class MyUtil {

    private static DecimalFormat df = new DecimalFormat("#0.00");

    public static String pliusMinusText(String stringNumber, String oper) {
        // oper   (true = +) (false = -)
        if (stringNumber.equals("")) {
            stringNumber = "0";
        }
        float ii = Float.valueOf(stringNumber);

        if (oper.equals("+")) {
            ii++;
        } else {
            if (ii >= 1) {
                ii--;
            }
        }
        return floatToSmartStr(ii);
    }

    public static String floatToSmartStr(Float number) {
        long intNumb = Math.round(number);
        if (Math.abs(number - intNumb) < Constantebi.ACCURACY) {
            return String.valueOf(intNumb);
        }
        return df.format(number);
    }

}
