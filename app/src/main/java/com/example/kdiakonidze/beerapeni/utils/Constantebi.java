package com.example.kdiakonidze.beerapeni.utils;

import android.app.Application;
import android.content.pm.ActivityInfo;
import android.widget.Toast;

import com.example.kdiakonidze.beerapeni.models.BeerModel;
import com.example.kdiakonidze.beerapeni.models.Obieqti;
import com.example.kdiakonidze.beerapeni.models.PeerObjPrice;
import com.example.kdiakonidze.beerapeni.models.Useri;

import java.util.ArrayList;

/**
 * Created by k.diakonidze on 10/30/2017.
 */

public class Constantebi {
    public static String URL_GET_OBIEQTS =  "http://apeni.ge/andr_app_links/get_obieqts.php";
    public static String URL_GET_ORDERLIST = "http://apeni.ge/andr_app_links/get_shekvetebi.php";
    public static String URL_GET_LUDILIST = "http://apeni.ge/andr_app_links/get_ludi_list.php";
    public static String URL_INS_SHEKVETA = "http://apeni.ge/andr_app_links/insert_shekvetebi.php";
    public static String URL_INS_LUDISSHETANA = "http://apeni.ge/andr_app_links/insert_ludis_shetana.php";
    public static String URL_INS_TAKEMONEY = "http://apeni.ge/andr_app_links/insert_moneyoutput.php";
    public static String URL_INS_TAKEKASRI = "http://apeni.ge/andr_app_links/insert_kasrioutput.php";
    public static String URL_GET_DAVALIANEBA = "http://apeni.ge/andr_app_links/get_davalianeba.php";
    public static String URL_GET_FASEBI = " http://apeni.ge/andr_app_links/get_fasebi.php";
    public static String URL_GET_SALEDAY =  "http://apeni.ge/andr_app_links/view_sale_day.php";
    public static String URL_GET_AMONAWERI = "http://apeni.ge/andr_app_links/get_amonaweri_m.php";
    public static String URL_GET_AMONAWERI_K = "http://apeni.ge/andr_app_links/get_amonaweri_k.php";
    public static String URL_INS_AXALI_OBIEQTI = "http://apeni.ge/andr_app_links/insert_axali_obieqti.php";
    public static String URL_LOGIN =        "http://apeni.ge/andr_app_links/login.php";
    public static String URL_DEL_OBJ =      "http://apeni.ge/andr_app_links/del_obj.php";
    public static String URL_INS_AXALI_USERI = "http://apeni.ge/andr_app_links/insert_axali_momxmarebeli.php";
    public static String URL_GET_USERS =    "http://apeni.ge/andr_app_links/get_users.php";
    public static String URL_DEL_ORDER =    "http://apeni.ge/andr_app_links/del_order.php";
    public static String URL_DEL_BEER =     "http://apeni.ge/andr_app_links/del_beer.php";
    public static String URL_INS_BEER =     "http://apeni.ge/andr_app_links/insert_beer.php";
    public static String URL_DEL_RECORD =   "http://apeni.ge/andr_app_links/del_record.php";
    public static String URL_GET_RECORD =   "http://apeni.ge/andr_app_links/get_record.php";
    public static String URL_GET_SYSCLEAN = "http://apeni.ge/andr_app_links/get_cleaning.php";
    public static String URL_INS_SYSCLEAN = "http://apeni.ge/andr_app_links/ins_cleaning.php";
    public static String URL_CH_PASS =      "http://apeni.ge/andr_app_links/ch_pass.php";
    public static String URL_GET_NASHTI =   "http://apeni.ge/andr_app_links/get_nashtebi.php";
    public static String URL_INS_SAWYOBI =  "http://apeni.ge/andr_app_links/sawyobi.php";
    public static String URL_GET_SAWY_DETAIL=  "http://apeni.ge/andr_app_links/sawyobi_detail_list.php";

    public static String USER_USERNAME = "";
    public static String USER_NAME = "";
    public static String USER_TYPE = "";
    public static String USER_ID = "";
    public static String USER_PASS = "";
    public static String USER_FILENAME = "userdata.txt";
    public static String USER_TYPE_admin = "2";
    public static String USER_TYPE_user = "1";

    public static String REASON = "mizezi";
    public static String CREATE = "ახალი ობიექტი";
    public static String CREATE_USER = "ახალი მომხმარებელი";
    public static String EDIT = "რედაქტირება";
    public static String NEW_ORDER = "შეკვეთის დამატება";
    public static String MITANA = "mitana";
    public static String K_OUT = "kout";
    public static String M_OUT = "mout";
    public static String ORDER = "order";
    public static String TABLE_SAWY_IN = "sawyobi_in";
    public static String TABLE_SAWY_OUT = "sawyobi_out";
    public static String SAWYOBSHI_SHETANA = "SAWYOBSHI_SHETANA";
    public static String SAWYOBIDAN_GATANA = "SAWYOBIDAN_GATANA";

    public static String MSG_DEL = "გსურთ ჩანაწერის წაშლა?";

    public static ArrayList<BeerModel> ludiList = new ArrayList<>();
    public static ArrayList<Obieqti> OBIEQTEBI = new ArrayList<>();
    public static ArrayList<PeerObjPrice> FASEBI = new ArrayList<>();
    public static ArrayList<Useri> USERsLIST = new ArrayList<>();

    public static Integer MDEBAREOBA_SHEKVETA = 1;
    public static Integer MDEBAREOBA_MITANA = 2;
    public static Integer MDEBAREOBA_AMONAWERI = 3;

    public static int screenDefOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_USER;

    public static Boolean loged_in = false;
}
