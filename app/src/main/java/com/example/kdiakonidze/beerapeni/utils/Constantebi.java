package com.example.kdiakonidze.beerapeni.utils;

import com.example.kdiakonidze.beerapeni.models.Obieqti;
import com.example.kdiakonidze.beerapeni.models.PeerObjPrice;

import java.util.ArrayList;

/**
 * Created by k.diakonidze on 10/30/2017.
 */

public class Constantebi {
    public static String URL_GET_OBIEQTS = "http://apeni.ge/andr_app_links/get_obieqts.php";
    public static String URL_GET_ORDERLIST = "http://apeni.ge/andr_app_links/get_shekvetebi.php";
    public static String URL_GET_LUDILIST = "http://apeni.ge/andr_app_links/get_ludi_list.php";
    public static String URL_INS_SHEKVETA = "http://apeni.ge/andr_app_links/insert_shekvetebi.php";
    public static String URL_INS_LUDISSHETANA = "http://apeni.ge/andr_app_links/insert_ludis_shetana.php";
    public static String URL_INS_TAKEMONEY = "http://apeni.ge/andr_app_links/insert_moneyoutput.php";
    public static String URL_INS_TAKEKASRI = "http://apeni.ge/andr_app_links/insert_kasrioutput.php";
    public static String URL_GET_DAVALIANEBA = "http://apeni.ge/andr_app_links/get_davalianeba.php";
    public static String URL_GET_FASEBI = "http://apeni.ge/andr_app_links/get_fasebi.php";
    public static String URL_GET_SALEDAY = "http://apeni.ge/andr_app_links/view_sale_day.php";
    public static String URL_GET_AMONAWERI = "http://apeni.ge/andr_app_links/get_amonaweri_m.php";
    public static String URL_GET_AMONAWERI_K = "http://apeni.ge/andr_app_links/get_amonaweri_k.php";

    public static ArrayList<String> ludiList = new ArrayList<>();
    public static ArrayList<Obieqti> OBIEQTEBI = new ArrayList<>();
    public static ArrayList<PeerObjPrice> FASEBI = new ArrayList<>();

    public static Integer MDEBAREOBA_SHEKVETA = 1;
    public static Integer MDEBAREOBA_MITANA = 2;
    public static Integer MDEBAREOBA_AMONAWERI = 3;

}
