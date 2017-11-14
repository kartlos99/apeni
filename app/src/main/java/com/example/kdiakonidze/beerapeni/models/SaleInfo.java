package com.example.kdiakonidze.beerapeni.models;

import java.io.Serializable;

/**
 * Created by k.diakonidze on 11/13/2017.
 */

public class SaleInfo implements Serializable {
    String beerName;
    Double pr;
    int litraji, k30, k50;

    public SaleInfo(String beerName, Double pr, int litraji, int k30, int k50) {
        this.beerName = beerName;
        this.pr = pr;
        this.litraji = litraji;
        this.k30 = k30;
        this.k50 = k50;
    }

    public String getBeerName() {
        return beerName;
    }

    public Double getPr() {
        return pr;
    }

    public int getLitraji() {
        return litraji;
    }

    public int getK30() {
        return k30;
    }

    public int getK50() {
        return k50;
    }
}
