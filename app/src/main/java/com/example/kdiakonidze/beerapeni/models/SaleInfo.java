package com.example.kdiakonidze.beerapeni.models;

import java.io.Serializable;

/**
 * Created by k.diakonidze on 11/13/2017.
 */

public class SaleInfo implements Serializable {
    private String beerName;
    private float pr, k30, k50;
    private int litraji;

    public SaleInfo(String beerName, float pr, int litraji, float k30, float k50) {
        this.beerName = beerName;
        this.pr = pr;
        this.litraji = litraji;
        this.k30 = k30;
        this.k50 = k50;
    }

    public String getBeerName() {
        return beerName;
    }

    public float getPr() {
        return pr;
    }

    public int getLitraji() {
        return litraji;
    }

    public float getK30() {
        return k30;
    }

    public float getK50() {
        return k50;
    }
}
