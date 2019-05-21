package com.example.kdiakonidze.beerapeni.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.widget.Toast;

import com.example.kdiakonidze.beerapeni.models.BeerModel;
import com.example.kdiakonidze.beerapeni.models.Obieqti;
import com.example.kdiakonidze.beerapeni.models.OrderCommentRowModel;
import com.example.kdiakonidze.beerapeni.models.PeerObjPrice;
import com.example.kdiakonidze.beerapeni.models.Useri;
import com.example.kdiakonidze.beerapeni.models.Xarji;
import com.example.kdiakonidze.beerapeni.services.NotificationService;

import java.util.ArrayList;

/**
 * Created by k.diakonidze on 10/30/2017.
 */

public class Constantebi {
    public static String APP_DISLOCATION = "თბილისი";
//    public static String APP_DISLOCATION = "კახეთი";

    private static String HOST = "http://apeni.ge/tbilisi/andr_app_links/";

    public static String URL_GET_OBIEQTS = HOST + "get_obieqts.php";
    public static String URL_GET_ORDERLIST = HOST + "get_shekvetebi.php";
    public static String URL_GET_LUDILIST = HOST + "get_ludi_list.php";
    public static String URL_INS_SHEKVETA = HOST + "insert_shekvetebi.php";
    public static String URL_INS_LUDISSHETANA = HOST + "insert_ludis_shetana_v2.php";
    public static String URL_INS_TAKEMONEY = HOST + "insert_moneyoutput.php";
    public static String URL_INS_TAKEKASRI = HOST + "insert_kasrioutput.php";
    public static String URL_GET_DAVALIANEBA = HOST + "get_davalianeba.php";
    public static String URL_GET_FASEBI = HOST + "get_fasebi.php";
    public static String URL_GET_SALEDAY =  HOST + "view_sale_day_v2.php";
    public static String URL_GET_AMONAWERI = HOST + "get_amonaweri_m.php";
    public static String URL_GET_AMONAWERI_K = HOST + "get_amonaweri_k.php";
    public static String URL_INS_AXALI_OBIEQTI = HOST + "insert_axali_obieqti.php";
    public static String URL_LOGIN =        HOST + "login.php";
    public static String URL_DEL_OBJ =      HOST + "del_obj.php";
    public static String URL_INS_AXALI_USERI = HOST + "insert_axali_momxmarebeli.php";
    public static String URL_GET_USERS =    HOST + "get_users.php";
    public static String URL_DEL_ORDER =    HOST + "del_order.php";
    public static String URL_DEL_BEER =     HOST + "del_beer.php";
    public static String URL_INS_BEER =     HOST + "insert_beer.php";
    public static String URL_DEL_RECORD =   HOST + "del_record_v2.php";
    public static String URL_GET_RECORD =   HOST + "get_record.php";
    public static String URL_GET_SYSCLEAN = HOST + "get_cleaning.php";
    public static String URL_INS_SYSCLEAN = HOST + "ins_cleaning.php";
    public static String URL_CH_PASS =      HOST + "ch_pass.php";
    public static String URL_GET_NASHTI =   HOST + "get_nashtebi.php";
    public static String URL_INS_SAWYOBI =  HOST + "sawyobi.php";
    public static String URL_GET_SAWY_DETAIL = HOST + "sawyobi_detail_list.php";
    public static String URL_INSERT_XARJI = HOST + "insert_xarji.php";
    public static String URL_GET_ORDER_COMMENTS = HOST + "get_order_comments.php";

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

    public static ArrayList<Xarji> XARJI_LIST = new ArrayList<>();
    public static ArrayList<OrderCommentRowModel> ORDER_COMMENTS = new ArrayList<>();

    public static Integer MDEBAREOBA_SHEKVETA = 1;
    public static Integer MDEBAREOBA_MITANA = 2;
    public static Integer MDEBAREOBA_AMONAWERI = 3;

    public static int screenDefOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_USER;

    public static Boolean loged_in = false;
    public static Float ACCURACY = 0.001f;
    public static NotificationService.NSinterface nSinterface;
    public static int oCount = 0;
}
