package com.example.kdiakonidze.beerapeni.utils;

import com.example.kdiakonidze.beerapeni.models.Shekvetebi;
import com.example.kdiakonidze.beerapeni.models.Xarji;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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

    public static ArrayList<Shekvetebi> objToOrderList(Object listObj) {
        ArrayList<Shekvetebi> list = new ArrayList<>();
        if (listObj instanceof List) {
            for (int i = 0; i < ((List) listObj).size(); i++) {
                Object item = ((List) listObj).get(i);
                if (item instanceof Shekvetebi) {
                    list.add((Shekvetebi) item);
                }
            }
        }
        return list;
    }

    public static float tempListPrice(ArrayList<Shekvetebi> tempList){
        float n = 0;
        for (Shekvetebi tempItem : tempList){
            n += (tempItem.getK30in()*30 + tempItem.getK50in()*50) * Float.valueOf(tempItem.getComment());
        }
        return n;
    }

    public static float totalXarji(ArrayList<Xarji> xarjebi){
        float n = 0;
        for (Xarji xarji: xarjebi){
            n += xarji.getAmount();
        }
        return n;
    }
}
