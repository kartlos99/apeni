package com.example.kdiakonidze.beerapeni.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.kdiakonidze.beerapeni.models.Shekvetebi;
import com.example.kdiakonidze.beerapeni.models.Useri;
import com.example.kdiakonidze.beerapeni.models.Xarji;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyUtil {

    private static final String SHARED_PREF = "my_pref";
    private static final String DATA_KEY = "datakey";
    private Context mContext;

    public MyUtil(Context context) {
        mContext = context;
    }
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

    public static float tempListPrice(ArrayList<Shekvetebi> tempList) {
        float n = 0;
        for (Shekvetebi tempItem : tempList) {
            n += (tempItem.getK30in() * 30 + tempItem.getK50in() * 50) * Float.valueOf(tempItem.getComment());
        }
        return n;
    }

    public static float totalXarji(ArrayList<Xarji> xarjebi) {
        float n = 0;
        for (Xarji xarji : xarjebi) {
            n += xarji.getAmount();
        }
        return n;
    }

    public static String getUserName(int id) {
        for (Useri user : Constantebi.USERsLIST) {
            if (user.getId() == id){
                return user.getUsername();
            }
        }
        return "";
    }

    public void notifyFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myMsgRef = database.getReference(PrivateKey.FIREBASE_DB_TREE);
        Date date = new Date();
        myMsgRef.setValue(date.toString());

        saveLastValue(date.toString());
    }

    public void saveLastValue(String data){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DATA_KEY, data);
        editor.apply();
    }

    public String loadLastValue(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString(DATA_KEY, "");
    }
}
