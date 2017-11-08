package com.example.kdiakonidze.beerapeni.utils;

import com.example.kdiakonidze.beerapeni.models.Obieqti;

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

    public static ArrayList<String> ludiList = new ArrayList<>();
    public static ArrayList<Obieqti> OBIEQTEBI = new ArrayList<>();

    public static Integer MDEBAREOBA_SHEKVETA = 1;
    public static Integer MDEBAREOBA_MITANA = 2;

}
